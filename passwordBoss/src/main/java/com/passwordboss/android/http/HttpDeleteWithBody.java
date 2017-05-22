package com.passwordboss.android.http;

import org.apache.http.client.methods.HttpPost;

class HttpDeleteWithBody extends HttpPost {
    public HttpDeleteWithBody(String url){
        super(url);
    }
    @Override
    public String getMethod() {
        return "DELETE";
    }
}