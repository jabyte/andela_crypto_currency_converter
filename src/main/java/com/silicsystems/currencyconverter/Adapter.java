package com.silicsystems.currencyconverter;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Adapter {
    private static Adapter adapter;
    private RequestQueue requestQueue;
    private static Context context;
    private JsonObjectRequest objectRequest;

    private Adapter(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized Adapter getInstance(Context context) {
        if (adapter == null) {
            adapter = new Adapter(context);
        }
        return adapter;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}