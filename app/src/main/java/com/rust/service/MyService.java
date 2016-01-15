package com.rust.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rust.aidl.IMyService;

public class MyService extends Service {

    public class MyServiceImpl extends IMyService.Stub {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                               double aDouble, String aString) throws RemoteException {

        }

        public String helloAndroidAIDL(String name) throws RemoteException {
            Log.d("aidl", "helloAndroidAIDL heard from : " + name);
            return "Rust: Service01 return value successfully!";
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyServiceImpl();
    }
}
