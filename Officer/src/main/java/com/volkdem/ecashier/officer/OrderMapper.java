package com.volkdem.ecashier.officer;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.common.model.Order;
import com.common.model.Product;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Evgeny on 31.05.2016.
 */
public class OrderMapper {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss.SSS";
    private static final SimpleDateFormat FULL_DATETIME_FORMAT = new SimpleDateFormat( DATE_FORMAT + " " + TIME_FORMAT );
    private static final SimpleDateFormat JUST_DATE_FORMAT = new SimpleDateFormat( DATE_FORMAT );
    public static Order getOrder(Cursor cursor ) {
        Order order = new Order();
        int columnIndex = 0;
        order.setId( cursor.getLong( columnIndex++ ) );
        order.setPaymentCode( cursor.getInt( columnIndex++ ) );
        order.setPaymentDate( parseDatetime( cursor.getString( columnIndex ++ )) );
        order.setCheckStatus( ( cursor.getInt( columnIndex++ ) == OrdersDatabase.CheckStatus.CHECKED ) ? true : false);
        return order;
    }

    public static Map<Product, Integer> getProducts(Cursor cursor ) {
        Map<Product, Integer> products = new HashMap<>();
        do {
            Product product = new Product();
            int columnIndex = 0;
            product.setProductId(cursor.getLong(columnIndex++));
            product.setProductName(cursor.getString(columnIndex++));
            product.setPrice(new BigDecimal(cursor.getDouble(columnIndex++)));
            int count = cursor.getInt(columnIndex++);

            products.put(product, count);

        } while (cursor.moveToNext());

        cursor.close();

        return products;
    }

    public static String formatDatetime( Date date) {
        return FULL_DATETIME_FORMAT.format( date );
    }

    public static Date parseDatetime( String date) {
        try {
            return FULL_DATETIME_FORMAT.parse( date );
        } catch (ParseException e) {
            // TODO: write error to DB to send it for analyze to server
            e.printStackTrace();
        }
        return null;
    }


    public static String formatJustDate(Date date) {
        return JUST_DATE_FORMAT.format( date );
    }

    public static Date parseJustDate(String date) {
        try {
            return JUST_DATE_FORMAT.parse( date );
        } catch (ParseException e) {
            // TODO: write error to DB to send it for analyze to server
            e.printStackTrace();
        }
        return null;
    }
}

