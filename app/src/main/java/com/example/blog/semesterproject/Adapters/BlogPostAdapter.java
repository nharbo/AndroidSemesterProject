package com.example.blog.semesterproject.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blog.semesterproject.Entities.BlogPost;
import com.example.blog.semesterproject.R;

import java.util.ArrayList;


public class BlogPostAdapter extends RecyclerView.Adapter<BlogPostAdapter.MyViewHolder> {

    private ArrayList<BlogPost> blogPostList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView author;
        public TextView startContent;
        public ImageView coverImage;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            author = (TextView) view.findViewById(R.id.author);
            startContent = (TextView) view.findViewById(R.id.startcontent);
            coverImage = (ImageView) view.findViewById(R.id.cover_image_cardview);
        }
    }


    public BlogPostAdapter(ArrayList<BlogPost> blogPostList) {
        this.blogPostList = blogPostList;
    }

    public BlogPostAdapter() {

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blogs_list_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        BlogPost blogpost = blogPostList.get(position);
        holder.title.setText("Title: " + blogpost.getTitle());
        holder.author.setText("By: " + blogpost.getAuthor());

        //Content
        String startContent = ".....";
        if (blogpost.getContent().length() < 50) {
            startContent = blogpost.getContent() + ".....";
        } else {
            startContent = blogpost.getContent().substring(0, 50) + ".....";
        }
        holder.startContent.setText(Html.fromHtml(startContent));

        //Coverimage
//        if (blogpost.getCoverpicString().length() > 0) {
            String coverImageString = blogpost.getCoverpicString();
            byte[] imageBytes = Base64.decode(coverImageString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

            holder.coverImage.setImageBitmap(bitmap);
//        }

    }

    @Override
    public int getItemCount() {
        return blogPostList.size();
    }
}
