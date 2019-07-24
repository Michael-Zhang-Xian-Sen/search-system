package nju.software.ocr.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonUtils {

    // 接收数据
    public static void ajaxJson(String jsonString, HttpServletResponse response) throws ServletException, IOException {
        ajax(jsonString,"application/json",response);
    }

    public static void ajax(String content,String type,HttpServletResponse response){
        try{
            response.setContentType(type+";charset=utf-8");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control","no-cache");
            response.setDateHeader("Expires",0);
            response.getWriter().write(content);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}