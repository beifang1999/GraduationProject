package com.travel.producer.data.util;


import okhttp3.*;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class HttpUtil {

    private static OkHttpClient client;


    private HttpUtil() {

    }

    public static OkHttpClient getInstance() {
        if (client == null) {
            synchronized (HttpUtil.class) {
                if (client == null) {
                    client = new OkHttpClient();
                }
            }
        }
        return client;
    }

    public static void get(String json,String url) {
        String encodeJson = "";
        try {
            encodeJson = URLEncoder.encode(json, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
         url = url + "?param=" + encodeJson;
        Request request = null;
        try {
            request = new Request.Builder()
                    .url(url).get().build();
        }catch (Exception e){
            e.printStackTrace();
        }

        Call call = HttpUtil.getInstance().newCall(request);
        
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                throw new RuntimeException("发送失败...检查网络地址...");
            }

            @Override
            public void onResponse(Call call, Response response){

            }

        });


//        try {
//            call.execute();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("发送失败...检查网络地址...");
//        }
    }


//    public static void post(String json)  {
//          System.out.println(json);
//          org.springframework.web.bind.annotation.RequestBody requestBody = RequestBody.create(    MediaType.parse("application/json; charset=utf-8"),json     );
//          Request request = new Request.Builder()
//                    .url(AppConfig.mock_url)
//                    .post(requestBody) //post请求
//                .build();
//            Call call = HttpUtil.getInstance().newCall(request);
//          Response response = null;
//          long start = System.currentTimeMillis();
//          try {
//              response = call.execute();
//              long end = System.currentTimeMillis();
//              System.out.println(response.body().string()+" used:"+(end-start)+" ms");
//          } catch (IOException e) {
//              e.printStackTrace();
//              throw new RuntimeException("发送失败...检查网络地址...");
//
//          }
//
//         }
}
