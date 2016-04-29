package com.volkdem.cashdesc.utils;

import android.content.res.Resources;

import com.common.model.Store;
import com.volkdem.cashdesc.R;

/**
 * Created by Evgeny on 29.04.2016.
 */
public class CashDescUtil {
    public static String getStoreInfo(Resources res, Store store ) {
        return res.getString( R.string.store_info, new Object[] { store.getName(), store.getAddress() } );

    }
}
