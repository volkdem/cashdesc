package com.volkdem.cashdesc.buyer.ui;

import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.volkdem.cashdesc.R;

/**
 * Created by Evgeny on 22.03.2016.
 */
public class SreenLocker {
    private IViewFinder viewFinder;
    private IUnlockListener unlockListener;

    public SreenLocker(IViewFinder viewFinder) {
        this.viewFinder = viewFinder;
    }

    public void lockScreen() {
        Button cancelButton = (Button) findViewById( R.id.cancel_button );
        cancelButton.setVisibility( View.VISIBLE );
        ProgressBar loadingPanelBar = (ProgressBar) findViewById( R.id.loading_panel );
        loadingPanelBar.setVisibility(View.VISIBLE );
    }

    public void unlockScreen() {
        Button cancelButton = (Button) findViewById( R.id.cancel_button );
        cancelButton.setVisibility( View.INVISIBLE );
        ProgressBar loadingPanelBar = (ProgressBar) findViewById( R.id.loading_panel );
        loadingPanelBar.setVisibility(View.INVISIBLE );
    }

    public View findViewById(@IdRes int id) {
         return viewFinder.findViewById( id );
    }

    public void setUnlockListener(final IUnlockListener unlockListener) {
        this.unlockListener = unlockListener;
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unlockListener.onUnlock();
                unlockScreen();
            }
        });
    }
}
