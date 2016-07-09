package com.volkdem.ecashier.officer;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Evgeny on 07.07.2016.
 */
public class OrdersSyncAdapter extends AbstractThreadedSyncAdapter {
    private final static String TAG = OrdersSyncAdapter.class.getName();

    private ContentResolver contentResolver;

    public OrdersSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        contentResolver = context.getContentResolver();
    }

    public OrdersSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        contentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d( TAG, "onPerformSync(): test");
    }
}
