package com.example.ranobeparser.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ranobeparser.JSON.JSONHelper;
import com.example.ranobeparser.R;
import com.example.ranobeparser.activity.TextChapterActivity;
import com.example.ranobeparser.entity.URLChapter;

import java.util.ArrayList;

public class RVAdaterNovelChapter extends RecyclerView.Adapter<RVAdaterNovelChapter.ChapterViewHolder> {

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        private TextView novelName;
        private ImageView bookImage;

        ChapterViewHolder(View itemView) {
            super(itemView);
            novelName = (TextView)itemView.findViewById(R.id.novel_name);
            bookImage = (ImageView)itemView.findViewById(R.id.novel_photo_chapter);
        }

        public void setNovelName(String novelName) {
            this.novelName.setText(novelName);
        }

        public void setBookImage(int bookImage) {
            this.bookImage.setImageResource(bookImage);
        }
    }

    ArrayList<URLChapter> novelData;
    String nameNovel;

    public RVAdaterNovelChapter(ArrayList<URLChapter> novelData, String nameNovel) {
        this.novelData = novelData;
        this.nameNovel = nameNovel;
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
        chapterViewHolder.novelName.setText(novelData.get(i).getNameChapter());
        if (novelData.get(i).isReadChepter()) {
            chapterViewHolder.setBookImage(R.drawable.book_close);
        }
        chapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novelData.get(i).setReadChepter(true);
                chapterViewHolder.setBookImage(R.drawable.book_close);

                boolean result = JSONHelper.exportToJSON(v.getContext(), novelData, nameNovel + ".json");
                if (result) {
//                    Toast.makeText(v.getContext(), "Данные сохраннены", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(v.getContext(), "Ошибка!!", Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(v.getContext(), TextChapterActivity.class);
                intent.putExtra("UrlChapter", novelData.get(i).getUrlChapter());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return novelData.size();
    }
}
