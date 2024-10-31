package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static SeekBar sb;
    private static TextView tv_progess,tv_total;
    private ObjectAnimator animator;
    private MusicService.MusicControl musicControl;
    MyServiceConn conn;
    Intent intent;
    private boolean isUnbind=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void init(){
        findViewById(R.id.btn_play).setOnClickListener(this);
        findViewById(R.id.btn_pause).setOnClickListener(this);
        findViewById(R.id.btn_continue_play).setOnClickListener(this);
        findViewById(R.id.btn_exit).setOnClickListener(this);
        tv_progess=findViewById(R.id.tv_progress);
        tv_total=findViewById(R.id.tv_total);
        sb=findViewById(R.id.sb);
        intent=new Intent(this,MusicService.class);
        conn=new MyServiceConn();
        bindService(intent,conn,BIND_AUTO_CREATE);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress==seekBar.getMax()){
                    animator.pause();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress=seekBar.getProgress();
                musicControl.seekTo(progress);
            }
        });
        ImageView iv_music=findViewById(R.id.iv_music);
        animator=ObjectAnimator.ofFloat(iv_music,"rotation",0f,360.0f);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);



    }

    public static Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            Bundle bundle=msg.getData();
            int duration=bundle.getInt("duration");
            int currentPostion=bundle.getInt("currentPosition");
            sb.setMax(duration);
            sb.setProgress(currentPostion);
            int minute=duration/1000/60;
            int second=duration/1000%60;
            String strMinute=null;
            String strSecond=null;
            if(minute<10){
                strMinute="0"+minute;
            }else {
                strMinute=minute+"";
            }if(second<10){
                strSecond="0"+second;
            }else {
                strSecond=second+"";
            }
            tv_total.setText(strMinute+":"+strSecond);
            minute=currentPostion/1000/60;
            second=currentPostion/1000%60;
            if(minute<10){
                strMinute="0"+minute;
            }else {
                strMinute=minute+"";
            }if(second<10){
                strSecond="0"+second;
            }else{
                strSecond=second+"";
            }
            tv_progess.setText(strMinute+":"+strSecond);
        }
    };

    private void unbind(Boolean isUnbind){
        if(!isUnbind){
            musicControl.pausePlay();
            unbindService(conn);
            stopService(intent);
        }
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_play:
            musicControl.play();
            animator.start();
            break;
            case R.id.btn_pause:
            musicControl.pausePlay();
            animator.pause();
            break;
            case R.id.btn_continue_play:
                musicControl.continuePlay();
                animator.start();
                break;
            case R.id.btn_exit:
                unbind(isUnbind);
                isUnbind=true;
                finish();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind(isUnbind);
    }
}