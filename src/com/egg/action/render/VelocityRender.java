package com.egg.action.render;

import com.egg.common.log.LogKit;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class VelocityRender extends Render {

    private static final String contentType = "text/html; charset=" + encode;
    private static final Properties props = new Properties();

    private static boolean notInit = true;
    private String view;

    public VelocityRender(String view) {
        this.view = view;
    }

    public static void init(ServletContext servletContext) {
        String webPath = servletContext.getRealPath("/");
        props.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, webPath);
        props.setProperty(Velocity.ENCODING_DEFAULT, encode);
        props.setProperty(Velocity.INPUT_ENCODING, encode);
        props.setProperty(Velocity.OUTPUT_ENCODING, encode);
    }

    public static void setProperty(String key, String value) {
        props.setProperty(key, value);
    }

    public static void setProperties(Properties properties) {
        Set<Map.Entry<Object, Object>> set = properties.entrySet();
        for (Iterator<Map.Entry<Object, Object>> it = set.iterator(); it.hasNext(); ) {
            Map.Entry<Object, Object> e = it.next();
            VelocityRender.props.put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void render() throws IOException {
        if (notInit) {
            Velocity.init(props);
            notInit = false;
        }

        PrintWriter writer = null;
        try {
            VelocityContext context = new VelocityContext();

            for (Enumeration<String> attrs = request.getAttributeNames(); attrs.hasMoreElements(); ) {
                String attrName = attrs.nextElement();
                context.put(attrName, request.getAttribute(attrName));
            }

            Template template = Velocity.getTemplate(view);

            response.setContentType(contentType);
            writer = response.getWriter();

            template.merge(context, writer);
            writer.flush();
        } catch (IOException e) {
            LogKit.error("render vm error:", e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
