package com.volkdem.ecashier.officer.communication.request;

/**
 * Created by Evgeny on 19.06.2016.
 */

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Evgeny on 19.06.2016.
 */
public abstract class CustomRequst<A> extends Request<A> {
    private Response.Listener<A> successListener = null;
    private ObjectMapper mapper = new ObjectMapper();
    private Class<A> classOfA;
    private TypeReference<A> typeReference;


    public CustomRequst(Class<A> classOfA, int method, String url, Module module, Response.Listener<A> successlistener, Response.ErrorListener errorlistener) {
        super(method, url, errorlistener );

        this.successListener = successlistener;
        this.classOfA = classOfA;

        if( module != null ) {
            mapper.registerModule(module);
        }
    }

    public CustomRequst(TypeReference<A> typeReference, int method, String url, Module module, Response.Listener<A> successlistener, Response.ErrorListener errorlistener) {
        super(method, url, errorlistener );

        this.successListener = successlistener;
        this.typeReference= typeReference;

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
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            if( typeReference != null) {
                answer = mapper.readValue(json, typeReference);
            } else {
                answer = mapper.readValue(json, classOfA );
            }
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
