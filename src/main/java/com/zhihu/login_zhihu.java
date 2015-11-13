package com.zhihu;

import org.apache.http.Consts;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hexing on 15-5-28.
 */
public class login_zhihu {
    public static void main(String[] args) throws IOException {
        //选择cookies策略
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        HttpGet httpGet = new HttpGet("http://www.zhihu.com/");
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String responseHtml = EntityUtils.toString(response.getEntity());
            String xsrfValue = responseHtml.split("<input type=\"hidden\" name=\"_xsrf\" value=\"")[1].split("\"/>")[0];
            System.out.println("xsrfValue:" + xsrfValue);
            response.close();

            //存储要提交的<input>里面的参数
            List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
            formparams.add(new BasicNameValuePair("_xsrf", xsrfValue));
            formparams.add(new BasicNameValuePair("email", "1776898728@qq.com"));
            formparams.add(new BasicNameValuePair("password", "hx106107"));
            formparams.add(new BasicNameValuePair("rememberme", "y"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
            HttpPost httpPost = new HttpPost("http://www.zhihu.com/login");
            httpPost.setEntity(entity);
            //判断是否登录成功
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost);   //登录
            System.out.println(closeableHttpResponse.getStatusLine().toString());
            closeableHttpResponse.close();

            //获得想要访问的下一页的信息
            HttpGet get = new HttpGet("http://www.zhihu.com/topic");
            CloseableHttpResponse r = httpClient.execute(get);
            System.out.println(EntityUtils.toString(r.getEntity()));
            r.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}