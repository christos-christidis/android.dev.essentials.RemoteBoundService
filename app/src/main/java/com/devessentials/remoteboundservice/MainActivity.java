package com.devessentials.remoteboundservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    static final String KEY_MESSAGE = "MyString";

    private Messenger myService;
    private boolean mIsBound;

    // SOS: I wrap the binder I get from the service inside a Messenger, which can be used to send
    // messages to other processes.
    private final ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = new Messenger(service);
            mIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService = null;
            mIsBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(getApplicationContext(), RemoteService.class);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
    }

    public void sendMessage(View view) {
        if (!mIsBound) return;

        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MESSAGE, "Message Received");
        msg.setData(bundle);

        try {
            myService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
