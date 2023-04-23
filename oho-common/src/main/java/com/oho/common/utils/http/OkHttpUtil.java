package com.oho.common.utils.http;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;


/**
 * OKHttp工具类
 *
 * @author MENGJIAO
 * @createDate 2023-04-23 16:48
 */
public class OkHttpUtil {

    private final OkHttpClient client;
    /**
     * MediaType
     */
    private final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

    public OkHttpUtil() {
        client = new OkHttpClient();
    }

    /**
     * GET请求
     *
     * @param url 请求地址
     * @return ResponseBody字符串
     */
    public String doGet(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return returnResult(request);
    }

    /**
     * POST请求
     *
     * @param url  请求地址
     * @param json 请求数据
     * @return ResponseBody字符串
     */
    public String doPost(String url, String json) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.Companion.create(json, mediaType))
                .build();
        return returnResult(request);
    }

    /**
     * 带Header的GET请求
     *
     * @param url         请求地址
     * @param headerName  Header名字
     * @param headerValue Header值
     * @return ResponseBody字符串
     */
    public String doGetWithHeader(String url, String headerName, String headerValue) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header(headerName, headerValue)
                .build();
        return returnResult(request);
    }

    /**
     * 带多个Header的GET请求
     *
     * @param url     请求地址
     * @param headers Header对
     * @return ResponseBody字符串
     */
    public String doGetWithHeaders(String url, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url);
        return builderWithHeaders(headers, builder);
    }

    /**
     * 带Header的POST请求
     *
     * @param url         请求地址
     * @param json        请求数据
     * @param headerName  Header名字
     * @param headerValue Header值
     * @return ResponseBody字符串
     */
    public String doPostWithHeader(String url, String json, String headerName, String headerValue) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header(headerName, headerValue)
                .post(RequestBody.Companion.create(json, mediaType))
                .build();
        return returnResult(request);
    }

    /**
     * 带多个Header的POST请求
     *
     * @param url     请求地址
     * @param json    请求数据
     * @param headers Header 对
     * @return ResponseBody字符串
     */
    public String doPostWithHeaders(String url, String json, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(RequestBody.Companion.create(json, mediaType));
        return builderWithHeaders(headers, builder);
    }

    @NotNull
    private String returnResult(Request request) throws IOException {
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            if (response.body() != null) {
                return response.body().string();
            } else {
                return "";
            }
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    @NotNull
    private String builderWithHeaders(Map<String, String> headers, Request.Builder builder) throws IOException {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
        }
        Request request = builder.build();
        return returnResult(request);
    }
}
