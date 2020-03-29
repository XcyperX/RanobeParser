package com.example.ranobeparser.JSON;

import android.content.Context;

import com.example.ranobeparser.entity.NovelData;
import com.example.ranobeparser.entity.URLChapter;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class JSONHelper {
    private static final String FILE_NAME = "data.json";
    private static final String FILE_NAME_MAIN_DATA = "main_data.json";

    public static boolean exportToJSON(Context context, ArrayList<URLChapter> novelData, String name) {
        Gson gson = new Gson();
        DataItems dataItems = new DataItems();
        dataItems.setNovelData(novelData);
        String jsonString = gson.toJson(dataItems);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = context.openFileOutput(Arrays.toString(name.getBytes()), Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public static ArrayList<URLChapter> importFromJSON(Context context, String name) {
        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = context.openFileInput(Arrays.toString(name.getBytes()));
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(streamReader, DataItems.class);
            return dataItems.getNovelData();
        }
        catch (IOException ex){
                ex.printStackTrace();
            }
        finally {
                if (streamReader != null) {
                    try {
                        streamReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
    }

    public static boolean exportToJSONMainData(Context context, List<NovelData> novelData) {
        Gson gson = new Gson();
        DataMainItems dataItems = new DataMainItems();
        dataItems.setNovelData(novelData);
        String jsonString = gson.toJson(dataItems);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = context.openFileOutput(FILE_NAME_MAIN_DATA, Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public static List<NovelData> importFromJSONMainData(Context context) {
        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = context.openFileInput(FILE_NAME_MAIN_DATA);
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            DataMainItems dataItems = gson.fromJson(streamReader, DataMainItems.class);
            return dataItems.getNovelData();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        finally {
            if (streamReader != null) {
                try {
                    streamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private static class DataItems {
        private ArrayList<URLChapter> novelData;

        public ArrayList<URLChapter> getNovelData() {
            return novelData;
        }

        public void setNovelData(ArrayList<URLChapter> novelData) {
            this.novelData = novelData;
        }
    }

    private static class DataMainItems {
        private List<NovelData> novelData;

        public List<NovelData> getNovelData() {
            return novelData;
        }

        public void setNovelData(List<NovelData> novelData) {
            this.novelData = novelData;
        }
    }
}
