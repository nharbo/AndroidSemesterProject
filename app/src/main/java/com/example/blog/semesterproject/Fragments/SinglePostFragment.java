package com.example.blog.semesterproject.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blog.semesterproject.Entities.BlogPost;
import com.example.blog.semesterproject.R;

/**
 * Created by nicolaiharbo on 16/05/2016.
 */
public class SinglePostFragment extends Fragment {

    BlogPost singlePost;

    public SinglePostFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        savedInstanceState = getArguments();

        String title = savedInstanceState.getString("title");
        String author = savedInstanceState.getString("author");
        String content = savedInstanceState.getString("content");
        String coverpic = savedInstanceState.getString("coverpic");

        View view1 = inflater.inflate(R.layout.fragment_single_post_head, container, false);
//        View view2 = inflater.inflate(R.layout.fragment_single_post_content, container, false);
//        View view = inflater.inflate(R.layout.fragment_single_post, container, false);

        TextView textViewAuthor = (TextView) view1.findViewById(R.id.singlePostAuthor);
        TextView textViewTitle = (TextView) view1.findViewById(R.id.singlePostTitle);
        TextView textViewContent = (TextView) view1.findViewById(R.id.singlePostContent);
        ImageView imageViewCoverpic = (ImageView) view1.findViewById(R.id.singlePostCoverpic);

        textViewAuthor.setText("By: " + author);
        textViewTitle.setText("Title: " + title);
        textViewContent.setText(content);

        //Coverimage
        String coverImageString = coverpic;
        byte[] imageBytes = Base64.decode(coverImageString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        imageViewCoverpic.setImageBitmap(bitmap);

        return view1;
    }
}
