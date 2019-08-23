package com.android.webkit.uautils;

import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class WebViewSettingsUtils {
    private static String defaultUserAgent;

    private static String getDefaultUserAgent(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                return WebSettings.getDefaultUserAgent(context);
            } catch (Exception ignored) {
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) {
            try {
                final Class<?> webSettingsClassicClass = Class.forName("android.webkit.WebSettingsClassic");
                final Class<?> webViewClassicClass = Class.forName("android.webkit.WebViewClassic");
                final Constructor<?> constructor = webSettingsClassicClass.getDeclaredConstructor(Context.class, webViewClassicClass);
                constructor.setAccessible(true);
                final Object object = constructor.newInstance(context, null);
                final Method method = webSettingsClassicClass.getMethod("getUserAgentString");
                return (String) method.invoke(object);
            } catch (Exception ignored) {
            }
        } else {
            try {
                Constructor<WebSettings> constructor = WebSettings.class.getDeclaredConstructor(Context.class, WebView.class);
                constructor.setAccessible(true);
                WebSettings settings = constructor.newInstance(context, null);
                return settings.getUserAgentString();
            } catch (Exception ignored) {
            }
        }
        try {
            WebView webView = new WebView(context);
            WebSettings webSettings = webView.getSettings();
            return webSettings.getUserAgentString();
        } catch (Exception ignored) {

        }
        return System.getProperty("http.agent");
    }

    public static String getUserAgent() {
        if (defaultUserAgent == null) {
            synchronized (WebViewSettingsUtils.class) {
                if (defaultUserAgent == null) {
                    try {
                        Class<?> ActivityThread = Class.forName("android.app.ActivityThread");
                        Method currentActivityThread = ActivityThread.getMethod("currentActivityThread");
                        Object activityThread = currentActivityThread.invoke(null);
                        Method getApplication = ActivityThread.getMethod("getApplication");
                        Context context = (Context) getApplication.invoke(activityThread);
                        defaultUserAgent = getDefaultUserAgent(context);
                    } catch (Exception e) {
                    }
                }
            }
        }
        return defaultUserAgent;
    }
}
