package officer.cashdesc.volkdem.com.officer;

import android.support.annotation.ArrayRes;

import com.common.model.Order;
import com.common.model.Product;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Evgeny on 21.05.2016.
 */
public class OrderFactory {
    private static Random random = new Random( );

    private static String[] names = new String[] {
            "Говядина (кроме бескостного мяса), кг",
            "Свинина (кроме бескостного мяса), кг",
            "Баранина (кроме бескостного мяса), кг",
            "Куры (кроме куриных окорочков), кг",
            "Рыба мороженая неразделанная, кг",
            "Масло сливочное, кг",
            "Масло подсолнечное, кг",
            "Молоко питьевое, л",
            "Яйца куриные, 10 шт.",
            "Сахар-песок, кг",
            "Соль поваренная пищевая, кг",
            "Чай черный байховый, кг",
            "Мука пшеничная, кг",
            "Хлеб ржаной, ржано-пшеничный, кг",
            "Хлеб и булочные изделия из пшеничной муки, кг",
            "Рис шлифованный, кг",
            "Пшено, кг",
            "Крупа гречневая – ядрица, кг",
            "Вермишель, кг",
            "Картофель, кг",
            "Капуста белокочанная свежая, кг",
            "Лук репчатый, кг",
            "Морковь, кг",
            "Яблоки, кг"
    };

    private static BigDecimal[] prices = new BigDecimal[ names.length ];

    public static List< Order > generateOrders( int count, int maxOrderSize ) {
        List< Order > orders = new ArrayList<>( count );
        for( int i = 0; i < count; i++ ) {
            orders.add( generateOrder( random.nextInt( maxOrderSize ) ) );
        }

        return orders;
    }

    public static Order generateOrder( int size ) {

        Order order = new Order();
        // TODO: id replace to paymentCode
        order.setId( String.valueOf( random.nextInt( 100 ) ) );
        Calendar date = Calendar.getInstance();
        date.set( Calendar.DAY_OF_MONTH, random.nextInt( 31 ));
        order.setPaymentDate( date.getTime() );

        for( int i = 0; i < size; i++ ) {
            order.addProduct( getProduct() );
        }

        return order;
    }

    private static Product getProduct() {
        Product product = new Product();
        product.setBarcode( new BigInteger( 32, random  ) );
        Long id = random.nextLong() % names.length + 1;
        product.setProductId( id );
        product.setProductName( names[ getIndex( id )] );
        product.setPrice( getPrice( id ) );
        return product;
    }

    private static int getIndex( Long id ) {
        if( id.equals( 0L ) ) {
            return 1;
        }
        return (int) (names.length % id);
    }


    private static BigDecimal getPrice( Long id ) {
        int index = getIndex( id );
        if ( prices[ index ] != null ) {
            return prices[ index ];
        }

        BigDecimal price = new BigDecimal( random.nextDouble() );
        prices[ index ] = price;

        return price;
    }

}
