package com.volkdem.storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vadim on 19.06.2016.
 */
public class OrderIdPool implements IPayPool{


    private static List<Long> occupitedNumbers;


    static
    {
        occupitedNumbers = new ArrayList<Long>();
    }


    @Override
    public Integer getCodeFromPool() {
        Integer orderID = (occupitedNumbers.size() + 1);
        occupitedNumbers.add(Long.valueOf(orderID));
        return orderID;
    }



    @Override
    public boolean resetPool() {
        return false;
    }


}
