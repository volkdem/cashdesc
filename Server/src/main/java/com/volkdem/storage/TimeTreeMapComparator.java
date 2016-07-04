package com.volkdem.storage;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Vadim on 30.06.2016.
 */
public class TimeTreeMapComparator implements Comparator<Timestamp>, Serializable {


    @Override
    public int compare(Timestamp timeStamp1, Timestamp timeStamp2) {
        return timeStamp1.before(timeStamp1) ? 0 : -1;
    }
}
