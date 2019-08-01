package nju.software.ocr;

import nju.software.ocr.util.ReadFile;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@SpringBootApplication
public class ElasticSearch {

    public static void main(String[] args) throws IOException {
        // 将jpg图片存储至虚拟目录、将json数据存储至Elastic Searcsh数据库


//        System.out.println("开始保存数据");
//        ReadFile readFile = new ReadFile();
//        String path = "C:\\Users\\Himory\\Desktop\\天津组\\作业二描述\\电子卷宗智能检索系统任务要求\\电子卷宗OCR";
//        readFile.readDirFile(path);
//        System.out.println("保存数据成功！");


        // 运行spring
        SpringApplication.run(ElasticSearch.class,args);



//
//
//        System.out.println("准备建立连接！");
//        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("peer1", 9200, "http")));
//        System.out.println("建立连接成功！");
//
//        // 获取数据
//        GetRequest getRequest = new GetRequest("ocr","_doc","1");
//        getRequest.fetchSourceContext();
//
//        // 获取响应数据
//        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
//        System.out.println("getResponse的内容为："+getResponse);
//
//        /**
//         * 关闭连接
//         */
//        System.out.println("准备关闭连接！");
//        client.close();
//        System.out.println("关闭连接成功！");
    }
}
