package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import java.util.ArrayList;
import Adapter.MusicAdapter;
import Model.Music;
import Model.MusicInfo;

public class ListActivity extends AppCompatActivity {

    private MusicInfo musicInfo=new MusicInfo();
    private ArrayList<Music> musicList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        musicList=musicInfo.getMusicInfo();
        RecyclerView recyclerView=findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MusicAdapter adapter=new MusicAdapter(musicList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }
}