package officer.cashdesc.volkdem.com.officer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.common.model.Order;
import com.common.model.Product;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Evgeny on 31.05.2016.
 */
class OrdersDatabase {
    private static final String TAG = OrdersDatabase.class.getName();
    private static final String DATABASE_NAME = "ORDERS";
    private static final String ORDERS_TABLE_NAME = "ORDERS";
    private static final String PRODUCTS_TABLE_NAME = "PRODUCTS";

    private final DatabaseOpenHelper openHelper;

    public OrdersDatabase(Context context) {
        openHelper = new DatabaseOpenHelper( context );
    }

    class OrderColumn {
        private static final String ID = "_ID";
        private static final String PAYMENT_CODE = "PAYMENT_CODE";
        private static final String PAYMENT_DATE = "PAYMENT_DATE";
    }

    class ProductColumn {
        private static final String ID = "_ID";
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

        private final Context contex;
        private SQLiteDatabase database;
        private static final int DATABASE_VERSION = 2;

        private static final String ORDERS_TABLE_CREATE = generateCreateVirtualTableStatement(
                ORDERS_TABLE_NAME, OrderColumn.ID, OrderColumn.PAYMENT_CODE, OrderColumn.PAYMENT_DATE );

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
            this.contex = context;
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
            database.execSQL( "DROP TABLE IF EXISTS " + ORDERS_TABLE_NAME );
            database.execSQL( "DROP TABLE IF EXISTS " + PRODUCTS_TABLE_NAME );
            onCreate( database );
        }

        public void addOrders( List< Order > orders) {
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
            // TODO: replace order.getId() to original payment code
            initialValues.put( OrderColumn.PAYMENT_CODE, order.getId() );
            initialValues.put( OrderColumn.PAYMENT_DATE, OrderMapper.formatDatetime( order.getPaymentDate() ) );

            database.beginTransaction();
            try {
                database.insert(ORDERS_TABLE_NAME, null, initialValues);

                for (Map.Entry product : order.getProducts().entrySet()) {
                    addProduct(new Long(order.getId()), (Product) product.getKey(), (Integer) product.getValue());
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
        final String[] columns = new String[] { OrderColumn.ID, OrderColumn.PAYMENT_CODE, OrderColumn.PAYMENT_DATE };

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables( ORDERS_TABLE_NAME );
        builder.appendColumns( new StringBuilder(), columns);
        Cursor cursor = builder.query( openHelper.getReadableDatabase(), columns, OrderColumn.PAYMENT_CODE + " LIKE ?", new String[] { searchCriteria.getPaymentCode() + "%" }, null, null, null );

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

}


class OrdersSearchCriteria {
    private String paymentCode;
    private Date from;
    private Date to;

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }
}