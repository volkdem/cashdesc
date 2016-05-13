package com.volkdem.cashdesc.buyer.utils;

import com.volkdem.cashdesc.buyer.model.OrderWrapper;

/**
 * Created by Evgeny on 16.03.2016.
 */

// TODO replace to transfering between activities instead of static class
public class StaticContainer {
    private static volatile OrderWrapper order;

    public static void setOrder( OrderWrapper order ) {
        StaticContainer.order = order;
    }

    public static OrderWrapper getOrder() {
        return order;
    }
}
