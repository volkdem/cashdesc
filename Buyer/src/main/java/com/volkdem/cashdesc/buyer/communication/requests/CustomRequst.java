package com.volkdem.cashdesc.buyer.communication.requests;

/**
 * Created by Evgeny on 19.06.2016.
 */

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.common.jackson.ProductDeserializer;
import com.common.model.Order;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;



import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.common.jackson.ProductDeserializer;
import com.common.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Evgeny on 19.06.2016.
 */
// TODO: put it into the separate Android library
public abstract class CustomRequst<A> extends Request<A> {
    private Response.Listener<A> successListener = null;
    private ObjectMapper mapper = new ObjectMapper();
    public Class<A> classOfA;


    public CustomRequst(Class<A> classOfA, int method, String url, Module module, Response.Listener<A> successlistener, Response.ErrorListener errorlistener) {
        super(method, url, errorlistener );

        this.successListener = successlistener;
        this.classOfA = classOfA;

        if( module != null ) {
            mapper.registerModule(module);
        }
    }

    public CustomRequst(Class<A> classOfA, int method, String url, Response.Listener<A> successlistener, Response.ErrorListener errorlistener) {
        this(classOfA, method, url, null, successlistener, errorlistener );
    }

    @Override
    protected Response<A> parseNetworkResponse(NetworkResponse response) {
        A answer = null;
        try {
            String json = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            answer = mapper.readValue(json, classOfA );
        } catch (Exception e) {
            ParseError error = new ParseError( e);
            return Response.error(  error );
        }

        return Response.success( answer,
                HttpHeaderParser.parseCacheHeaders(response) );

    }

    @Override
    protected void deliverResponse(A response) {
        successListener.onResponse( response );
    }

    protected ObjectMapper getMapper() {
        return mapper;
    }
}
