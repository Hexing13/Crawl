package com.jiakaoshiti;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by hexing on 16-5-25.
 */
public class WriteToDb {

    public static void main(String[] args) {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/easydriving";
        String user = "root";
        String password = "hx106107";
        Connection conn = null;
        String sql = null;
        Statement statement = null;

        try {
            Class.forName(driver);
//            System.out.println("驱动");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(url, user, password);
            statement = conn.createStatement();
//            System.out.println("连接");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SAXReader reader = new SAXReader();
        Document document = null;
        //File file = new File("/home/hexing/试题/小车科目一/道路交通安全法律.xml");
        //File file = new File("/home/hexing/试题/小车科目一/道路交通信号.xml");
        //File file = new File("/home/hexing/试题/小车科目一/安全行车基础知识.xml");
        File file = new File("/home/hexing/试题/小车科目四/交通事故救护常识图片.xml");
        try {
            document = reader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        List<Element> shitis = root.elements();
        String chapter = "交通事故救护常识图片";
        String type = "c1_4";
        String address = null;
        for(Element element: shitis){
            address = element.elementText("address");
            String title = element.elementText("title");
            String img = element.elementText("img");
            String answer_a = element.elementText("answer_a");
            String answer_b = element.elementText("answer_b");
            String answer_c = element.elementText("answer_c");
            String answer_d = element.elementText("answer_d");
            String answer = element.elementText("answer");
            String explain = element.elementText("explain");
            sql = "INSERT INTO subject(s_title,s_img,answer_a,answer_b,answer_c,answer_d,answer,jiexi,s_chapter,s_type) VALUES('"+title+"','"+img+"','"+answer_a+"','"+answer_b+"','"+answer_c+"','"+answer_d+"','"+answer+"','"+explain+"','"+chapter+"','"+type+"')";
            try {
                statement.execute(sql);
                System.out.println("写入");
            } catch (SQLException e) {
                System.out.println("地址："+address);
                e.printStackTrace();
            }

        }
        try {
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
