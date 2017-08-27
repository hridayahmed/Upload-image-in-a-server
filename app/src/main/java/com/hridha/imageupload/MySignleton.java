package com.hridha.imageupload;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by hrida on 8/10/2017.
 */

public class MySignleton {
    private static MySignleton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private MySignleton(Context context){
        mCtx = context;
        requestQueue= getRequestQueue();
    }

    private RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySignleton getmInstance(Context context){
        if (mInstance==null){
            mInstance = new MySignleton(context);
        }
        return mInstance;
    }

    public<T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }
}
