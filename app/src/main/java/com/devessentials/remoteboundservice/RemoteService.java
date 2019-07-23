package com.devessentials.remoteboundservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class RemoteService extends Service {

    // SOS: from its side, the service wraps a Handler inside a Messenger and the msg is received
    // in handleMessage of the Handler.
    private final Messenger myMessenger = new Messenger(new MyHandler(getApplicationContext()));

    public RemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myMessenger.getBinder();
    }

    private static class MyHandler extends Handler {

        final WeakReference<Context> mContextRef;

        MyHandler(Context context) {
            mContextRef = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            Context context = mContextRef.get();
            if (context == null) return;

            Bundle data = msg.getData();
            String dataString = data.getString(MainActivity.KEY_MESSAGE);
            Toast.makeText(context, dataString, Toast.LENGTH_SHORT).show();
        }
    }
}
