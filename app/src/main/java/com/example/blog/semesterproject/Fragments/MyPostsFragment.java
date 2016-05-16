package com.example.blog.semesterproject.Fragments;

import android.app.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.blog.semesterproject.Adapters.BlogPostAdapter;
import com.example.blog.semesterproject.Entities.BlogPost;
import com.example.blog.semesterproject.R;
import com.example.blog.semesterproject.Utils.AjaxHelper;

import java.util.ArrayList;

/**
 * Created by nicolaiharbo on 05/05/2016.
 */
public class MyPostsFragment extends Fragment {

    static ListView listview;
    private static RecyclerView recycle;
    private static RecyclerView.Adapter mAdapter;
    private SwipeRefreshLayout swipeContainer;

    public static ArrayList<BlogPost> MyPostsList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myposts_recycle, container, false);

        final Activity activity = (Activity) view.getContext();

        recycle = (RecyclerView) view.findViewById(R.id.myposts_recyclerview);

        Log.w("MyPostsFragment: ", "MyPostsList: " + MyPostsList.size());

        //Her sendes listen ned til adapteren, som så genererer et "card" for hver iteration.
        mAdapter = new BlogPostAdapter(MyPostsList);
        recycle.setAdapter(mAdapter);
        recycle.setLayoutManager(new LinearLayoutManager(activity));

        //SWIPE TO REFRESH! Virker fordi der er lavet et SwipeRefreshLayout inde i fragment_allposts-xml'en.
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.MYswipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Clearer arrayet og henter det ind igen.
                MyPostsList.clear();
                AjaxHelper ajax = new AjaxHelper(activity);
                String author = "android"; //OBS!! USERNAME SKAL HENTES FRA TELEFONENS MEMORY, GEMT EFTER LOGIN!
                ajax.getMyPosts(author); //((getMyPosts indeholder en refresh metode))

                //Denne fjerner "loading" ikonet når dataen er klar
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    public static void refreshList() {
        recycle.removeAllViews();
        mAdapter.notifyDataSetChanged();
    }
}
