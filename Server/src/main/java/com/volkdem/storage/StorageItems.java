package com.volkdem.storage;

import com.common.model.Product;
import com.common.model.Store;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vadim on 22.02.2016.
 */
public class StorageItems {



        private static Map<Long, Store> storageStore = new HashMap<Long, Store>();
        private static Map<String, Product> storageProduct = new HashMap<String, Product>();

        private static Store magnit = new Store();
        private static Store ashan = new Store();
        private static Store peterochka = new Store();
        private static Store testStore = new Store();
        private static Product milk = new Product();
        private static Product bread = new Product();
        private static Product eggs = new Product();
        private static Product napkin = new Product();


        static {

                milk.setBarcode(new BigInteger("54321"));
                milk.setProductName("Milk");
                milk.setPrice(new BigDecimal(67.0));
                milk.setProductId(1L);

                bread.setBarcode(new BigInteger("654321"));
                bread.setProductName("Bread");
                bread.setPrice(new BigDecimal(30.5));
                bread.setProductId(2L);

                eggs.setBarcode(new BigInteger("7654321"));
                eggs.setProductName("Eggs");
                eggs.setPrice(new BigDecimal(56.70));
                eggs.setProductId(3L);

                eggs.setBarcode(new BigInteger("7654321"));
                eggs.setProductName("Eggs");
                eggs.setPrice(new BigDecimal(56.70));
                eggs.setProductId(3L);

                napkin.setBarcode(new BigInteger("7322540098075"));
                napkin.setProductName("Zewa");
                napkin.setPrice(new BigDecimal(21.3));
                napkin.setProductId(4L);


                storageProduct.put("54321", milk);
                storageProduct.put("654321", bread);
                storageProduct.put("7654321", eggs);
                storageProduct.put("7322540098075", napkin);

                magnit.setAddress("sh. Entuziastov, 66");
                magnit.setBarcode(new BigInteger("1234567"));
                magnit.setName("Magnit");
                magnit.setStoreId(1L);
                magnit.setProductIdList(new ArrayList<Product>() {{add(milk);}});

                ashan.setAddress("Lublinskaya st., 153");
                ashan.setBarcode(new BigInteger("12345"));
                ashan.setName("Ashan");
                ashan.setStoreId(2L);
                ashan.setProductIdList(new ArrayList<Product>() {{add(bread);}});

                peterochka.setAddress("Dzerzhinskogo st., 2");
                peterochka.setBarcode(new BigInteger("123456"));
                peterochka.setName("Peterochka");
                peterochka.setStoreId(3L);
                peterochka.setProductIdList(new ArrayList<Product>() {{add(eggs);}});


                testStore.setAddress("1i Volokolamskiy proezd.");
                testStore.setBarcode(new BigInteger("7322540098075"));
                testStore.setName("Test Client Store");
                testStore.setStoreId(4L);
                testStore.setProductIdList(new ArrayList<Product>() {{
                        add(napkin);
                        add(milk);
                        add(bread);
                }});


                storageStore.put(2L, ashan);
                storageStore.put(3L, peterochka);
                storageStore.put(1L, magnit);
                storageStore.put(4L, testStore);

        }


        public static Map<Long, Store> getStorageStore() {
                return storageStore;
        }

        public static void setStorageStore(Map<Long, Store> storageStore) {
                StorageItems.storageStore = storageStore;
        }

        public static Map<String, Product> getStorageProduct() {
                return storageProduct;
        }

        public static void setStorageProduct(Map<String, Product> storageProduct) {
                StorageItems.storageProduct = storageProduct;
        }



}
