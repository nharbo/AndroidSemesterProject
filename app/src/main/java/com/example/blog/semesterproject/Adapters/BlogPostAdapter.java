package com.example.blog.semesterproject.Adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        public MyViewHolder(View view) {
            super(view);
            //Her kan evt sættes et billede ind, i toppen af hvert kort.
            title = (TextView) view.findViewById(R.id.title);
            author = (TextView) view.findViewById(R.id.author);
            startContent = (TextView) view.findViewById(R.id.startcontent);
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
                .inflate(R.layout.allblogs_list_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BlogPost blogpost = blogPostList.get(position);
        holder.title.setText("Title: " + blogpost.getTitle());
        holder.author.setText("By: " + blogpost.getAuthor());

        String startContent = ".....";

        if (blogpost.getContent().length() < 50) {
            startContent = blogpost.getContent() + ".....";
        } else {
            startContent = blogpost.getContent().substring(0, 50) + ".....";
        }


        holder.startContent.setText(Html.fromHtml(startContent));
    }

    @Override
    public int getItemCount() {
        return blogPostList.size();
    }
}
