package com.example.myapplication;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class MyServiceConn implements ServiceConnection {
    private MusicService.MusicControl musicControl;
    @Override
    public void onServiceConnected(ComponentName name, IBinder service){
        musicControl =(MusicService.MusicControl) service;
    }
    @Override
    public void onServiceDisconnected(ComponentName name){

    }
}
