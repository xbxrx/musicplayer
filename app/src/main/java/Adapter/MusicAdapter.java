package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.R;

import java.util.ArrayList;
import Model.Music;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private ArrayList<Music> myMusicList;
    public MusicAdapter(ArrayList<Music> musicList){
        myMusicList=musicList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Music music=myMusicList.get(position);
        holder.author.setText(music.getAuthor());
        holder.title.setText(music.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"点击了"+position,Toast.LENGTH_SHORT).show();
                Context context=v.getContext();
                Intent intent=new Intent(context,MainActivity.class);
                intent.putExtra("send","play");
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return myMusicList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView author;
        public ViewHolder(View view){
            super(view);
            title=view.findViewById(R.id.title_text);
            author=view.findViewById(R.id.author_text);
        }
    }
}
