package com.example.blog.semesterproject.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.blog.semesterproject.Fragments.AllPostsFragment;
import com.example.blog.semesterproject.Entities.BlogPost;
import com.example.blog.semesterproject.Fragments.MyPostsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        rq = Volley.newRequestQueue(mContext);
        //------Ajax------
        JsonArrayRequest jor = new JsonArrayRequest(Request.Method.GET, "http://blogbackend-nh127.rhcloud.com/reader/allposts", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //Log.w("in reponselistener", "onResponse");
                AllPostsFragment.postlist.clear();
                try {
//                            JSONArray jsoneArray = response.getJSONArray("");
                    //Log.w("Main", "inside try newRequestQueue");
                    Log.w("response size: ", "" + response.length());
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jresponse = response.getJSONObject(i);

                        if (!jresponse.has("coverpic")) {
                            String author = jresponse.getString("author");
                            String title = jresponse.getString("title");
                            String content = jresponse.getString("content");
                            String coverpicString = "";
                            if (jresponse.has("latitude")) {
                                String latitude = jresponse.getString("latitude");
                                String longitude = jresponse.getString("longitude");
                                BlogPost pbLoc = new BlogPost(author, title, content, coverpicString, latitude, longitude);
                                AllPostsFragment.postlist.add(pbLoc);
                            } else {
                                BlogPost pb = new BlogPost(author, title, content, coverpicString);
                                AllPostsFragment.postlist.add(pb);
                            }
                        } else {
                            String author = jresponse.getString("author");
                            String title = jresponse.getString("title");
                            String content = jresponse.getString("content");
                            String coverpicString = jresponse.getString("coverpic");
                            if (jresponse.has("latitude")) {
                                String latitude = jresponse.getString("latitude");
                                String longitude = jresponse.getString("longitude");
                                BlogPost pbLoc = new BlogPost(author, title, content, coverpicString, latitude, longitude);
                                AllPostsFragment.postlist.add(pbLoc);
                            } else {
                                BlogPost pb = new BlogPost(author, title, content, coverpicString);
                                AllPostsFragment.postlist.add(pb);
                            }
                        }
                    }
                    Log.w("htmlarray size: ", "" + AllPostsFragment.postlist.size());
                    //Listen som ligger i Fragmentet er nu fyldt op

                    //Her sætters den nyeste post først, ved at reverse arrayet..
                    Collections.reverse(AllPostsFragment.postlist);

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

    public void addNewPost(Bitmap coverpic, final String author, final String title, final String content, double latitude, double longitude) {

        if (coverpic != null) {

            //Her laves billedet om til et bytearray, så vi kan gemme det i db.
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
//            coverpic.compress(Bitmap.CompressFormat.JPEG, 100, blob);
            coverpic.compress(Bitmap.CompressFormat.JPEG, 80, blob);
            byte[] imageBytes = blob.toByteArray();
            final String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            String url = "http://blogbackend-nh127.rhcloud.com/blogger/newpost/";

            Map<String, String> params = new HashMap();
            params.put("author", author);
            params.put("title", title);
            params.put("content", content);
            params.put("coverpic", encodedImage);
            params.put("latitude", "" + latitude);
            params.put("longitude", "" + longitude);
            Boolean b = false;
            String str = String.valueOf(b);
            params.put("draft", str);

            JSONObject parameters = new JSONObject(params);

            Log.w("JSON: ", parameters.toString());

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.w("POST: ", "success!");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.w("POST: ", "error! " + error.getMessage());
                }
            });

            Volley.newRequestQueue(mContext).add(jsonRequest);

        } else {
            String url = "http://blogbackend-nh127.rhcloud.com/blogger/newpost/";

            Map<String, String> params = new HashMap();
            params.put("author", author);
            params.put("title", title);
            params.put("content", content);
            params.put("coverpic", "null");
            Boolean b = false;
            String str = String.valueOf(b);
            params.put("draft", str);

            JSONObject parameters = new JSONObject(params);

            Log.w("JSON: ", parameters.toString());

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.w("POST: ", "success!");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.w("POST: ", "error! " + error.getMessage());
                }
            });

            Volley.newRequestQueue(mContext).add(jsonRequest);
        }
    }


    public void getMyPosts(String author) {
        rq = Volley.newRequestQueue(mContext);

        //------Ajax------
        JsonArrayRequest jor = new JsonArrayRequest(Request.Method.GET, "http://blogbackend-nh127.rhcloud.com/blogger/myposts/" + author, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                MyPostsFragment.MyPostsList.clear();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jresponse = response.getJSONObject(i);

                        if (!jresponse.has("coverpic") || jresponse.getString("coverpic").equals("null")) {
                            String author = jresponse.getString("author");
                            String title = jresponse.getString("title");
                            String content = jresponse.getString("content");
                            String coverpicString = "null";
                            if (jresponse.has("latitude")) {
                                String latitude = jresponse.getString("latitude");
                                String longitude = jresponse.getString("longitude");
                                BlogPost pbLoc = new BlogPost(author, title, content, coverpicString, latitude, longitude);
                                MyPostsFragment.MyPostsList.add(pbLoc);
                            } else {
                                BlogPost pb = new BlogPost(author, title, content, coverpicString);
                                MyPostsFragment.MyPostsList.add(pb);
                            }
                        } else {
                            String author = jresponse.getString("author");
                            String title = jresponse.getString("title");
                            String content = jresponse.getString("content");
                            String coverpicString = jresponse.getString("coverpic");
                            if (jresponse.has("latitude")) {
                                String latitude = jresponse.getString("latitude");
                                String longitude = jresponse.getString("longitude");
                                BlogPost pbLoc = new BlogPost(author, title, content, coverpicString, latitude, longitude);
                                MyPostsFragment.MyPostsList.add(pbLoc);
                            } else {
                                BlogPost pb = new BlogPost(author, title, content, coverpicString);
                                MyPostsFragment.MyPostsList.add(pb);
                            }
                        }
                    }
                    Log.w("getMyPosts arraysize ", "is: " + MyPostsFragment.MyPostsList.size());
                    //Listensom ligger i Fragmentet er nu fyldt op

                    //Her sætters den nyeste post først, ved at reverse arrayet..
                    Collections.reverse(MyPostsFragment.MyPostsList);

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
