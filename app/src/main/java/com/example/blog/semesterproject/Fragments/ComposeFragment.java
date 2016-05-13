package com.example.blog.semesterproject.Fragments;

import android.app.Activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blog.semesterproject.MainActivity;
import com.example.blog.semesterproject.R;
import com.example.blog.semesterproject.Utils.AjaxHelper;

import org.w3c.dom.Text;

import java.sql.Blob;
import java.util.ArrayList;

/**
 * Created by nicolaiharbo on 09/05/2016.
 */
public class ComposeFragment extends Fragment {

    EditText title;
    EditText content;
    Bitmap coverimage = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public static ComposeFragment newInstance() {
        ComposeFragment fragment = new ComposeFragment();
        return fragment;
    }

    public ComposeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_compose, container, false);

        final Activity activity = (Activity) view.getContext();

        final Button takePhotoButton = (Button) view.findViewById(R.id.addPhotoButton);
        final Button postButton = (Button) view.findViewById(R.id.postButton);
        title = (EditText) view.findViewById(R.id.titleInput);
        content = (EditText) view.findViewById(R.id.contentInput);


        //Tag et billede, og gem det i en variabel
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }


                Blob coverimage = null;
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(activity, "Button clicked", Toast.LENGTH_SHORT).show();

                String author = "android"; //OBS! Skal hentes fra telefonens memory, gemt efter logind!
                String titleString = title.getText().toString();
                String contentString = content.getText().toString();

                AjaxHelper ah = new AjaxHelper(activity);
                ah.addNewPost(coverimage, author, titleString, contentString);
                //Lav snackbar som kommer op, når posten er sendt!
            }

        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.w("INSIDE ", "onActivityResult");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            coverimage = (Bitmap) extras.get("data");
            Log.w("bitmap: ", coverimage.toString());
        }
    }
}
