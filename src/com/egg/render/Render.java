package com.egg.render;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class Render {

    protected static final String encode = "UTF-8";
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    public Render setContent(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        return this;
    }

    public abstract void render() throws IOException;

}
