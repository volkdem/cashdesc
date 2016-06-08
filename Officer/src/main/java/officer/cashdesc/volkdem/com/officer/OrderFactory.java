package officer.cashdesc.volkdem.com.officer;

import com.common.model.Order;
import com.common.model.Product;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by Evgeny on 21.05.2016.
 */
public class OrderFactory {
    private static Random random = new Random( );

    private static Product[] products = new Product[] {
            getProduct("Говядина (кроме бескостного мяса), кг"),
            getProduct("Свинина (кроме бескостного мяса), кг"),
            getProduct("Баранина (кроме бескостного мяса), кг"),
            getProduct("Куры (кроме куриных окорочков), кг"),
            getProduct("Рыба мороженая неразделанная, кг"),
            getProduct("Масло сливочное, кг"),
            getProduct("Масло подсолнечное, кг"),
            getProduct("Молоко питьевое, л"),
            getProduct("Яйца куриные, 10 шт."),
            getProduct("Сахар-песок, кг"),
            getProduct("Соль поваренная пищевая, кг"),
            getProduct("Чай черный байховый, кг"),
            getProduct("Мука пшеничная, кг"),
            getProduct("Хлеб ржаной, ржано-пшеничный, кг"),
            getProduct("Хлеб и булочные изделия из пшеничной муки, кг"),
            getProduct("Рис шлифованный, кг"),
            getProduct("Пшено, кг"),
            getProduct("Крупа гречневая – ядрица, кг"),
            getProduct("Вермишель, кг"),
            getProduct("Картофель, кг"),
            getProduct("Капуста белокочанная свежая, кг"),
            getProduct("Лук репчатый, кг"),
            getProduct("Морковь, кг"),
            getProduct("Яблоки, кг")
    };

    static {
        for(int i = 0; i < products.length; i++ ) {
            Product product = products[i];
            product.setPrice(new BigDecimal(random.nextDouble()));
            product.setBarcode( new BigInteger( 32, random  ) );
            product.setProductId( new Long( i ) );
        }
    }

    private static Product getProduct( String name ) {
        Product product = new Product();
        product.setProductName( name );
        return product;
    }

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
        order.setCheckStatus( ( random.nextInt(1) == 0 ) ? false : true );

        for( int i = 0; i < size + 1; i++ ) {
            order.addProduct( getProduct() );
        }

        return order;
    }

    private static Product getProduct() {
        return products[ random.nextInt( products.length ) ];
    }
}
