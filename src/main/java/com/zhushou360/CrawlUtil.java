package com.zhushou360;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hexing on 15-11-11.
 */
public class CrawlUtil {
    //获取同类软件中前多少个的appId
    public static Set<String> getAppIds(String classAddr,int limit) throws IOException {
        int i = 0;
        Set<String> listId = new HashSet<String>();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(classAddr);
        CloseableHttpResponse response = httpclient.execute(httpget);
        String content = EntityUtils.toString(response.getEntity());
        Pattern pattern = Pattern.compile("sid=\"(\\w*?)\"");
        Matcher matcher = pattern.matcher(content);
        if(matcher.find()){
            do{
                if(i <= (limit-1)*3){
                    listId.add(matcher.group(1));
                    i++;
                }
            }while (matcher.find());
        }
        return listId;
    }

    //获取各个App的名字
    public static String getAppName(String id) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet("http://zhushou.360.cn/detail/index/soft_id/" + id);
        CloseableHttpResponse response = client.execute(get);
        return EntityUtils.toString(response.getEntity()).split("baikeName = '")[1].split("'")[0].replaceAll(" ","+");
    }

    //获取各个App的评论数
    public static int getCommentCount(String id,String name) throws IOException, JSONException {
        CloseableHttpClient client = HttpClients.createDefault();
      //  System.out.println("http://intf.baike.360.cn/index.php?name="+name+"&c=message&a=getmessage&start=0&count=10");
        HttpGet httpGet = new HttpGet("http://intf.baike.360.cn/index.php?name="+name+"&c=message&a=getmessage&start=10&count=10");
        CloseableHttpResponse response = client.execute(httpGet);
        String json = EntityUtils.toString(response.getEntity());
        //  System.out.println(json);
        JSONObject jsonObject = new JSONObject(json);
        int count = jsonObject.getJSONObject("data").getInt("total");
        return count;
    }

    //把每个软件的评论写入文件
    public static void WtiteXmlToFile(Element app,String fileName) throws IOException {
        Writer fileWriter = new FileWriter(fileName);
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter xmlWriter = new XMLWriter(fileWriter,format);
        xmlWriter.write(app);
        System.out.println(app);
        xmlWriter.close();
    }
}
