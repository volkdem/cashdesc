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

import java.lang.reflect.Array;
import java.text.MessageFormat;
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

    class CheckStatus {
        public static final int UNCHECKED = 0;
        public static final int CHECKED = 1;
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

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables( ORDERS_TABLE_NAME );
        builder.appendColumns( new StringBuilder(), columns);
        String paymentCode = ( searchCriteria.getPaymentCode() == null ) ? "" : searchCriteria.getPaymentCode();
        /*Cursor cursor = builder.query( openHelper.getReadableDatabase(), columns,
                OrderColumn.PAYMENT_CODE + " LIKE ? "
                + " and " + OrderColumn.CHECK_STATUS + " = " + CheckStatus.CHECKED,
                new String[] { paymentCode + "%" }, null, null, null );*/
        Cursor cursor = builder.query( openHelper.getReadableDatabase(), columns,
                OrderColumn.PAYMENT_CODE + " LIKE ? ",
                new String[] { paymentCode + "%" }, null, null, null );

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
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

        return cursor.getLong( 0 );
    }

}


class OrdersSearchCriteria {
    private String paymentCode;
    private Date from;
    private Date to;
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
}