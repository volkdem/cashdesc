package com.volkdem.ecashier.officer.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.volkdem.ecashier.officer.Authenticator;

/**
 * Created by Evgeny on 04.07.2016.
 */
public class AuthenticatorService extends Service {
    private Authenticator authenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        authenticator = new Authenticator( this );
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
