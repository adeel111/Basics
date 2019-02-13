package com.example.adeeliftikhar.mysqldatabase.singletonpkg;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton mySingletonClassReference;
    private RequestQueue requestQueue;
    private static Context context;

    private MySingleton(Context context){
        this.context = context;
//        Initializing the request queue by using getRequestQueue method...
        requestQueue = getRequestQueue();
    }

//    This method will return RequestQueue
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
//    This method will return the instance or object of class
//    Synchronized ==> synchronized keyword ensures that a method can be invoked by only one
//    thread at a time. At run time every class has an instance of a Class object. That is the
//    object that is locked by static synchronized methods.
    public static synchronized MySingleton getMySingletonClassObject(Context context){
        if(mySingletonClassReference == null){
            mySingletonClassReference = new MySingleton(context);
//            Now it is referring to an class object...
        }
        return mySingletonClassReference;
    }
//    This method will add the request to the RequestQueue...
    public <T>void addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }
}
