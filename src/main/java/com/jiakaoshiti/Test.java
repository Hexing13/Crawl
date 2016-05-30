package com.jiakaoshiti;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hexing on 16-5-25.
 */
public class Test {
        public static void main(String[] args) {

        String s = "";
        Pattern h1 = Pattern.compile("<h1>试题：(.*?)</h1>");
        Pattern img = Pattern.compile("<li class=\"file\"><img src=\"(.*?)\" /></li>",Pattern.DOTALL);
        Pattern a = Pattern.compile("<li>A.(.*?)</li>",Pattern.DOTALL);
        Pattern b = Pattern.compile("<li>B.(.*?)</li>",Pattern.DOTALL);
        Pattern c = Pattern.compile("<li>C.(.*?)</li>",Pattern.DOTALL);
        Pattern d = Pattern.compile("<li>D.(.*?)</li>",Pattern.DOTALL);
        Pattern answer = Pattern.compile("<li class=\"desc\">正确答案：<strong>(.*?)</strong>");
        Pattern explain = Pattern.compile("<li class=\"explain\">(.*?)</li>",Pattern.DOTALL);
        Matcher m = explain.matcher(s);
        while (m.find()){
                String ss = m.group(1);
                System.out.println("长度:"+ss.length());
                System.out.println(ss);

        }
        }

}
