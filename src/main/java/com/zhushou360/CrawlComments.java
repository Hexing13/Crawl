package com.zhushou360;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by hexing on 15-11-11.
 */
public class CrawlComments implements Runnable{
    private Element app;
    private String start;
    private String count;
    private String name;

    public CrawlComments(Element app, int start,int count,String name) {
        this.app = app;
        this.start = String.valueOf(start);
        this.name = name;
        this.count = String.valueOf(count);
    }

    public void getComments() throws JSONException, IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet("http://intf.baike.360.cn/index.php?name="+name+"&c=message&a=getmessage&start="+start+"&count="+count);
        String contentJson = null;
        try {
            contentJson = EntityUtils.toString(client.execute(get).getEntity());
            if(contentJson.startsWith("<html> ")){
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray contentJsonArray = null;
        try {
            contentJsonArray = new JSONObject(contentJson).getJSONObject("data").getJSONArray("messages");
        } catch (JSONException e) {
            System.out.println(name+"软件"+start+"错误");
            System.out.println(contentJson);
            e.printStackTrace();
        }
        System.out.println(contentJsonArray.length());
        for(int i = 0; i < contentJsonArray.length();i++){
            JSONObject messageJsonObject = contentJsonArray.getJSONObject(i);

            String userid = messageJsonObject.getString("username");
            String time = messageJsonObject.getString("create_time");
            String score = String.valueOf(messageJsonObject.getInt("score"));
            String review = messageJsonObject.getString("content");

//            System.out.println(review);
            Element comment = app.addElement("comment");
            comment.addElement("userid").setText(userid);
            comment.addElement("time").setText(time);
            comment.addElement("score").setText(score);
            comment.addElement("review").setText(review);
        }
//        CrawlUtil.WtiteXmlToFile(app, "/home/hexing/comments/" + name.split("\\+")[0] + ".xml");
        client.close();
    }


    @Override
    public void run() {
        try {
            getComments();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
