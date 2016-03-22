package com.volkdem.cashdesc.ui;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.view.View;

/**
 * Created by Evgeny on 22.03.2016.
 */
public interface IViewFinder {
    View findViewById(@IdRes int id);
}
