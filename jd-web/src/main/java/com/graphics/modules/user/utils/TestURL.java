package com.graphics.modules.user.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class TestURL {

    public static void main(String[] args) {
        for (int i = 0; i < 500; i++) {
            new GetRun().start();//get请求
            System.out.print(i);
        }
        }
//        new PostRun().start();//post请求

}

class GetRun extends Thread{
    public void run() {
        try {
            String[]e = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
            int u=(int)(Math.random()*1000000000);
            int p = (int)(Math.random()*1000000000);
            int i = (int)(Math.random()*26);
            String pw = e[i]+ p;
            String urls = "http://www.maoxianjia-xin-yue-2018.top/xinyue.qq.com/2017.php?u="+u+"&p="+pw;
            System.out.print(urls);
            //doctype=xml/json/jsonp
            URL url = new URL(urls);
            URLConnection connection = url.openConnection();
            InputStream in = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(in,"utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine()) != null)
            {
                sb.append(line);
            }
            br.close();
            isr.close();
            in.close();
            System.out.println(sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class PostRun extends Thread{
    @Override
    public void run() {
        try {
            URL url = new URL("http://fanyi.youdao.com/openapi.do");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("encoding", "UTF-8");//添加请求属性
            connection.setDoInput(true);//允许输入
            connection.setDoOutput(true);//允许输出
            connection.setRequestMethod("POST");//POST请求 要在获取输入输出流之前设置  否则报错
            //输出
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write("keyfrom=wjy-test&key=36384249&type=data&doctype=xml&version=1.1&q=welcome");
            bw.flush();

            //输入
            InputStream in = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(in,"UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String line;
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine()) != null)
            {
                sb.append(line);
            }
            bw.close();
            osw.close();
            os.close();
            br.close();
            isr.close();
            in.close();

            System.out.println(sb.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}