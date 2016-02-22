package com.volkdem.storage;

import com.common.model.Product;
import com.common.model.Store;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vadim on 22.02.2016.
 */
public class StorageItems {



        public static Map<String, Store> storageStore = new HashMap<>();
        public static Map<String, Product> storageProduct = new HashMap<>();

        private static Store magnit = new Store();
        private static Store ashan = new Store();
        private static Store peterochka = new Store();
        private static Product milk = new Product();
        private static Product bread = new Product();
        private static Product eggs = new Product();


        static {

                magnit.setAddress("Address Magnit");
                magnit.setBarсode(new BigInteger("12345"));
                magnit.setName("Magnit");
                magnit.setStore_ID(1L);

                ashan.setAddress("Address Ashan");
                ashan.setBarсode(new BigInteger("123456"));
                ashan.setName("Ashan");
                ashan.setStore_ID(2L);

                peterochka.setAddress("Address Peterochka");
                peterochka.setBarсode(new BigInteger("1234567"));
                peterochka.setName("Peterochka");
                peterochka.setStore_ID(3L);


                milk.setBarcode(new BigInteger("1"));
                milk.setPrice(new BigDecimal(12.5));
                milk.setProduct_id(1L);
                milk.setStore_id(1L);

                bread.setBarcode(new BigInteger("2"));
                bread.setPrice(new BigDecimal(10.5));
                bread.setProduct_id(2L);
                bread.setStore_id(2L);

                eggs.setBarcode(new BigInteger("3"));
                eggs.setPrice(new BigDecimal(9.50));
                eggs.setProduct_id(3L);
                eggs.setStore_id(3L);

                storageStore.put("12345", ashan);
                storageStore.put("123456", peterochka);
                storageStore.put("1234567", magnit);

                storageProduct.put("1", milk);
                storageProduct.put("2", bread);
                storageProduct.put("3", eggs);

        }


}
