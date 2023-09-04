package com.example.giphyapigifimagessearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.giphyapigifimagessearch.Adapters.SearchedImagesAdapter;
import com.example.giphyapigifimagessearch.Models.Image;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SearchedImagesAdapter.OnItemClickListener {


    SearchView searchView;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    SearchedImagesAdapter adapter;
    List<Image> imageList = new ArrayList<>();

    String url = "https://api.giphy.com/v1/gifs/trending?api_key=3ghe3gbyFL4LbYLdYSSgjWFpNCxA39nr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_search);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        recyclerView.setHasFixedSize(true);

        searchView = (SearchView) findViewById(R.id.search_view);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray dataArray = response.getJSONArray("data");

                    for (int i=0; i<dataArray.length();i++){
                        JSONObject obj = dataArray.getJSONObject(i);

                        JSONObject obj1 = obj.getJSONObject("images");
                        JSONObject obj2 = obj1.getJSONObject("downsized_medium");

                        String sourceUrl = obj2.getString("url");

                        imageList.add(new Image(sourceUrl));
                    }

                    adapter = new SearchedImagesAdapter(MainActivity.this, imageList);
                    recyclerView.setAdapter(adapter);
                    //    imagesAdapter.setOnItemClickListener(MainActivity.this::onItemClick);
                    adapter.notifyDataSetChanged();
                    adapter.setOnItemClickListener(MainActivity.this::onItemClick);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Kluda "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Requests.getInstance(this).addToRequestQueue(objectRequest);

//        SearchedImagesAdapter adapter = new SearchedImagesAdapter(MainActivity.this, imageList);
//        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                filter(query);
//                return true;
//                query = searchView.getQuery().toString();
//                if(query==""){
//                    getAllImages();
//
//                }
//                getAllImages();
//                imageList = new ArrayList<>();

                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                if (newText==""){//(newText.isEmpty()) {
//                    getAllImages();
//                }
//                else {
//                imageList = new ArrayList<>();

                filter(newText);
//                }
                return true;
//                return false;
            }
        });

//        View view = this.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }

    }
    private void filter(String newText) {
        Log.d("Teksts ko sanjemam", newText);

//        imageList = new ArrayList<>();
//        if (newText.isEmpty()){
//            getAllImages();
//        } else {

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    imageList = new ArrayList<>();
                    String gifName = newText.toLowerCase();
                    boolean boo = newText == gifName;
                    Log.d("Teksts ko sanjemam", boo + " " + gifName + newText);

                    try {
                        JSONArray dataArray = response.getJSONArray("data");


                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject obj = dataArray.getJSONObject(i);

                            JSONObject obj1 = obj.getJSONObject("images");
                            JSONObject obj2 = obj1.getJSONObject("downsized_medium");

                            String sourceUrl = obj2.getString("url");

                            String imgTitle = obj.getString("title");

//                        JSONObject objUser = obj1.getJSONObject("user");
//                        String displayUserName = objUser.getString("display_name");

                            if (imgTitle.toString().toLowerCase().contains(gifName)) {
                                imageList.add(new Image(sourceUrl));
                            }
                        }

                        adapter = new SearchedImagesAdapter(MainActivity.this, imageList);
                        recyclerView.setAdapter(adapter);
                        //    imagesAdapter.setOnItemClickListener(MainActivity.this::onItemClick);
                    adapter.notifyDataSetChanged();
//                        adapter.setFilteredList(imageList);
                        adapter.setOnItemClickListener(MainActivity.this::onItemClick);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Kluda " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            Requests.getInstance(this).addToRequestQueue(objectRequest);

//        }
    }

    private void getAllImages(){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray dataArray = response.getJSONArray("data");

                    for (int i=0; i<dataArray.length();i++){
                        JSONObject obj = dataArray.getJSONObject(i);

                        JSONObject obj1 = obj.getJSONObject("images");
                        JSONObject obj2 = obj1.getJSONObject("downsized_medium");

                        String sourceUrl = obj2.getString("url");

                        imageList.add(new Image(sourceUrl));
                    }

                    adapter = new SearchedImagesAdapter(MainActivity.this, imageList);
                    recyclerView.setAdapter(adapter);
                    //    imagesAdapter.setOnItemClickListener(MainActivity.this::onItemClick);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Kluda "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Requests.getInstance(this).addToRequestQueue(objectRequest);


    }


    @Override
    public void onItemClick(int pos) {
        Intent oneImg = new Intent(this, OneImageActivity.class);
        Image clickedItem = imageList.get(pos);
        oneImg.putExtra("oneImg",clickedItem.getImgUrl());
        startActivity(oneImg);
    }
}