package com.volkdem.storage;

import com.common.model.Order;
import com.common.model.Store;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Vadim on 29.06.2016.
 */
public class PaymentsSaver {


    private Order order;
    private static final String DEF_PATH_SAVE_ORDER_SER = "./orders";


    public PaymentsSaver() { super(); }

    public PaymentsSaver(Order order) { this.order = order; }



    public void saveOrderToMemory() {
        StorageItems.setPaidOrder(this.order);
        for (Map.Entry<Timestamp, Order> entry : StorageItems.getPaidOrders().entrySet()) {
            System.out.print("Key: " + entry.getKey().toString() + " - ");
            System.out.println("Value: " + entry.getValue().toString());
        }

    }





    private static boolean saveAllOrdersForHistory() {

        try {

            File saveOrdersPath = new File(DEF_PATH_SAVE_ORDER_SER);
            if(!saveOrdersPath.exists()) {
                saveOrdersPath.mkdir();
            }

            FileOutputStream fileOut = new FileOutputStream(DEF_PATH_SAVE_ORDER_SER + "/order_all.ser");
            ObjectOutputStream in = new ObjectOutputStream (fileOut);
            in.writeObject(StorageItems.getPaidOrders());
            in.close();
            fileOut.close();
        }
        catch (IOException e) { e.printStackTrace(); return false; }

        return true;
    }



    private static boolean getOrderHistory() {

        TreeMap<Timestamp, Order> orders = new TreeMap<Timestamp, Order>();
        File folderWithOrders = new File(DEF_PATH_SAVE_ORDER_SER);

        if(!folderWithOrders.exists()) {
            return false;
        } else {

            File[] fileOrders = folderWithOrders.listFiles();

            for (File file : fileOrders) {
                try {
                    FileInputStream fileIn = new FileInputStream(file);
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    orders = (TreeMap<Timestamp, Order>) in.readObject();
                    in.close();
                    fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            StorageItems.setPaidOrderMap(orders);
            return true;
        }
    }




    public static void saveOrdersWhileShutDown() {

        System.out.println("Выполняем saveOrdersWhileShutDown() hook");
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.out.println("Выполняем ShutDown hook saveAllOrdersForHistory()");
                saveAllOrdersForHistory();
            }
        });

        System.out.println("ShutDown hook активирован, вызываем getOrderHistory()");
        if(getOrderHistory()) {
            System.out.println("Найден и загружен список заказов сохраненых на локальном диске. Размер равен: " + StorageItems.getPaidOrders().size());
        } else {
            System.out.println("Не найден список заказов сохраненный на локальном диске. Будет создан новый.");
        }
    }


}
