package com.angelaavalos.pokedexls.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ParseError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.UnsupportedEncodingException;

public class GsonRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final TypeToken<T> type;
    private final Response.Listener<T> listener;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param method the HTTP method to use
     * @param url URL of the request to make
     * @param type Relevant class object, for Gson's reflection
     * @param listener is the listener for the correct response
     * @param errorListener is the listener for handling errors
     */
    public GsonRequest(int method, String url, TypeToken<T> type,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.type = type;
        this.listener = listener;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, type.getType()),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            // Catching general exception to handle other unforeseen parsing issues
            return Response.error(new ParseError(e));
        }
    }
}

