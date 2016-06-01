package officer.cashdesc.volkdem.com.officer;

import android.database.Cursor;

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
    private static final SimpleDateFormat FULL_DATETIME_FORMAT = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
    public static Order getOrder(Cursor cursor ) {
        Order order = new Order();
        int columnIndex = 0;
        order.setId( String.valueOf( cursor.getLong( columnIndex++ ) ) );
        // TODO get payment and set payment code
        order.setPaymentDate( parseDatetime( cursor.getString( columnIndex ++ )) );
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
}

