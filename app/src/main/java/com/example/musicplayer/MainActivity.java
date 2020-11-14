package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import myService.MediaService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaService.MyBinder myBinder;
    Intent serviceIntent;
    private Button playbutton;
    private Button pauseButton;
    private Button nextButton;
    private Button preciousButton;
    private TextView timeTxt;
    private SeekBar mySeekBar;
    private SimpleDateFormat time=new SimpleDateFormat("m:ss");
    Handler nyHandler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        serviceIntent=new Intent(MainActivity.this,MediaService.class);
        bindService(serviceIntent,myServiceConnection,BIND_AUTO_CREATE);

    }

    private ServiceConnection myServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder=(MediaService.MyBinder) service;
            mySeekBar.setMax(myBinder.getLength());
            mySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser){
                        myBinder.turnToPosition(seekBar.getProgress());
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            Log.e("MediaPlay","onConnect myBinder is null ? "+(myBinder == null));
            nyHandler.post(myRunnable);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private Runnable myRunnable=new Runnable() {
        @Override
        public void run() {
            mySeekBar.setProgress(myBinder.getCurrentPosition());
            timeTxt.setText(time.format(myBinder.getCurrentPosition())+"s");
            nyHandler.postDelayed(myRunnable,1000);

        }
    };

    private void initView(){
        playbutton = findViewById(R.id.playButton);
        pauseButton=findViewById(R.id.pauseButton);
        nextButton=findViewById(R.id.nextButton);
        preciousButton=findViewById(R.id.preciousButton);
        timeTxt=findViewById(R.id.timeTxt);
        mySeekBar=findViewById(R.id.seekBar);
        pauseButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        preciousButton.setOnClickListener(this);
        playbutton.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myBinder.closeMedia();
        nyHandler.removeCallbacks(myRunnable);
        unbindService(myServiceConnection);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playButton: {

                myBinder.playMusic();
            }
            break;
            case R.id.pauseButton:{

                myBinder.pauseMusic();
            }
            break;
            case R.id.nextButton:{
                myBinder.nextMusic();
            }
            break;
            case R.id.preciousButton:{
                myBinder.preciousMusic();
            }
            break;
        }
    }
}