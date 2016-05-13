package com.example.blog.semesterproject.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.blog.semesterproject.Fragments.AllPostsFragment;
import com.example.blog.semesterproject.Entities.BlogPost;
import com.example.blog.semesterproject.Fragments.MyPostsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Blob;

/**
 * Created by nicolaiharbo on 06/05/2016.
 */
public class AjaxHelper {

    Context mContext;

    public AjaxHelper(Context context) {
        this.mContext = context;
    }

    RequestQueue rq;

    public void getAllPosts() {

        //Log.w("Main", "before newRequestQueue");
        rq = Volley.newRequestQueue(mContext);
        //Log.w("Main", "after newRequestQueue, before jsonobjectreq");
        //------Ajax------
        JsonArrayRequest jor = new JsonArrayRequest(Request.Method.GET, "http://blogbackend-nh127.rhcloud.com/reader/allposts", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //Log.w("in reponselistener", "onResponse");
                try {
//                            JSONArray jsoneArray = response.getJSONArray("");
                    //Log.w("Main", "inside try newRequestQueue");
                    //Log.w("response size: ", "" + response.length());
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jresponse = response.getJSONObject(i);

                        //Log.w("response: ", jresponse.toString());
                        String author = jresponse.getString("author");
                        String title = jresponse.getString("title");
                        String content = jresponse.getString("content");

                        BlogPost pb = new BlogPost(author, title, content);
                        AllPostsFragment.postlist.add(pb);
                        //Log.w("htmlarray size: ", "" + AllPostsFragment.postlist.size());
                        //Listensom ligger i Fragmentet er nu fyldt op
                    }

                    //Vi refresher listen, da dataen først nu er kommet ind, og er klar til visning.
                    AllPostsFragment.refreshList();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.w("ajax error", e.getMessage().toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error, getAllPosts", "response error listener: " + error);
            }
        }
        );
        rq.add(jor);
        Log.w("AJAX", "GET ALL POSTS, success");
        //------Ajax slut------
    }

    public void addNewPost(Bitmap coverpic, String author, String title, String content) {
        if (coverpic != null) {
            Log.w("coverpic: ", coverpic.toString());
            Log.w("author: ", author);
            Log.w("title: ", title);
            Log.w("content: ", content);
        } else {
            Log.w("author: ", author);
            Log.w("title: ", title);
            Log.w("content: ", content);
        }

    }

    public void getMyPosts(String username) {
        rq = Volley.newRequestQueue(mContext);

        //------Ajax------
        JsonArrayRequest jor = new JsonArrayRequest(Request.Method.GET, "http://blogbackend-nh127.rhcloud.com/blogger/myposts/" + username, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jresponse = response.getJSONObject(i);

                        //Log.w("response: ", jresponse.toString());
                        String author = jresponse.getString("author");
                        String title = jresponse.getString("title");
                        String content = jresponse.getString("content");

                        BlogPost pb = new BlogPost(author, title, content);
                        MyPostsFragment.MyPostsList.add(pb);
                        //Listensom ligger i Fragmentet er nu fyldt op
                    }

                    //Vi refresher listen, da dataen først nu er kommet ind, og er klar til visning.
                    MyPostsFragment.refreshList();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.w("ajax error", e.getMessage().toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error, getMyPosts", "response error listener: " + error);
            }
        });
        rq.add(jor);
        Log.w("AJAX", "GET MY POSTS, success");
        //------Ajax slut------
    }
}
