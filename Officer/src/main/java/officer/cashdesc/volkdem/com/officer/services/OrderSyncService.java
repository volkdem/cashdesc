package officer.cashdesc.volkdem.com.officer.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import officer.cashdesc.volkdem.com.officer.OrdersSyncAdapter;

/**
 * Created by Evgeny on 07.07.2016.
 */
public class OrderSyncService extends Service {
    private OrdersSyncAdapter ordersSyncAdapter;

    private static final Object ordersSycnAdapterLock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (ordersSycnAdapterLock) {
            if( ordersSyncAdapter == null ) {
                ordersSyncAdapter = new OrdersSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return ordersSyncAdapter.getSyncAdapterBinder();
    }
}
