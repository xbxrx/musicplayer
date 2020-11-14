package myService;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import com.example.musicplayer.R;
import java.io.IOException;
import java.util.ArrayList;

public class MediaService extends Service {
    private MediaPlayer mediaPlayer=new MediaPlayer();
    private ArrayList<Integer> musicList=new ArrayList<>();
    private int i=0;
    private MyBinder myBinder=new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("MediaPlay","onCreate");
        initMusicList();
        initMediaPlayerFile(musicList.get(i));
    }

    private void initMediaPlayerFile(int index) {
       AssetFileDescriptor file=getResources().openRawResourceFd(index);
        try {
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMusicList(){
        musicList.add(R.raw.actor);
        musicList.add(R.raw.fallintosea);
        musicList.add(R.raw.rise);
        musicList.add(R.raw.music);
    }

    public class MyBinder extends Binder{
        public int getLength(){
            return mediaPlayer.getDuration();
        }

        public int getCurrentPosition(){
            return mediaPlayer.getCurrentPosition();
        }

        public void turnToPosition(int minute){
            mediaPlayer.seekTo(minute);
        }

        public void playMusic(){
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }
        }

        public void pauseMusic(){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
        }

        public void nextMusic(){
            if(mediaPlayer!=null&&i>=0&&i<4){
                mediaPlayer.reset();
                if(i==3){
                    i=0;
                    initMediaPlayerFile(musicList.get(i));
                }else {
                    i++;
                    initMediaPlayerFile(musicList.get(i));
                }
            }
            playMusic();
        }

        public void preciousMusic(){
            if(mediaPlayer!=null&&i>=0&&i<4){
                mediaPlayer.reset();
                if(i==0){
                    i=3;
                }else{
                    i--;
                }
                initMediaPlayerFile(musicList.get(i));
            }
            playMusic();
        }

        public void closeMedia(){
            if(mediaPlayer!=null){
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
    }
}
