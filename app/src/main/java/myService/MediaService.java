package myService;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import Model.Music;
import Model.MusicInfo;

public class MediaService extends Service {
    private MediaPlayer mediaPlayer=new MediaPlayer();
    private MusicInfo musicInfo=new MusicInfo();
    private ArrayList<Music> musicList;
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
        initMediaPlayerFile(musicList.get(i).getMusicId());
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
        musicList=musicInfo.getMusicInfo();
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

        public String getTitle(){
            return musicList.get(i).getTitle();
        }

        public String getAuthor(){
            return  musicList.get(i).getAuthor();
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
            if(mediaPlayer!=null&&i>=0&&i<musicList.size()){
                mediaPlayer.reset();
                if(i==3){
                    i=0;
                    initMediaPlayerFile(musicList.get(i).getMusicId());
                }else {
                    i++;
                    initMediaPlayerFile(musicList.get(i).getMusicId());
                }
            }
            playMusic();
        }

        public void preciousMusic(){
            if(mediaPlayer!=null&&i>=0&&i<4){
                mediaPlayer.reset();
                if(i==0){
                    i=musicList.size()-1;
                }else{
                    i--;
                }
                initMediaPlayerFile(musicList.get(i).getMusicId());
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
