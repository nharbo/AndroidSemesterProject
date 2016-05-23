package com.example.blog.semesterproject.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by nicolaiharbo on 09/05/2016.
 */
public class AuthHelper {

    Context mContext;
    public static boolean authenticated;
    public static boolean signedUp;

    public AuthHelper(Context context) {
        this.mContext = context;
    }

    public void register(String username, String password) {

        String url = "http://blogbackend-nh127.rhcloud.com/register/";

        Map<String, String> params = new HashMap();
        params.put("email", username);
        params.put("password", password);

        JSONObject parameters = new JSONObject(params);

        Log.w("JSON: ", parameters.toString());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.w("REGISTER: ", "success!");
                Log.w("RESISTER RESPONSE:", "" + response);
                signedUp = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
                Log.w("REGISTER: ", "error! " + error.getMessage());
                signedUp = true;
            }
        });

        Volley.newRequestQueue(mContext).add(jsonRequest);

    }

    public void authenticate(String username, String password) {

        String url = "http://blogbackend-nh127.rhcloud.com/login/";

        Map<String, String> params = new HashMap();
        params.put("email", username);
        params.put("password", password);

        JSONObject parameters = new JSONObject(params);

        Log.w("LOGIN SENDING: ", parameters.toString());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.w("LOGIN: ", "success!");
                authenticated = true;
                Log.w("LOGIN RESPONSE:", "" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
                authenticated = false;
                Log.w("LOGIN: ", "error! " + error.getMessage());
            }
        });

        //Asynkront kald..
        Volley.newRequestQueue(mContext).add(jsonRequest);
    }

    public String getUsernameFromExternalStorage() {
        String res = "";

        try {
            InputStream inputStream = mContext.openFileInput("username");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                res = stringBuilder.toString();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean checkForUserInExternalStorage() {

        boolean exists = false;

        String[] files = mContext.fileList();
        for (int i = 0; i < files.length; i++) {
            Log.w("File:", files[i].toString());
            if (files[i].toString().equals("username")) {
                Log.w("'username' found", " in files");
                exists = true;
            }
        }
        return exists;
    }

}
