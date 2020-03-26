package com.example.ranobeparser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private List<NovelData> novelList;
    private TextView mTextMessage;
    private RecyclerView rv;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        rv = (RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager lim = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(lim);


        ParseNovel parseNovel = new ParseNovel();
        parseNovel.execute();
        try {
            novelList = parseNovel.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initializeAdapter();
    }

    class ParseNovel extends AsyncTask<Void, Void, List<NovelData>> {
        @Override
        protected List<NovelData> doInBackground(Void... voids) {
            novelList = new ArrayList<>();

            try {
                Document document = Jsoup.connect("https://ranobelib.ru/ranobe/")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                                "(KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                        .timeout(100000).get();
                Elements items = document.getElementsByClass("item");
                for (Element item : items) {
                    novelList.add(new NovelData(item.select("a").text(),
                            item.select("img").first().attr("src"),
                            item.select("a").first().attr("href")));
                }
                return novelList;
            } catch (IOException e) {
                System.out.println("ОШИБКА!!!");
                e.printStackTrace();
            }
            return null;
        }
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(novelList);
        rv.setAdapter(adapter);
    }

}