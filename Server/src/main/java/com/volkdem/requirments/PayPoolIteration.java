package com.volkdem.requirments;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vadim on 19.06.2016.
 */
public class PayPoolIteration implements IPayPool {



    private static List<Integer> payCodeNumsAvalible;
    private static Object lock = new Object();

    /*
      This variable track on bigest number in pool in case if pool is
      empty and we need to generate new bulk of files
    */
    private static int current_pool_size = -1;
    private static final int DEFAULT_NUM_AMOUNT_TO_INCREMENT_INPOOL = 20;


    static
    {
        payCodeNumsAvalible = new ArrayList<Integer>();

    }


    @Override
    public Integer getCodeFromPool() {

        synchronized (lock) {

            Integer tempVal;

            if (payCodeNumsAvalible.size() <= 0) {
                System.out.println("Нет свободных номеров, генерируем новый пул свободный номеров");
                if (resetPool()) {
                    System.out.println("Пул сгенерирован " + payCodeNumsAvalible.size());
                    return getCodeFromPool();
                }
            } else if (payCodeNumsAvalible.size() > 0) {
                tempVal = payCodeNumsAvalible.get(0);
                payCodeNumsAvalible.remove(0);
                return tempVal;
            } else {
                try {
                    throw new Exception("Exception при инициализации пула номеров. Не возможно создать новые номера или их колличество не соответствует значению по умолчанию");
                } catch (Exception e) { e.printStackTrace(); }
            }

        }
        return null;
    }



    @Override
    public boolean resetPool() {
        if(initPool().size() == DEFAULT_NUM_AMOUNT_TO_INCREMENT_INPOOL)
                return true;

            return false;
    }



    private static List<Integer> initPool() {
        for(int i = 1; i<= DEFAULT_NUM_AMOUNT_TO_INCREMENT_INPOOL; i++)
            payCodeNumsAvalible.add(i);
        System.out.println("InitPool вызван, теперь размер нового пула номеров равен " + payCodeNumsAvalible.size());
        return payCodeNumsAvalible;
    }









}
