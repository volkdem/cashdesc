package com.volkdem.ecashier.officer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.common.model.Order;
import com.common.model.Product;
import com.volkdem.ecashier.officer.services.OrderSyncService;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Evgeny on 31.05.2016.
 */
class OrdersDatabase {
    private static final String TAG = OrdersDatabase.class.getName();
    private static final String DATABASE_NAME = "ORDERS";
    private static final String ORDERS_TABLE_NAME = "ORDERS";
    private static final String PRODUCTS_TABLE_NAME = "PRODUCTS";

    private final DatabaseOpenHelper openHelper;
    private static OrdersDatabase ordersDatabase;

    public static OrdersDatabase getDatabase( Context context ) {
        if( ordersDatabase == null ) {
            synchronized ( TAG ) {
                if( ordersDatabase == null ) {
                    ordersDatabase = new OrdersDatabase( context );
                }
            }
        }

        return ordersDatabase;
    }

    private OrdersDatabase(Context context) {
        openHelper = new DatabaseOpenHelper( context );
    }

    static class CheckStatus {
        public static final int UNCHECKED = 0;
        public static final int CHECKED = 1;

        public static int convertCheckStatus( Boolean checkStatus) {
            return ( checkStatus ) ? CHECKED : UNCHECKED;
        }
    }



    class OrderColumn {
        private static final String ID = "_id";
        private static final String PAYMENT_CODE = "PAYMENT_CODE";
        private static final String PAYMENT_DATE = "PAYMENT_DATE";
        private static final String CHECK_STATUS = "CHECK_STATUS";
    }

    class ProductColumn {
        private static final String ID = "_id";
        private static final String ORDER_ID = "ORDER_ID";
        private static final String NAME = "NAME";
        private static final String COUNT = "COUNT";
        private static final String PRICE = "PRICE";
    }

    public void addOrders( List< Order > orders) {
        openHelper.addOrders( orders );
    }

    public void addOrder( Order order ) {
        openHelper.addOrder( order );
    }

    private static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private SQLiteDatabase database;
        private static final int DATABASE_VERSION = new Random(new Date().getTime()).nextInt(999999);

        private static final String ORDERS_TABLE_CREATE = generateCreateVirtualTableStatement(
                ORDERS_TABLE_NAME, OrderColumn.ID, OrderColumn.PAYMENT_CODE, OrderColumn.PAYMENT_DATE, OrderColumn.CHECK_STATUS );

        private static final String PRODUCTS_TABLE_CREATE = generateCreateVirtualTableStatement(
                PRODUCTS_TABLE_NAME, ProductColumn.ID, ProductColumn.ORDER_ID, ProductColumn.NAME, ProductColumn.COUNT, ProductColumn.PRICE );

        private static String generateCreateVirtualTableStatement( String tableName, String ... columns ) {
            final String topPattern = "CREATE VIRTUAL TABLE {0} USING fts3( {1} )";
            StringBuilder columnsStr = new StringBuilder();
            for( String column: columns ) {
                columnsStr.append( column ).append( ", " );
            }
            columnsStr.delete( columnsStr.length() - 2, columnsStr.length() - 1 );
            return MessageFormat.format( topPattern, tableName, columnsStr );
        };

