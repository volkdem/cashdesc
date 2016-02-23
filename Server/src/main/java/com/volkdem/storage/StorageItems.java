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



        private static Map<String, Store> storageStore = new HashMap<>();
        private static Map<String, Product> storageProduct = new HashMap<>();

        private static Store magnit = new Store();
        private static Store ashan = new Store();
        private static Store peterochka = new Store();
        private static Product milk = new Product();
        private static Product bread = new Product();
        private static Product eggs = new Product();


        static {

                milk.setBarcode(new BigInteger("54321"));
                milk.setPrice(new BigDecimal(67.0));
                milk.setProduct_id(1L);

                bread.setBarcode(new BigInteger("654321"));
                bread.setPrice(new BigDecimal(30.5));
                bread.setProduct_id(2L);

                eggs.setBarcode(new BigInteger("7654321"));
                eggs.setPrice(new BigDecimal(56.70));
                eggs.setProduct_id(3L);

                storageStore.put("12345", ashan);
                storageStore.put("123456", peterochka);
                storageStore.put("1234567", magnit);

                storageProduct.put("54321", milk);
                storageProduct.put("654321", bread);
                storageProduct.put("7654321", eggs);

                magnit.setAddress("ш. Энтузиастов, 66");
                magnit.setBarсode(new BigInteger("12345"));
                magnit.setName("Магнит");
                magnit.setStore_ID(1L);
                magnit.setProduct_ID();

                ashan.setAddress("Люблинская ул., 153");
                ashan.setBarсode(new BigInteger("123456"));
                ashan.setName("Ашан");
                ashan.setStore_ID(2L);

                peterochka.setAddress("ул. Дзержинского, 2");
                peterochka.setBarсode(new BigInteger("1234567"));
                peterochka.setName("Peterochka");
                peterochka.setStore_ID(3L);

        }


        public static Map<String, Store> getStorageStore() {
                return storageStore;
        }

        public static void setStorageStore(Map<String, Store> storageStore) {
                StorageItems.storageStore = storageStore;
        }

        public static Map<String, Product> getStorageProduct() {
                return storageProduct;
        }

        public static void setStorageProduct(Map<String, Product> storageProduct) {
                StorageItems.storageProduct = storageProduct;
        }
}
