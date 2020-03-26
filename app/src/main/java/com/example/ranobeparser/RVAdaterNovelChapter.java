package com.example.ranobeparser;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class RVAdaterNovelChapter extends RecyclerView.Adapter<RVAdaterNovelChapter.ChapterViewHolder> {

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        private TextView novelName;

        ChapterViewHolder(View itemView) {
            super(itemView);
            novelName = (TextView)itemView.findViewById(R.id.novel_name);
        }

        public void setNovelName(String novelName) {
            this.novelName.setText(novelName);
        }
    }

    LinkedHashMap<String, String> novelData;

    RVAdaterNovelChapter(LinkedHashMap<String, String> novelData) {
        this.novelData = novelData;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chapter, viewGroup, false);
        ChapterViewHolder pvh = new ChapterViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ChapterViewHolder chapterViewHolder, final int i) {
        List<String> list = new ArrayList<String>(novelData.values());
        final List<String> urlChapter = new ArrayList<>(novelData.keySet());
        chapterViewHolder.novelName.setText(list.get(i));

        chapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TextChapterActivity.class);
                intent.putExtra("UrlChapter", urlChapter.get(i));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return novelData.size();
    }
}