        public DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d( TAG, "onCreate" );
            this.database = db;
            database.execSQL( ORDERS_TABLE_CREATE );
            database.execSQL( PRODUCTS_TABLE_CREATE );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d( TAG, MessageFormat.format( "Upgrade database old version from {0} to {1} which will destroy all data.", oldVersion, newVersion ) );
            db.execSQL( "DROP TABLE IF EXISTS " + ORDERS_TABLE_NAME );
            db.execSQL( "DROP TABLE IF EXISTS " + PRODUCTS_TABLE_NAME );
            onCreate( db );
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion );
        }

        public void addOrders(List< Order > orders) {
            database = this.getWritableDatabase();
            for( Order order: orders ) {
                addOrder( order );
            }
            this.close();
        }


        public void addOrder( Order order ) {
            boolean opened = true;
            if ( !database.isOpen() ) {
                database = this.getWritableDatabase();
                opened = false;
            }

            ContentValues initialValues = new ContentValues();
            initialValues.put( OrderColumn.ID, order.getId() );
            initialValues.put( OrderColumn.PAYMENT_CODE, order.getPaymentCode() );
            initialValues.put( OrderColumn.PAYMENT_DATE, OrderMapper.formatDatetime( order.getPaymentDate() ) );
            // TODO: get check status from the Order
            int checkStatus = ( order.isCheckStatus() ) ? CheckStatus.CHECKED : CheckStatus.UNCHECKED;
            Log.d( TAG, "addOrder(), checkStatus=" + checkStatus );
            initialValues.put( OrderColumn.CHECK_STATUS, checkStatus );

            database.beginTransaction();
            try {
                database.insert(ORDERS_TABLE_NAME, null, initialValues);

                for (Map.Entry product : order.getProducts().entrySet()) {
                    addProduct(order.getId(), (Product) product.getKey(), (Integer) product.getValue());
                }
                database.setTransactionSuccessful();
            } catch (Exception e) {
                // TODO: write to database or file to send for anylyze to server
                e.printStackTrace();
            } finally {
                database.endTransaction();
                if( !opened ) {
                    this.close();
                }
            }
        }


        private void addProduct(Long orderId, Product product, Integer count) {
            ContentValues values = new ContentValues();
            values.put( ProductColumn.ID, product.getProductId());
            values.put( ProductColumn.ORDER_ID, orderId);
            values.put( ProductColumn.NAME, product.getProductName());
            values.put( ProductColumn.COUNT, count);
            values.put( ProductColumn.PRICE, product.getPrice().doubleValue() );

            database.insertOrThrow( PRODUCTS_TABLE_NAME, null, values );
        }
    }

    public Cursor getOrders( OrdersSearchCriteria searchCriteria ) {
        final String[] columns = new String[] { OrderColumn.ID, OrderColumn.PAYMENT_CODE, OrderColumn.PAYMENT_DATE, OrderColumn.CHECK_STATUS };

        return buildQuery( columns, searchCriteria, null, OrderColumn.PAYMENT_DATE + " desc");
    }

    private Cursor buildQuery( String[] columns, OrdersSearchCriteria searchCriteria, String groupBy, String orderBy ) {
        Log.d( TAG, MessageFormat.format( "buildQuery(), columns={0}, searchCriteria={1}", Arrays.toString( columns ), searchCriteria ) );
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables( ORDERS_TABLE_NAME );
        builder.appendColumns( new StringBuilder(), columns);
        String paymentCodeWhere = OrderColumn.PAYMENT_CODE + " LIKE ? ";
        String paymentCodeParam = ( searchCriteria.getPaymentCode() == null ) ? "%" : searchCriteria.getPaymentCode() + "%";
        String checkStatusWhere = ( searchCriteria.getChecked() == null ) ? null : OrderColumn.CHECK_STATUS + " LIKE ? ";
        String checkStatusParam = ( searchCriteria.getChecked() == null ) ? null : String.valueOf( CheckStatus.convertCheckStatus( searchCriteria.getChecked() ) );
        String paymentDateFromWhere = ( searchCriteria.getFrom() == null ) ? null : OrderColumn.PAYMENT_DATE + " > ? ";
        String paymentDateFromParam = ( searchCriteria.getFrom() == null ) ? null : OrderMapper.formatDatetime( searchCriteria.getFrom() );
        String paymentDateToWhere = ( searchCriteria.getTo() == null ) ? null : OrderColumn.PAYMENT_DATE + " <= ? ";
        String paymentDateToParam = ( searchCriteria.getTo() == null ) ? null : OrderMapper.formatDatetime( searchCriteria.getTo() );
        Cursor cursor = builder.query(
                openHelper.getReadableDatabase(),
                columns,
                concatWhereParamsByAnd(paymentCodeWhere, checkStatusWhere, paymentDateFromWhere, paymentDateToWhere ),
                selectNonNullParams( paymentCodeParam, checkStatusParam, paymentDateFromParam, paymentDateToParam ),
                groupBy,
                null,
                orderBy );

        if (cursor == null) {
            Log.d(TAG, "buildQuery(): no curosr" );
            return null;
        } else if (!cursor.moveToFirst()) {
            Log.d(TAG, "buildQuery(): empty curosr" );
            cursor.close();
            return null;
        }

        Log.d(TAG, "buildQuery(): count=" + cursor.getCount() );

        return cursor;
    }

    private String concatWhereParamsByAnd(String ... params) {
        Log.d( TAG, "concatWhereParamsByAnd(), input: " + Arrays.asList( params ) );
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for( String param: params ) {
            Log.d(TAG, "concatWhereParamsByAnd(), param=" + param + ", first=" + first);
            if( param == null ) {
                continue;
            }

            if( !first ) {
                builder.append(" and ");
            } else {
                first = false;
            }

            builder.append( param );
        }

        if ( builder.length() == 0 ) {
            Log.d( TAG, "concatWhereParamsByAnd(), output: null" );
            return null;
        }


        Log.d( TAG, "concatWhereParamsByAnd(), output: " + builder.toString() );

        return builder.toString();
    }

    private String[] selectNonNullParams(String ... params) {
        Log.d( TAG, "selectNonNullParams(), input: " + Arrays.asList( params ) );
        List<String> paramsRes = new ArrayList<String>(params.length);
        for( String param: params ) {
            if( param != null ) {
                paramsRes.add( param );
            }
        }

        if( paramsRes.isEmpty() ) {
            Log.d( TAG, "selectNonNullParams(), output: null" );
            return null;
        }


        Log.d( TAG, "selectNonNullParams(), output: " + paramsRes );

        String[] p = paramsRes.toArray( new String[0] );

        return p;
    }

    public Cursor getProducts( Long orderId ) {
        final String[] columns = new String[] { ProductColumn.ID, ProductColumn.NAME, ProductColumn.PRICE, ProductColumn.COUNT };

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables( PRODUCTS_TABLE_NAME );
        builder.appendColumns( new StringBuilder(), columns);
        Cursor cursor = builder.query( openHelper.getReadableDatabase(), columns, ProductColumn.ORDER_ID + " MATCH ? ", new String[] { String.valueOf( orderId ) + "%" }, null, null, null );
        Log.d( TAG, builder.toString() );

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        return cursor;
    }

    public void setCheckStatus(Long id, boolean checkStatusB) {
        int checkStatus = ( checkStatusB ) ? CheckStatus.CHECKED : CheckStatus.UNCHECKED;
        ContentValues values = new ContentValues( 1 );
        values.put( OrderColumn.CHECK_STATUS, checkStatus );
        Log.d( TAG, MessageFormat.format( "Update order: id={0}, check_status={1}.", id.toString(), checkStatus ) );
        int count = openHelper.getWritableDatabase().update( ORDERS_TABLE_NAME, values, OrderColumn.ID + " = " + id, null );
        if( count != 1 ) {
            throw new RuntimeException( "No one row has been updated (count=" + count + ")");
        }
    }

    public Long getLastOrderId() {
        final String[] columns = new String[] { OrderColumn.ID };

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables( ORDERS_TABLE_NAME );
        builder.appendColumns( new StringBuilder(), columns);
        Cursor cursor = builder.query( openHelper.getReadableDatabase(), columns, null, null, null, null, OrderColumn.ID + " desc" );

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        Log.d( TAG, " count: " + cursor.getCount() + " names: " + Arrays.toString( cursor.getColumnNames() ) );

        Long count = cursor.getLong( 0 );
        cursor.close();

        return count;
    }

    public Long getNewOrdersCount(OrdersSearchCriteria baseCriteria) {
        OrdersSearchCriteria searchCriteria = cloneCriteria(baseCriteria);
        searchCriteria.setFrom( OrderListAdapter.getExpiredTime().getTime() );

        return getUncheckedOrder(searchCriteria);
    }

    public Long getExpiredOrdersCount(OrdersSearchCriteria baseCriteria) {
        OrdersSearchCriteria searchCriteria = cloneCriteria(baseCriteria);
        searchCriteria.setTo( OrderListAdapter.getExpiredTime().getTime() );

        return getUncheckedOrder(searchCriteria);
    }

    private Long getUncheckedOrder(OrdersSearchCriteria searchCriteria) {
        final String[] columns = new String[] { "count( 1 )" };

        searchCriteria.setChecked( false );

        return getFirstAndClose( buildQuery(columns, searchCriteria, null, null ) );
    }



    private OrdersSearchCriteria cloneCriteria(OrdersSearchCriteria baseCriteria) {
        try {
            return (OrdersSearchCriteria) baseCriteria.clone();
        } catch (CloneNotSupportedException e) {
            // TODO: log it right way
            e.printStackTrace();
            return null;
        }
    }

    private Long getFirstAndClose( Cursor cursor) {
        Long value = cursor.getLong( 0 );
        Log.d(TAG, "getFirstAndClose(), value=" + value);
        cursor.close();

        return value;
    }

    public Long getPayedOrdersCount(OrdersSearchCriteria baseCriteria) {
        final String[] columns = new String[] { "count( 1 )" };

        OrdersSearchCriteria searchCriteria = cloneCriteria( baseCriteria );
        searchCriteria.setChecked( true );

        return getFirstAndClose( buildQuery(columns, searchCriteria, null, null ) );
    }

    /**
     *
     * @param moreOreLess ( ">" or "<=" )
     * @return
     */
}



class OrdersSearchCriteria implements Cloneable {
    private String paymentCode;
    private Date from;          // strictly more then order's date
    private Date to;            // less or equals then order's date
    private Boolean checked;

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        OrdersSearchCriteria searchCriteria = new OrdersSearchCriteria();
        searchCriteria.setPaymentCode( paymentCode );
        searchCriteria.setChecked( checked );
        searchCriteria.setFrom( from );
        searchCriteria.setTo( to );
        return searchCriteria;
    }

    @Override
    public String toString() {
        return "OrdersSearchCriteria{" +
                "paymentCode='" + paymentCode + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", checked=" + checked +
                '}';
    }
}