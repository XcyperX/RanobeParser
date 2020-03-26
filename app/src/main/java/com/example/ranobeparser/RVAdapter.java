package com.example.ranobeparser;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        private TextView novelName;
        private TextView novelInfo;
        private ImageView novelPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            novelName = (TextView)itemView.findViewById(R.id.novel_name);
            novelInfo = (TextView)itemView.findViewById(R.id.novel_info);
            novelPhoto = (ImageView)itemView.findViewById(R.id.novel_photo);
        }

        public void setNovelName(String novelName) {
            this.novelName.setText(novelName);
        }

        public void setNovelInfo(String novelInfo) {
            this.novelInfo.setText(novelInfo);
        }

        public void setNovelPhoto(String novelPhoto) {
            Picasso.get()
                    .load(novelPhoto)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.ic_highlight_off_black_24dp)
                    .resize(1000, 0)
                    .into(this.novelPhoto);
        }
    }

    List<NovelData> novel;

    RVAdapter(List<NovelData> novel){
        this.novel = novel;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, final int i) {
        personViewHolder.novelName.setText(novel.get(i).getNameNovel());
        personViewHolder.novelInfo.setText(novel.get(i).getInfoNovel());
        personViewHolder.setNovelPhoto(novel.get(i).getPhotoId());

        personViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InfoNovelActivity.class);
                intent.putExtra("Name", novel.get(i).getNameNovel());
                intent.putExtra("UrlNovel", novel.get(i).getUrlNovel());
                intent.putExtra("Photo", novel.get(i).getPhotoId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return novel.size();
    }

    public NovelData getItem(int position) {
        return novel.get(position);
    }
}
