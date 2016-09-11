package com.garfield.weishu.http.volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UrlBuilder {
    private String baseUrl = "";
    private List<String> paths = new ArrayList();
    private Map<String, String> queryParameters = new HashMap();

    public static UrlBuilder newBuilder(String baseUrl) {
        if(baseUrl == null) {
            throw new NullPointerException("baseUrl is Null");
        } else {
            return new UrlBuilder(baseUrl);
        }
    }

    private UrlBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public UrlBuilder appendPath(String path) {
        this.paths.add(path);
        return this;
    }

    public UrlBuilder addQueryParameter(String name, String value) {
        this.queryParameters.put(name, value);
        return this;
    }

    public UrlBuilder addQueryParameters(Map<String, String> queryParameters) {
        this.queryParameters.putAll(queryParameters);
        return this;
    }

    public String build() {
        StringBuilder url = new StringBuilder(this.baseUrl);

        String path;
        for(Iterator separator = this.paths.iterator(); separator.hasNext(); url.append(path)) {
            path = (String)separator.next();
            if(url.charAt(url.length() - 1) != 47 && path.charAt(0) != 47) {
                url.append('/');
            }
        }

        if(this.queryParameters != null && this.queryParameters.size() > 0) {
            char separator1 = 63;

            for(Iterator path1 = this.queryParameters.entrySet().iterator(); path1.hasNext(); separator1 = 38) {
                Map.Entry entry = (Map.Entry)path1.next();
                String name = (String)entry.getKey();
                String value = (String)entry.getValue();

                try {
                    url.append(separator1).append(name).append('=').append(URLEncoder.encode(value, "utf-8"));
                } catch (UnsupportedEncodingException var8) {
                    ;
                }
            }
        }

        return url.toString();
    }
}
