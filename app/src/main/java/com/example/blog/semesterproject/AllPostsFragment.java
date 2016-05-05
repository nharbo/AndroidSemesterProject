package com.example.blog.semesterproject;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by nicolaiharbo on 05/05/2016.
 */
public class AllPostsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allposts_listview, container, false);

//        Activity activity = (Activity)view.getContext();

//        gridView = (GridView) view.findViewById(R.id.gridview);
//        gridView.setAdapter(new ImageAdapter(activity));

        return view;
    }
}
