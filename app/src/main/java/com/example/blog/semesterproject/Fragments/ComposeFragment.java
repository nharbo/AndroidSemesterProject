package com.example.blog.semesterproject.Fragments;

import android.app.Activity;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blog.semesterproject.MainActivity;
import com.example.blog.semesterproject.R;
import com.example.blog.semesterproject.Utils.AjaxHelper;
import com.example.blog.semesterproject.Utils.AuthHelper;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nicolaiharbo on 09/05/2016.
 */
public class ComposeFragment extends Fragment {

    EditText title;
    EditText content;
    Bitmap coverimage = null;
    //Bruges til genkendelse af onActivityResult
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_CHOOSE_PHOTO = 2;
    String mCurrentPhotoPath;
    View view;
    View blogListLayoutView;
    ImageView mImageView;
    String filePath;

    AuthHelper ah = new AuthHelper(getContext());

    public static ComposeFragment newInstance() {
        ComposeFragment fragment = new ComposeFragment();
        return fragment;
    }

    public ComposeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_compose, container, false);

        final Activity activity = (Activity) view.getContext();

        final Button takePhotoButton = (Button) view.findViewById(R.id.addPhotoButton);
        final Button choosePhotoButton = (Button) view.findViewById(R.id.choosePhotoButton);
        final Button postButton = (Button) view.findViewById(R.id.postButton);
        title = (EditText) view.findViewById(R.id.titleInput);
        content = (EditText) view.findViewById(R.id.contentInput);

        //Er ikke det rigtige imageview, men et med samme dimentioner, så billedet får den rigtige størrelse.
        mImageView = (ImageView) view.findViewById(R.id.imageViewSizeForListLayout);


        //Tag et billede, og sender det til "onActivityResult"
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        Log.w("photofile path: ", "" + photoFile.getAbsolutePath());
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.w("Error creating imgfile", " " + ex.getMessage());
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }

                }
            }
        });

        choosePhotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openGallery();
            }

        });

        //Ved klik på post-knappen
        postButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String author = MainActivity.username;
                Log.w("USERNAME IN NEW POST:", author);
                String titleString = title.getText().toString();
                String contentString = content.getText().toString();

                //Gemmer i db
                AjaxHelper ah = new AjaxHelper(activity);
                ah.addNewPost(coverimage, author, titleString, contentString, MainActivity.currentLatitude, MainActivity.currentLongitude);

                Toast.makeText(activity, "New post created!", Toast.LENGTH_SHORT).show();

                //Tager brugeren til My Posts fratmentet.
                MyPostsFragment mpf = new MyPostsFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title__my_posts));
                ft.replace(R.id.frame_content, mpf);
                ft.commit();
                ah.getMyPosts(author);

            }

        });

        return view;
    }

    //Henter billedet ind fra den Intent vi startede, og gemmer det i en variabel.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            //Saving image to SDcard
            galleryAddPic();

            //Start intent for opening gallery
            openGallery();

            //Open gallery for choosing image
        } else if (requestCode == REQUEST_CHOOSE_PHOTO && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();

            coverimage = BitmapFactory.decodeFile(filePath);
            Log.w("Image ", "selected!");

            //Coverimage scaleres billedet og gemmes i coverimage-variablen
            scaleCoverImageToFitView();

            //Posten sendes først når der klikkes på send!
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private void galleryAddPic() {
        //Gemmer billedet i en seperat Pictures-mappe på SD kortet.
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
        Log.w("Image saved to gallery", "");
    }

    private void openGallery() {
        //Åbner galleriet og afventer result - resultatet sendes til onActivityResults med requestcode
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_CHOOSE_PHOTO);
    }

    private void scaleCoverImageToFitView() {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        //Det nye billede gemmes i coverimage variablen
        coverimage = BitmapFactory.decodeFile(filePath, bmOptions);
        Toast.makeText(getContext(), "Image added to post!", Toast.LENGTH_SHORT).show();
    }

}
