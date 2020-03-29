package com.example.ranobeparser.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ranobeparser.JSON.JSONHelper;
import com.example.ranobeparser.R;
import com.example.ranobeparser.adapter.RVAdaterNovelChapter;
import com.example.ranobeparser.entity.URLChapter;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class InfoNovelActivity extends Activity {
    private TextView name;
    private TextView info;
    private ImageView photo;
    private RecyclerView rv;
    private ArrayList<String> pageUrlChapter;
    private ArrayList<URLChapter> urlChapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_novel);

        name = (TextView)findViewById(R.id.novel_name);
        info = (TextView)findViewById(R.id.novel_info);
        photo = (ImageView)findViewById(R.id.novel_photo);
        rv = (RecyclerView)findViewById(R.id.rvChapter);
        Button loadChapterButton = (Button) findViewById(R.id.loadNewChapter);

        LinearLayoutManager lim = new LinearLayoutManager(this);
        rv.setLayoutManager(lim);
        rv.setHasFixedSize(true);

        name.setText(Objects.requireNonNull(getIntent().getExtras()).getString("Name"));

        Picasso.get()
                .load(Objects.requireNonNull(getIntent().getExtras()).getString("Photo"))
                .placeholder(R.drawable.loading)
                .error(R.drawable.ic_highlight_off_black_24dp)
                .resize(1000, 0)
                .into(this.photo);



        boolean loadNovel = loadNovelData();
        if (loadNovel) {
            System.out.println("ИЗ ФАЙЛА!!");
            initializeAdapter();
            ParsePageChapter parsePageChapter = new ParsePageChapter();
            parsePageChapter.execute();
            try {
                pageUrlChapter = parsePageChapter.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            ParsePageChapter parsePageChapter = new ParsePageChapter();
            parsePageChapter.execute();
            try {
                pageUrlChapter = parsePageChapter.get();

                final ParseNovel parseNovel = new ParseNovel();
                parseNovel.execute(pageUrlChapter.get(0));
                urlChapter = parseNovel.get();
                pageUrlChapter.remove(0);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            initializeAdapter();
        }

        loadChapterButton.setOnClickListener(v -> {
            for (URLChapter chapter : urlChapter) {
                for (Iterator<String> iterator = pageUrlChapter.iterator(); iterator.hasNext(); ) {
                    if (chapter.getURLPage().contains(iterator.next())) {
                        iterator.remove();
                    }
                }
            }
            System.out.println(pageUrlChapter.size());
            for (String page : pageUrlChapter) {
                System.out.println(page);
            }
            final ParseNovel parseNovel = new ParseNovel();

            parseNovel.execute(pageUrlChapter.get(0));
            if (pageUrlChapter.size() == 1) {
                v.setVisibility(View.GONE);
            }
                try {
                    loadNovelData();
                    urlChapter.addAll(parseNovel.get());
                    saveChepterData();
                    initializeAdapter();
                } catch (ExecutionException e) {
                    this.finish();
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    this.finish();
                    e.printStackTrace();
                }
        });
    }

    class ParseNovel extends AsyncTask<String, Void, ArrayList<URLChapter>> {
        @Override
        protected ArrayList<URLChapter> doInBackground(String... string) {
            ArrayList<URLChapter> urlChapterArrayList = new ArrayList<>();

            try {
                Document document = Jsoup
                        .connect(string[0])
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                        .timeout(100000).get();
                Elements items = document.getElementsByClass("ttl");
                for (Element item : items) {
                    URLChapter chapter1 = new URLChapter(item.select("a").first().attr("href"),
                            item.select("a").first().text().replace("ЧИТАТЬ", ""));
                    chapter1.setURLPage(pageUrlChapter.get(0));
                    urlChapterArrayList.add(chapter1);
                }
                Collections.reverse(urlChapterArrayList);
                return urlChapterArrayList;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class ParsePageChapter extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> pages = new ArrayList<>();
            String urlAdress = Objects.requireNonNull(getIntent().getExtras()).getString("UrlNovel");
            try {
                Document document = Jsoup
                        .connect(urlAdress)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                                "(KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                        .timeout(100000).get();
                Elements elements = document.select("a.page-numbers");
                elements.remove(elements.last());
                if (!elements.isEmpty()) {
                    int countPage = Integer.parseInt(elements.last().text());
                    for (int i = countPage; i >= 1; i--) {
                        System.out.println(urlAdress + "page/" + i);
                        pages.add(urlAdress + "page/" + i);
                    }
                    return pages;
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadNovelData();
        saveChepterData();
//        boolean result = JSONHelper.exportToJSON(this, urlChapter,
//                Objects.requireNonNull(getIntent().getExtras()).getString("Name"));
//        if (result) {
//            Toast.makeText(this, "Данные сохраннены", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "Ошибка!!", Toast.LENGTH_LONG).show();
//        }
    }

    public void saveChepterData() {
        boolean result = JSONHelper.exportToJSON(this, urlChapter,
                Objects.requireNonNull(getIntent().getExtras()).getString("Name"));
        if (result) {
//            Toast.makeText(this, "Данные сохраннены", Toast.LENGTH_LONG).show();
        } else {
//            Toast.makeText(this, "Ошибка!!", Toast.LENGTH_LONG).show();
        }
    }

    public boolean loadNovelData() {
        ArrayList<URLChapter> loadData = JSONHelper.importFromJSON(this,
                Objects.requireNonNull(getIntent().getExtras()).getString("Name") + ".json");
        if (loadData != null) {
            urlChapter = loadData;
//            Toast.makeText(this, "Данные загружены", Toast.LENGTH_LONG).show();
            return true;
        } else {
//            Toast.makeText(this, "Данных нет", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void initializeAdapter() {
        RVAdaterNovelChapter adapter = new RVAdaterNovelChapter(urlChapter,
                Objects.requireNonNull(getIntent().getExtras()).getString("Name"));
        rv.setAdapter(adapter);
    }

}
