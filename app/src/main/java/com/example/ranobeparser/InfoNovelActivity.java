package com.example.ranobeparser;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class InfoNovelActivity extends Activity {
    private TextView name;
    private TextView info;
    private ImageView photo;
    private LinkedHashMap<String, String> urlChapterNovel;
    private RecyclerView rv;
    private ArrayList<String> pageUrlChapter;
    private Button loadChapterButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_novel);

        name = (TextView)findViewById(R.id.novel_name);
        info = (TextView)findViewById(R.id.novel_info);
        photo = (ImageView)findViewById(R.id.novel_photo);
        rv = (RecyclerView)findViewById(R.id.rvChapter);
        loadChapterButton = (Button)findViewById(R.id.loadNewChapter);

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
        final ParseNovel parseNovel = new ParseNovel();
        parseNovel.execute(Objects.requireNonNull(getIntent().getExtras()).getString("UrlNovel"));
        ParsePageChapter parsePageChapter = new ParsePageChapter();
        parsePageChapter.execute();
        try {
            urlChapterNovel = parseNovel.get();
            pageUrlChapter = parsePageChapter.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initializeAdapter();

        loadChapterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseNovel parseNovel = new ParseNovel();
                parseNovel.execute(pageUrlChapter.get(0));
                pageUrlChapter.remove(0);
                if (pageUrlChapter.isEmpty()) {
                    v.setVisibility(View.GONE);
                }
                try {
                    urlChapterNovel.putAll(parseNovel.get());
                    initializeAdapter();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
//    class parse extends AsyncTask<String, Void, LinkedHashMap<String, String>> {
//
//        @Override
//        protected LinkedHashMap<String, String> doInBackground(String... strings) {
//            return null;
//        }
//    }

    class ParseNovel extends AsyncTask<String, Void, LinkedHashMap<String, String>> {
        @Override
        protected LinkedHashMap<String, String> doInBackground(String... string) {
            LinkedHashMap<String, String> chapter = new LinkedHashMap<>();
            try {
                Document document = Jsoup
                        .connect(string[0])
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                        .timeout(100000).get();
                Elements items = document.getElementsByClass("ttl");
                Elements UrlChapter = document.select("a.page-numbers");
                for (Element item : items) {
                    chapter.put(item.select("a").first().attr("href"),
                            item.select("a").first().text().replace("ЧИТАТЬ", ""));
                }
                return chapter;
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
            try {
                Document document = Jsoup
                        .connect(Objects.requireNonNull(getIntent().getExtras()).getString("UrlNovel"))
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                                "(KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                        .timeout(100000).get();
                Elements elements = document.select("a.page-numbers");
                for (Element url : elements) {
                    System.out.println(url.attr("href"));
                    pages.add(url.attr("href"));
                }
                pages.remove(elements.size() - 1);
                return pages;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class ParseNovelOnClickButton extends AsyncTask<Void, Void, LinkedHashMap<String, String>> {
        @Override
        protected LinkedHashMap<String, String> doInBackground(Void... voids) {
            LinkedHashMap<String, String> chapter = new LinkedHashMap<>();
            try {
                Document document = Jsoup.connect(Objects.requireNonNull(getIntent().getExtras()).getString("UrlNovel")).get();
                Elements items = document.getElementsByClass("ttl");
                for (Element item : items) {
                    chapter.put(item.select("a").first().attr("href"),
                            item.select("a").first().text().replace("ЧИТАТЬ", ""));
                }
                return chapter;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void initializeAdapter() {
        System.out.println("Test");
        RVAdaterNovelChapter adapter = new RVAdaterNovelChapter(urlChapterNovel);
        rv.setAdapter(adapter);
    }

}
