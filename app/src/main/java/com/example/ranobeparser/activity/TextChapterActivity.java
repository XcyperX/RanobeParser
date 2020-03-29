package com.example.ranobeparser.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ranobeparser.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class TextChapterActivity extends Activity {

    private TextView chapterText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_chapter);

        chapterText = (TextView)findViewById(R.id.chapterText);

        ParseChapterText parseChapterText = new ParseChapterText();
        parseChapterText.execute();

        try {
            chapterText.setText(parseChapterText.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class ParseChapterText extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
//            ArrayList<String> chapterText = new ArrayList<>();
            String chapterText = "";
            try {
                Document document = Jsoup
                        .connect(Objects.requireNonNull(getIntent().getExtras()).getString("UrlChapter"))
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                        .timeout(100000).get();
                Elements items = document.select("div.text > p");
                if (items.isEmpty()) {
                    System.out.println("Empty!!");
                    Elements itemsTwo = document.select("div.pageBlock.container > p");
                    System.out.println(itemsTwo.isEmpty());
                    for (Element item : itemsTwo) {
                        chapterText = chapterText + "\t\t\t" + item.text() + "\n\n";
                    }
                } else {
                    for (Element item : items) {
                        chapterText = chapterText + "\t\t\t" + item.text() + "\n\n";
                    }
                }
                return chapterText;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
