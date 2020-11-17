package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import Adapter.ImageAdapter;
import Model.Music;
import Model.MusicInfo;
import myService.MediaService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaService.MyBinder myBinder;
    private RecyclerView coverView;
    Intent serviceIntent;
    private Toolbar toolBar;
    private Button playbutton,nextButton,preciousButton;
    MusicInfo info=new MusicInfo();
    ArrayList<Music> musicList=info.getMusicInfo();
    private TextView timeStart,timeEnd;
    private SeekBar mySeekBar;
    private LinearLayoutManager manager;
    private SimpleDateFormat time=new SimpleDateFormat("m:ss");
    Handler nyHandler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceIntent=new Intent(MainActivity.this,MediaService.class);
        bindService(serviceIntent,myServiceConnection,BIND_AUTO_CREATE);
        initView();
        manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        coverView.setLayoutManager(manager);
        ImageAdapter adapter=new ImageAdapter(musicList);
        coverView.setAdapter(adapter);

//        Intent intent=getIntent();
////        if(intent.getStringExtra("send").equals("play")){
////            myBinder.playMusic();
////
////        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.shareChoice:{
                Toast.makeText(this,"你已分享",Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.detail:{
                Toast.makeText(this, "design by xiaoxu", Toast.LENGTH_SHORT).show();
            }
            case android.R.id.home:{
                finish();
            }
            break;
            default:
        }
        return true;
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
            timeStart.setText(time.format(myBinder.getCurrentPosition())+"s");
            timeEnd.setText(time.format(myBinder.getLength())+"s");
            nyHandler.postDelayed(myRunnable,1000);

        }
    };

    private void initView(){
        playbutton = findViewById(R.id.playButton);
        nextButton=findViewById(R.id.nextButton);
        preciousButton=findViewById(R.id.preciousButton);
        timeStart=findViewById(R.id.timeTxt);
        timeEnd=findViewById(R.id.timeEnd);
        mySeekBar=findViewById(R.id.seekBar);
        toolBar = findViewById(R.id.toolBar);
        nextButton.setOnClickListener(this);
        preciousButton.setOnClickListener(this);
        playbutton.setOnClickListener(this);
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolBar.setNavigationIcon(R.drawable.ic_home_back);
        getSupportActionBar().setTitle(musicList.get(0).getTitle());
        getSupportActionBar().setSubtitle(musicList.get(0).getAuthor());
        coverView = findViewById(R.id.coverView);
        PagerSnapHelper helper=new PagerSnapHelper();
        helper.attachToRecyclerView(coverView);
        coverView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("newState", String.valueOf(newState));
            }

            //停止滚动
            //public static final int SCROLL_STATE_IDLE = 0;
            ////正在被外部拖拽,一般为用户正在用手指滚动
            //public static final int SCROLL_STATE_DRAGGING = 1;
            ////自动滚动开始
            //public static final int SCROLL_STATE_SETTLING = 2;
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int x=0;
                int firstPosition=manager.findFirstVisibleItemPosition();
                Log.e("firstPosition",String.valueOf(firstPosition));
                DisplayMetrics dm=new DisplayMetrics();
                int width=(int)(dm.widthPixels/dm.density);
                Log.e("width",String.valueOf(width));
               if(dx<0){
                   dx=Math.abs(dx);
                   x=x+dx;
                   if(dx>(width/2)){

                   }
               }
                Log.e("dx",String.valueOf(dx));
            }
        });
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
                if(!playbutton.isActivated()){
                    myBinder.playMusic();
                }else{
                    myBinder.pauseMusic();
                }
                playbutton.setActivated(!playbutton.isActivated());


            }
            break;

            case R.id.nextButton:{
                myBinder.nextMusic();
                getSupportActionBar().setTitle(myBinder.getTitle());
                getSupportActionBar().setSubtitle(myBinder.getAuthor());
                playbutton.setActivated(!playbutton.isActivated());

            }
            break;

            case R.id.preciousButton:{
                myBinder.preciousMusic();
                getSupportActionBar().setTitle(myBinder.getTitle());
                getSupportActionBar().setSubtitle(myBinder.getAuthor());
                playbutton.setActivated(!playbutton.isActivated());
            }
            break;

        }
    }
}