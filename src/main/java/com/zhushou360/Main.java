package com.zhushou360;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONException;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hexing on 15-11-10.
 */

public class Main {
    public static void main(String[] args) throws IOException, JSONException, InterruptedException {
        //limit存储抓取的同类软件的前多少个
        //class存储要抓取的软件类型的网址
        int limit = 0;
        String classAddr = null;
        Set<String> listId = null;

        System.out.println("请输入软件类型的网址和需要抓取同类软件的个数:");
        Scanner in = new Scanner(System.in);
        classAddr = in.next();
        limit = in.nextInt();
        listId = CrawlUtil.getAppIds(classAddr,limit);
//        System.out.println(listId);
        for(String id:listId){
            //建立线程池
            ExecutorService executorService = Executors.newFixedThreadPool(50);
            //建立根节点
            Document document = DocumentHelper.createDocument();
            Element app = document.addElement("app");
            //添加名字结点
            String name = CrawlUtil.getAppName(id);
//            System.out.println(name);
            app.addElement("appname").setText(name);
            int allcommentscount = 0;
            try {
                allcommentscount = CrawlUtil.getCommentCount(id,name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            System.out.println(allcommentscount);
            app.addElement("allcomments").setText(String.valueOf(allcommentscount));
            int count = 20;
            for(int start = 0;start < allcommentscount;start += count){
                if(start+count>allcommentscount){
                    count = allcommentscount-start;
                }
               executorService.submit(new CrawlComments(app, start, count, name));
            }
            executorService.shutdown();
            while (!executorService.isTerminated()) {
                Thread.sleep(1000);
            }
            CrawlUtil.WtiteXmlToFile(app, "/home/hexing/comments/" + name.split("\\+")[0] + ".xml");
        }
    }
}
