package com.volkdem.cashdesc.utils;

import com.common.model.Order;

/**
 * Created by Evgeny on 16.03.2016.
 */

// TODO replace to transfering between activities instead of static class
public class StaticContainer {
    private static volatile Order order;

    public static void setOrder( Order order ) {
        StaticContainer.order = order;
    }

    public static Order getOrder() {
        return order;
    }
}
