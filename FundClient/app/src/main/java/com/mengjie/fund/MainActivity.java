package com.mengjie.fund;

import androidx.appcompat.app.AppCompatActivity;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mengjie.util.PagingArrayAdapter;
import com.paging.listview.PagingBaseAdapter;
import com.paging.listview.PagingListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> fundList=new ArrayList<>();
    private int more=0;
    OkHttpClient client = new OkHttpClient();
    int current_page = 0;
    static String URL = "http://10.0.2.2:8080/";

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fund);
        final PagingListView fundList = findViewById(R.id.fundList);
        for(int i=0;i<20;i++) this.fundList.add(String.valueOf(i));
//        final ArrayAdapter<String> adapter = new PagingBaseAdapter<String>(this,android.R.layout.simple_list_item_1,this.fundList);
        fundList.setAdapter(new PagingArrayAdapter());

        fundList.setHasMoreItems(true);
        fundList.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String str = MainActivity.this.run(URL+"api/fund");
                            JSONObject data = new JSONObject(str);
                            Log.d("MainActivity", str);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }
}
