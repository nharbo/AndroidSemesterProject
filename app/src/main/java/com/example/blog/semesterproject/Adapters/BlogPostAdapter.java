package com.example.blog.semesterproject.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blog.semesterproject.Entities.BlogPost;
import com.example.blog.semesterproject.Fragments.AllPostsFragment;
import com.example.blog.semesterproject.Fragments.SinglePostFragment;
import com.example.blog.semesterproject.MainActivity;
import com.example.blog.semesterproject.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class BlogPostAdapter extends RecyclerView.Adapter<BlogPostAdapter.MyViewHolder> {

    Context mContext;

    //ViewHolderen finder de enkelte text/image-views frem, og så inflater onCreateViewHOlder dem efterfølgende.
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView author;
        public TextView startContent;
        public ImageView coverImage;
        public TextView location;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            author = (TextView) view.findViewById(R.id.author);
            startContent = (TextView) view.findViewById(R.id.startcontent);
            coverImage = (ImageView) view.findViewById(R.id.cover_image_cardview);
            location = (TextView) view.findViewById(R.id.location_city);
        }
    }


    public ArrayList<BlogPost> blogPostList = new ArrayList<>();


    public BlogPostAdapter(ArrayList<BlogPost> blogPostList) {
        this.blogPostList = blogPostList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.blogs_list_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        //Her fyldes det enkelte card-view op.

        final BlogPost blogpost = blogPostList.get(position);

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
        String coverImageString = blogpost.getCoverpicString();
        byte[] imageBytes = Base64.decode(coverImageString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        holder.coverImage.setImageBitmap(bitmap);

        //Finder byen for den givne location, hvis den er gemt.
        if (blogpost.getLongitude() != null) {
            double lat = Double.parseDouble(blogpost.getLatitude());
            double lng = Double.parseDouble(blogpost.getLongitude());

            Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(lat, lng, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses.size() > 0)
                holder.location.setText("Posted from: " + addresses.get(0).getLocality());
        } else {
            holder.location.setText("Location unknown");
        }

    }

    @Override
    public int getItemCount() {
        Log.w("ItemCount in Adapter: ", "" + blogPostList.size());
        return blogPostList.size();
    }

}