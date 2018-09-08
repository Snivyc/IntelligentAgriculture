package com.example.snivy.intelligentagriculture;


import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Snivy on 2018/4/17.
 */

public class NodeSet {

    public List<Node> allNodes = new ArrayList<>();

    private MainActivity mMainActivity;

    private String partOfURL;

    private RunAfter mRunAfter;

    NodeSet(MainActivity mainActivity,RunAfter runAfter) {
        mMainActivity = mainActivity;
        mRunAfter = runAfter;
    }

    public void reflash() {
        partOfURL = "index/Showall";
        sendRequestWithOkHttp();
//        Log.e("test", "reflash: test");
    }

    public void reflash(int i) {
        partOfURL = "Result/Showbymac?mac=" + mMainActivity.mNodeSet.getNode(i).getMAC();
        sendRequestWithOkHttp();
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder().build();

                    Request request = new Request.Builder()
                            .url("http://119.29.226.30/agriculture/" + partOfURL)
                            .get()
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    parseJSONWithGSON(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArratData = jsonObject.getJSONArray("data");
            jsonData = jsonArratData.toString();
            Gson gson = new Gson();
            allNodes = gson.fromJson(jsonData, new TypeToken<List<Node>>(){}.getType());
            mMainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                mMainActivity.undo_point_clicked();
                    mRunAfter.run(mMainActivity);
//                    mMainActivity.printNodes();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    List<Node> getAllNodes(){
        return allNodes;
    }

    Node getNode(int i) {
        return allNodes.get(i);
    }

    public interface RunAfter{
        void run(MainActivity m);
    }
}
