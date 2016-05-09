package com.example.blog.semesterproject.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;

/**
 * Created by nicolaiharbo on 05/05/2016.
 */
public class AllPostsFragment extends Fragment {

    static ListView listview;
    private static RecyclerView recycle;
    private static RecyclerView.Adapter mAdapter;

    public static ArrayList<BlogPost> postlist = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_allposts_recycle, container, false);

        Activity activity = (Activity)view.getContext();

        recycle = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        Log.w("postlist: ", "" + postlist.size());
        mAdapter = new BlogPostAdapter(postlist);
        recycle.setAdapter(mAdapter);
        recycle.setLayoutManager(new LinearLayoutManager(activity));

        return view;
    }

    public static void refreshList(){
        recycle.removeAllViews();
        mAdapter.notifyDataSetChanged();
    }
}
