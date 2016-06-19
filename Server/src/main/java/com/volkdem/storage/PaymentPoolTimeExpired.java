package com.volkdem.storage;



import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


/**
 * Created by Vadim on 13.06.2016.
 */
public class PaymentPoolTimeExpired implements IPayPool {


    private static final Logger log = Logger.getLogger(PaymentPoolTimeExpired.class.getSimpleName());


    private static List<Integer> payCodeNumsAvalible;
    private static Map<Integer, Long> payCodeNumsOccupited;
    private static Object lock = new Object();
    private static int poolSize;

    /*
      This variable track on bigest number in pool in case if pool is
      empty and we need to generate new bulk of files
    */
    private static int current_pool_size = -1;
    private static final int DEFAULT_NUM_START_INPOOL= 1;
    private static final int DEFAULT_NUM_AMOUNT_TO_INCREMENT_INPOOL = 50;


    private static final int TIMEOUT_FOR_OCCUPITED_NUMBERS = 180000;



    static
    {
        payCodeNumsAvalible = new ArrayList<Integer>();
        payCodeNumsOccupited = new ConcurrentHashMap<Integer, Long>();
        poolSize  = initPool().size();

        Timer timer = new Timer();
        SetAvailableNumsBack availableNumsBack = new SetAvailableNumsBack();
        timer.schedule(availableNumsBack, 0, 30000);
    }




    @Override
    public Integer getCodeFromPool() {

        synchronized (lock) {

            log.info("Getting payCode from pool...");

            if(payCodeNumsAvalible.size() > 0 && payCodeNumsOccupited != null) {

                for(Integer payCode : payCodeNumsAvalible) {
                    Integer tmpPayCode = payCode;
                    payCodeNumsOccupited.put(tmpPayCode, System.currentTimeMillis());
                    payCodeNumsAvalible.remove(payCode);
                    log.info("PayCode: " + tmpPayCode + " is taken, avaliable numbers in pools is: " + payCodeNumsAvalible.size());
                    return tmpPayCode;
                }

            } else {

                while (payCodeNumsAvalible.size() <= 0) {
                    log.info("Pool size less then 0 waiting for numbers in the pool become available");
                    try { TimeUnit.SECONDS.sleep(5);} catch (InterruptedException e) { e.printStackTrace(); }
                }
                getCodeFromPool();

            }
        }
            return -1;
    }



    @Override
    public boolean resetPool() {
        synchronized (lock) {
            log.info("Resetting pool with numbers..." );
            if(initPool() != null && initPool().size() > 0)
                return true;
            else
                return false;
        }
    }




    private static List<Integer> initPool() {

        log.info("Init payCode pool with new numbers...");

        if(current_pool_size == -1)
            current_pool_size = DEFAULT_NUM_START_INPOOL;

        log.info("Current pool size is: " + current_pool_size);

        for(int i = current_pool_size; i<= DEFAULT_NUM_AMOUNT_TO_INCREMENT_INPOOL; i++)
            payCodeNumsAvalible.add(i);

        current_pool_size = payCodeNumsAvalible.size();

        log.info("Pool size is: " + current_pool_size);

        return payCodeNumsAvalible;
    }




    //This class will schedule task to get back occupied numbers
    public static class SetAvailableNumsBack extends TimerTask {

        private static final Logger logTask = Logger.getLogger(PaymentPoolTimeExpired.class.getSimpleName());

        @Override
        public void run() {

            logTask.info("Running task to check occupied numbers expired by timeout...");

            for(Map.Entry<Integer, Long> entry : payCodeNumsOccupited.entrySet()) {

                Long currentTime = System.currentTimeMillis();
                Long timeRemoved = entry.getValue();

                    if((currentTime - timeRemoved) > TIMEOUT_FOR_OCCUPITED_NUMBERS) {
                        logTask.info("Number : " + entry.getKey() + " is expired by timeout and will returned back for usage to pool!");
                        payCodeNumsAvalible.add(entry.getKey());
                        payCodeNumsOccupited.remove(entry.getKey(), entry.getValue());
                    }
            }
        } //End run()
    } // End class SetAvailableNumsBack



}
