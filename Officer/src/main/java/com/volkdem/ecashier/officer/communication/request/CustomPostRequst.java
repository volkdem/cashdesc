package com.volkdem.ecashier.officer.communication.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.Module;

/**
 * Created by Evgeny on 19.06.2016.
 */
// TODO: put it into the separate Android library
public class CustomPostRequst<R,A> extends CustomRequst<A> {
    private R requestObject;


    public CustomPostRequst(Class<A> classOfA, String url, R requestObject, Module module, Response.Listener<A> successlistener, Response.ErrorListener errorlistener) {
        super(classOfA, Request.Method.POST, url, module, successlistener, errorlistener);
        this.requestObject = requestObject;
    }

    public CustomPostRequst(Class<A> classOfA, String url, R requestObject, Response.Listener<A> successlistener, Response.ErrorListener errorlistener) {
        this(classOfA, url, requestObject, null, successlistener, errorlistener);
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return getMapper().writeValueAsBytes( requestObject );
        } catch (JsonProcessingException e) {
            throw new AuthFailureError( e.getMessage(), e );
        }
    }
}
