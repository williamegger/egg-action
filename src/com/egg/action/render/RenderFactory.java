package com.egg.action.render;

public class RenderFactory {

    public static Render getHtmlRender(String html) {
        return new HtmlRender(html);
    }

    public static Render getTextRender(String text) {
        return new TextRender(text);
    }

    public static Render getVelocityRender(String view) {
        return new VelocityRender(view);
    }

}
