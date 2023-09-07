package org.apache.coyote.http11;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestStartLine {
    private HttpMethod method;
    private String path;
    private Map<String, String> queryProperties;
    private String protocolVersion;

    private RequestStartLine(final HttpMethod method, final String path, final Map<String, String> queryProperties, final String protocolVersion) {
        this.method = method;
        this.path = path;
        this.queryProperties = queryProperties;
        this.protocolVersion = protocolVersion;
    }

    public static RequestStartLine from(final String startLine) {
        if (startLine == null) {
            return null;
        }
        final List<String> startLineTokens = List.of(startLine.split(" "));
        final HttpMethod method = HttpMethod.valueOf(startLineTokens.get(0));
        String path;
        Map<String, String> queryProperties;
        final String uri = startLineTokens.get(1);
        int uriSeparatorIndex = uri.indexOf("?");
        if (uriSeparatorIndex == -1) {
            path = uri;
            queryProperties = new HashMap<>();
        } else {// TODO: 2023/09/07 else 없애기
            path = uri.substring(0, uriSeparatorIndex);
            queryProperties = makeQueryProperties(uri.substring(uriSeparatorIndex + 1));
        }
        String protocolVersion = startLineTokens.get(2);
        return new RequestStartLine(method, path, queryProperties, protocolVersion);
    }

    private static Map<String, String> makeQueryProperties(final String queryString) {
        Map<String, String> result = new HashMap<>();
        String[] queryTokens = queryString.split("&");
        for (String queryToken : queryTokens) {
            int equalSeparatorIndex = queryToken.indexOf("=");
            if (equalSeparatorIndex != -1) {
                result.put(queryToken.substring(0, equalSeparatorIndex),
                        queryToken.substring(equalSeparatorIndex + 1));
            }
        }
        return result;
    }

    public boolean isPOST() {
        return this.method.equals(HttpMethod.POST);
    }

    public boolean isGET() {
        return this.method.equals(HttpMethod.GET);
    }

    public boolean isSamePath(String path) {
        return this.path.equals(path);
    }

    public String getPath() {// TODO: 2023/09/07 getter 없애기
        return this.path;
    }
}
