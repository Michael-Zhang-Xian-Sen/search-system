package nju.software.ocr.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import nju.software.ocr.model.Ocr;
import nju.software.ocr.model.OcrRoot;
import org.apache.http.HttpHost;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ReadFile {

    /*
        从单个文件中读取文件，并以字符串的形式返回文件内容
     */
    public static String readSingleFile(File file){
        StringBuilder res = new StringBuilder();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file));
            // 流不为空，则继续读取
            while(br.ready()){
                //这里可以作相关的处理过程 #todo your code#
                res.append(br.readLine() + "\n");
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res.toString();
    }

    /*
        读取path路径下的所有文件,并将文件存储至Elastic Search数据库
     */
    public static void readDirFile(String path) throws IOException {
        System.out.println("准备建立连接！");
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("peer1", 9200, "http")));
        System.out.println("建立连接成功！");

        // 获取文件夹下的所有文件
        LinkedList<File> dirlist = new LinkedList<File>();
        LinkedList<String> fileNameList = new LinkedList<String>();
        File dir = new File(path);
        File[] files = dir.listFiles();
        String virtualDir = "F:\\ocrjpg\\";

        // 获取所有的文件夹及文件夹名称
        for(File file : files){
            // 获取所有的文件夹
            dirlist.add(file);
            // 获取文件夹的名称
            fileNameList.add(file.getName());
            System.out.println(file.getName());
        }
        System.out.println();
        System.out.println();

        File temp;
        while(!dirlist.isEmpty()){
            // 取第一个文件夹
            temp = dirlist.removeFirst();
            files = temp.listFiles();
            for(File file : files){
                String type = getFileType(file.getName());
                if(type.equals("jpg")){
                    // 处理图片
                    System.out.println("拷贝jpg图片至虚拟目录！");
                    // 将图片存储至虚拟目录
                    copyFile(file.getAbsoluteFile(),new File(virtualDir+file.getName()));
                }else if(type.equals("json")){
                    // 处理json数据
                    // 在json数据末尾处加上文件名称
                    JSONObject jsonObject = JSON.parseObject(readSingleFile(file));
//                    System.out.println(readSingleFile(file));

                    // 处理filename
                    String tempFileName = file.getName();
                    int cutPosition = tempFileName.indexOf(".");
                    tempFileName = tempFileName.substring(0,cutPosition);

                    // 构建即将插入至数据库的对象
                    OcrRoot ocrRootTemp = new OcrRoot();
                    ocrRootTemp.setOcrText(""+jsonObject.get("ocrText"));
                    ocrRootTemp.setPdfURL(""+jsonObject.get("pdfURL"));
                    ocrRootTemp.setTextResult((List<Ocr>)jsonObject.get("textResult"));
                    ocrRootTemp.setFileName(tempFileName);


                    // 获取json字符串
                    System.out.println("tempStr is:"+ocrRootTemp.toString());
                    String tempJson = JSONObject.toJSONString(ocrRootTemp);
                    System.out.println("tempJson is :" + tempJson);

                    // 获取数据
                    IndexRequest request = new IndexRequest("ocr","_doc");
                    request.source(tempJson, XContentType.JSON);
                    IndexResponse indexResponse = client.index(request,RequestOptions.DEFAULT);

                    // 获取响应数据
                    String index = indexResponse.getIndex();
                    String id = indexResponse.getId();

                    System.out.println("index is :"+index);
                    System.out.println("id is:"+id);
                }
            }
        }

        /**
         * 关闭连接
         */
        System.out.println("准备关闭连接！");
        client.close();
        System.out.println("关闭连接成功！");
    }

    public List<Ocr> getListItem(JSONObject jsonObject){
        List<Ocr> listOcr = new ArrayList<Ocr>();

        // 获取Url
        jsonObject.get("pdf");

        return listOcr;
    }

    public static void copyFile(File sourceFile,File targetFile) throws IOException {
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff=new BufferedInputStream(input);

        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff=new BufferedOutputStream(output);

        // 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len =inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
        }

        // 刷新此缓冲的输出流
        outBuff.flush();

        //关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }

    /**
     * 获取文件名后缀
     * @param filename
     * @return
     */
    public static String getFileType(String filename){
        int pos = filename.lastIndexOf(".");
        if(pos == -1){
            return null;
        }
        return filename.substring(pos+1);
    }
}
