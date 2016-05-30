package com.jiakaoshiti;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hexing on 16-5-25.
 */
public class ShiTi {
    public static void writeTofile(Element root,String fileAddress){
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            Writer fileWriter = new FileWriter(fileAddress);
            XMLWriter xmlWriter = new XMLWriter(fileWriter,format);
            xmlWriter.write(root);
            xmlWriter.close();
            System.out.println("写入成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeImgToFile(String address,int j) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet getimg = new HttpGet(address);
        CloseableHttpResponse responseimg = httpClient.execute(getimg);
        HttpEntity httpEntity = responseimg.getEntity();
        if(address.indexOf("jpg")!=-1){
             address = "/home/hexing/试题/小车科目一/道路交通安全法律图片/"+j+".jpg";
        }else if(address.indexOf("gif")!=-1){
            address = "/home/hexing/试题/小车科目一/道路交通安全法律图片/"+j+".gif";
        }

        File imgFile = new File(address);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(imgFile);
            if(httpEntity != null){
                InputStream inputStream = httpEntity.getContent();
                byte[] b = new byte[1024];
                int i;
                while ((i = inputStream.read(b))!= -1){
                    fileOutputStream.write(b,0,i);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
      //  System.out.println("OK, 下载完成!!");
    }

    public static void main(String[] args) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        //正则匹配试题，答案解析等内容
        Pattern h1 = Pattern.compile("<h1>试题：(.*?)</h1>",Pattern.DOTALL);
        Pattern img = Pattern.compile("<li class=\"file\"><img src=\"(.*?)\" /></li>",Pattern.DOTALL);
        Pattern a = Pattern.compile("<li>A.(.*?)</li>",Pattern.DOTALL);
        Pattern b = Pattern.compile("<li>B.(.*?)</li>",Pattern.DOTALL);
        Pattern c = Pattern.compile("<li>C.(.*?)</li>",Pattern.DOTALL);
        Pattern d = Pattern.compile("<li>D.(.*?)</li>",Pattern.DOTALL);
        Pattern answer = Pattern.compile("<li class=\"desc\">正确答案：<strong>(.*?)</strong>");
        Pattern explain = Pattern.compile("<li class=\"explain\">试题解释：(.*?)</li>",Pattern.DOTALL);
        Matcher m = null;

        //生成xml
        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement("root");
        int j = 94;
        //循环获取信息
        for(int i = 4918; i <= 5018; i++){
            String address = "http://www.jkstk.com/tiku/"+i+".html";
            HttpGet get = new HttpGet("http://www.jkstk.com/tiku/"+i+".html");
            String str = null;
            String title = null,imgs = null,answer_a = null,answer_b = null,answer_c = null,answer_d = null,answers = null,explains = null;
            try {
                str = EntityUtils.toString(client.execute(get).getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Element shitielement = rootElement.addElement("shiti");
            m = h1.matcher(str);
            while (m.find()){
                title = m.group(1);
             //   System.out.println("试题："+title);
            }
            m = img.matcher(str);
            while (m.find()){
                imgs = m.group(1);
                writeImgToFile(imgs,j);
                if(imgs.indexOf("jpg")!=-1){
                    imgs = "/home/hexing/试题/小车科目一/道路交通安全法律图片/"+j+".jpg";
                }else if(imgs.indexOf("gif")!=-1){
                    imgs = "/home/hexing/试题/小车科目一/道路交通安全法律图片/"+j+".gif";
                }
                System.out.println(imgs);
                j++;
            }
            m = a.matcher(str);
            while (m.find()){
                answer_a = m.group(1);
                //System.out.println("A:"+answer_a);
            }
            m = b.matcher(str);
            while (m.find()){
                answer_b = m.group(1);
                //System.out.println("B:"+answer_b);
            }
            m = c.matcher(str);
            while (m.find()){
                answer_c = m.group(1);
                //System.out.println("C:"+answer_c);
            }
            m = d.matcher(str);
            while (m.find()){
                answer_d = m.group(1);
                //System.out.println("D:"+answer_d);
            }
            m = answer.matcher(str);
            while (m.find()){
                answers = m.group(1);
                //System.out.println("答案："+answers);
            }
            m = explain.matcher(str);
            while (m.find()){
                explains = m.group(1);
                if(explains.length()>=200){
                    explains = null;
                }
                //System.out.println("解析："+explains);
            }
            shitielement.addElement("address").setText(address + " ");
            shitielement.addElement("title").setText(title+" ");
            shitielement.addElement("img").setText(imgs+" ");
            shitielement.addElement("answer_a").setText(answer_a+" ");
            shitielement.addElement("answer_b").setText(answer_b+" ");
            shitielement.addElement("answer_c").setText(answer_c+" ");
            shitielement.addElement("answer_d").setText(answer_d+" ");
            shitielement.addElement("answer").setText(answers+" ");
            shitielement.addElement("explain").setText(explains+" ");
        }
        writeTofile(rootElement, "/home/hexing/试题/小车科目一/道路交通安全法律1.xml");
    }
}
