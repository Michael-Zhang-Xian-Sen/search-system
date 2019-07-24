package nju.software.ocr.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import nju.software.ocr.model.Status;
import nju.software.ocr.util.JsonUtils;
import org.apache.http.HttpHost;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  精准查询
 */
@RestController
public class AccurateSearchController {
    @RequestMapping(value = "accurateSearch", method = RequestMethod.POST)
    public void accurateSearch(@RequestParam("searchStr") String searchStr, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("peer1", 9200, "http")));

        // 建立搜索请求
        SearchRequest searchRequest = new SearchRequest("ocr");        // 建立"搜索请求"
        MatchPhraseQueryBuilder matchPhraseQueryBuilder = new MatchPhraseQueryBuilder("ocrText",searchStr);     // 建立针对ocrText字段的查询。
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();    // 建立"搜索内容"
        searchSourceBuilder.from(0);                                            // 分页从0开始
        searchSourceBuilder.size(100);                                          // 100的容量
        searchSourceBuilder.query(matchPhraseQueryBuilder);                     // 将"具体的查询"添加至搜索内容
        searchRequest.source(searchSourceBuilder);                              // 将"搜索内容"添加至"搜索请求"

        // 高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder();             // 创建高亮Builder
        highlightBuilder.numOfFragments(0);         // 设置切片为0，即不切片，返回字段的所有内容
        HighlightBuilder.Field highlightText = new HighlightBuilder.Field("ocrText");   // 为ocrText字段设置高亮
        highlightText.highlighterType("unified");   // 设置高亮类型为unified
        highlightBuilder.field(highlightText);      // 将HighlightBuilder.Field绑定至HighlightBuilder
        searchSourceBuilder.highlighter(highlightBuilder);  // 将highlightBuilder绑定至searchSourceBuilder

        //执行搜索请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // 获取响应信息
        RestStatus status = searchResponse.status();    // 获取请求的状态
        System.out.println("响应信息为："+status);

        //  如果请求失败，返回失败的信息
        if(!status.toString().equals("OK")){
            System.out.println("search failed!");

            // 向前端返回请求信息
            Status status1 = new Status("failed");
            JSONObject jsonObject = JSONObject.fromObject(status1);
            JsonUtils.ajaxJson(jsonObject.toString(), response);
        }

        // 显示命中信息
        SearchHits hits = searchResponse.getHits();
        System.out.println("命中信息的数量为："+hits.getTotalHits().value);
        List<String> searchList = new ArrayList<String>();  // 存储搜索结果。

        Map<String,Object> tempHit;

        // 遍历SearchHits，即所有的hit
        for (SearchHit hit : hits.getHits()) {
            // 获取高亮处理的结果
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlight = highlightFields.get("ocrText");
            Text[] fragments = highlight.fragments();

            // 将高亮结果放入Hit中
            tempHit = hit.getSourceAsMap();
            tempHit.put("ocrText",fragments[0].string());

            // 将本次查询结果放入列表
            String jsonStr = hit.getSourceAsString();
            System.out.println("jsonString为："+jsonStr);
            JSONObject hitResult = JSONObject.fromObject(tempHit);
            System.out.println("jsonObject为："+hitResult.toString());

            searchList.add(hitResult.toString());
        }
        client.close();

        JSONArray jsonArray = JSONArray.fromObject(searchList);
        JsonUtils.ajaxJson(jsonArray.toString(), response);
    }

    @RequestMapping(value = "accurateSearch2", method = RequestMethod.POST)
    public void accurateSearch2(@RequestParam("href") String href, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("peer1", 9200, "http")));

        // 建立搜索请求
        SearchRequest searchRequest = new SearchRequest("ocr");        // 建立"搜索请求"
        MatchPhraseQueryBuilder matchPhraseQueryBuilder = new MatchPhraseQueryBuilder("fileName",href);     // 建立针对ocrText字段的查询。
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();    // 建立"搜索内容"
        searchSourceBuilder.query(matchPhraseQueryBuilder);                     // 将"具体的查询"添加至搜索内容
        searchRequest.source(searchSourceBuilder);                              // 将"搜索内容"添加至"搜索请求"

        //执行搜索请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // 获取响应信息
        RestStatus status = searchResponse.status();    // 获取请求的状态
        System.out.println("响应信息为："+status);

        // 显示命中信息
        SearchHits hits = searchResponse.getHits();

        Map<String,Object> tempHit;

        String jsonStr = "";
        for (SearchHit hit : hits.getHits()) {
            jsonStr = hit.getSourceAsString();
        }

        client.close();

        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        JsonUtils.ajaxJson(jsonObject.toString(), response);
    }
}
