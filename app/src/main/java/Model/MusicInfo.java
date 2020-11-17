package Model;

import com.example.musicplayer.R;

import java.util.ArrayList;

public class MusicInfo {
    private ArrayList<Music> musicInfo=new ArrayList<>();
    public MusicInfo (){
        Music music1=new Music(1,"演员","薛之谦", R.mipmap.ic_launcher,R.raw.actor);
        Music music2=new Music(2,"入海","毛不易",R.mipmap.ic_launcher,R.raw.fallintosea);
        Music music3=new Music(3,"永远同在","Prime Selection",R.mipmap.ic_launcher,R.raw.music);
        Music music4=new Music(4,"RISE","The Glitch/Mako/The WORD Alive",R.mipmap.ic_launcher,R.raw.rise);
        musicInfo.add(music1);
        musicInfo.add(music2);
        musicInfo.add(music3);
        musicInfo.add(music4);
    }
    public ArrayList<Music> getMusicInfo(){
        return musicInfo;
    }

}
