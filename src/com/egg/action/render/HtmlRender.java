package com.egg.action.render;

public class HtmlRender extends TextRender {

    private static final String contentType = "text/html";

    public HtmlRender(String html) {
        super(html, contentType);
    }


}
