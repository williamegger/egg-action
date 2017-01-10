package com.egg.action.core;

import com.egg.action.handler.ActionHandler;
import com.egg.action.handler.Handler;
import com.egg.common.log.LogKit;
import com.egg.common.scan.ScanUtil;
import com.egg.common.utils.MethodUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public final class ActionMapping {

    private static final Map<String, Handler> actionHandlerMap = new HashMap<String, Handler>();

    // ----------------------
    // 扫描Action，并创建Handler
    // ----------------------

    /**
     * 扫描Action，并创建Handler
     */
    protected static void init(String scanPackage) {
        Set<Class<?>> actions = scanAction(scanPackage);
        if (actions == null || actions.isEmpty()) {
            return;
        }

        Map<String, Class<?>> actionClassMap = new HashMap<String, Class<?>>();
        String url;
        ActionAnno.Action anno;
        for (Class<?> clazz : actions) {
            anno = clazz.getAnnotation(ActionAnno.Action.class);
            url = getActionUrl(clazz, anno);
            if (actionClassMap.containsKey(url)) {
                LogKit.error("加载[" + clazz.getName() + "]时错误，与[" + actionClassMap.get(url).getName() + "]的URL设置重复。");
            } else {
                try {
                    actionClassMap.put(url, clazz);
                    loadActionMethods(url, clazz, anno);
                } catch (Exception e) {
                    LogKit.error(clazz.getName() + "加载失败", e);
                }
            }
        }
    }

    /**
     * 扫描Action
     * <p>
     * <pre>
     * 扫描使用{@link ActionAnno.Action Action}注解的类
     * </pre>
     */
    private static Set<Class<?>> scanAction(String scanPackage) {
        Set<Class<?>> actionSet = new HashSet<Class<?>>();
        try {
            List<Class<?>> classList = ScanUtil.scanByAnnotation(scanPackage, ActionAnno.Action.class);
            if (classList != null && !classList.isEmpty()) {
                actionSet.addAll(classList);
            }
        } catch (IOException e) {
            LogKit.error("扫描Action异常。", e);
        }
        return actionSet;
    }

    /**
     * 得到Action的URL
     * <p>
     * <pre>
     * 1.如果设置value值，则使用注解的value作为URL
     * 2.如果没设置value值，则将类名首字母小写，作为URL
     * </pre>
     */
    private static String getActionUrl(Class<?> clazz, ActionAnno.Action anno) {
        String path = "";

        if (anno != null) {
            path = anno.value().trim();
        }
        if (path.isEmpty()) {
            String simpleName = clazz.getSimpleName();
            path = simpleName.substring(0, 1).toLowerCase();
            if (simpleName.length() > 1) {
                path += simpleName.substring(1);
            }
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.endsWith("/")) {
            path += "/";
        }

        return path;
    }

    /**
     * 加载Action中的方法
     * <p>
     * <pre>
     * 1.方法必须是public void methodName()
     * 2.方法不会包括Object的equals(Object), hashCode(), toString()
     * </pre>
     */
    private static void loadActionMethods(String path, Class<?> actionClass, ActionAnno.Action anno) throws Exception {
        Object single = null;
        if (anno.isSingle()) {
            single = actionClass.newInstance();
        }

        Method[] methods = actionClass.getDeclaredMethods();
        if (methods == null || methods.length == 0) {
            return;
        }

        String methodName;
        ActionHandler handler;
        for (Method method : methods) {
            if (!(method.getModifiers() == Modifier.PUBLIC
                    && !MethodUtil.isEqualsMethod(method)
                    && !MethodUtil.isHashCodeMethod(method)
                    && !MethodUtil.isToStringMethod(method))) {
                continue;
            }

            methodName = method.getName();
            handler = new ActionHandler(actionClass, single, method);

            if (methodName.equals("index")) {
                actionHandlerMap.put(path, handler);
                LogKit.info("已加载[" + path + "] ---> [" + actionClass.getName() + "." + methodName + "()]");
            }
            actionHandlerMap.put(path + method.getName() + "/", handler);
            LogKit.info("已加载[" + path + method.getName() + "/" + "] ---> [" + actionClass.getName() + "." + methodName + "()]");
        }
    }

    // ----------------------
    // 得到ActionHandler
    // ----------------------

    /**
     * 得到ActoinHandler
     */
    public static Handler getActionHandler(HttpServletRequest req) {
        String key = getRequestPath(req);
        return actionHandlerMap.get(key);
    }

    /**
     * 得到请求的URL
     */
    private static String getRequestPath(HttpServletRequest req) {
        String path = req.getRequestURI();
        int contextPathLen = req.getContextPath().length();
        if (contextPathLen > 1) {
            path = path.substring(contextPathLen);
        }
        int index = path.indexOf(';');
        if (index != -1) {
            path = path.substring(0, index);
        }
        if (!path.endsWith("/")) {
			path += "/";
		}

        return path;
    }

}
