package com.egg.action.render;

import com.egg.common.log.LogKit;

import java.io.IOException;
import java.io.PrintWriter;

public class TextRender extends Render {

    private String contentType = "text/plain";
    private String text;

    public TextRender(String text) {
        this.text = text;
    }

    public TextRender(String text, String contentType) {
        this.text = text;
        this.contentType = contentType;
    }

    @Override
    public void render() throws IOException {
        PrintWriter writer = null;
        try {
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType(contentType);
            response.setCharacterEncoding(encode);
            writer = response.getWriter();
            writer.write(text);
            writer.flush();
        } catch (IOException e) {
            LogKit.error("render text error:", e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
