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


        private static Store testStore = new Store();

        private static Product napkin = new Product();
        private static Product chocolate = new Product();
        private static Product soap = new Product();


        static {


                napkin.setBarcode(new BigInteger("7322540098075"));
                napkin.setProductName("Zewa Deluxe Camomile Comfort");
                napkin.setPrice(new BigDecimal(21.3));
                napkin.setProductId(1L);

                chocolate.setBarcode(new BigInteger("4600300075409"));
                chocolate.setProductName("Аленка. Молочный шоколад(Крaсный Октябрь)");
                chocolate.setPrice(new BigDecimal(55.0));
                chocolate.setProductId(2L);

                soap.setBarcode(new BigInteger("4015600524258"));
                soap.setProductName("Safeguard. Пенка для детей.");
                soap.setPrice(new BigDecimal(97.5));
                soap.setProductId(3L);



                storageProduct.put("7322540098075", napkin);
                storageProduct.put("4600300075409", chocolate);
                storageProduct.put("4015600524258", soap);



                testStore.setAddress("1й волоколамский проезд д.10");
                testStore.setBarcode(new BigInteger("7322540098075"));
                testStore.setName("Тестовый магазин");
                testStore.setStoreId(1L);
                testStore.setProductIdList(new ArrayList<Product>() {{
                        add(napkin);
                        add(chocolate);
                        add(soap);
                }});


                storageStore.put(1L, testStore);

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
