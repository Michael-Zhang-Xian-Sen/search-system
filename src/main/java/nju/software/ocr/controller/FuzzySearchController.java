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
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 *  模糊查询代码编写
 */
@RestController
public class FuzzySearchController {

    @RequestMapping(value = "/fuzzySearch",method = RequestMethod.POST)
    public void fuzzySearch(@RequestParam("searchStr") String searchStr, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("peer1", 9200, "http")));
        SearchRequest searchRequest = new SearchRequest("ocr");

        //　搜索内容
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("ocrText",searchStr);
        matchQueryBuilder.fuzziness(Fuzziness.AUTO);
        searchSourceBuilder.query(matchQueryBuilder);

        // 高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.numOfFragments(0);
        HighlightBuilder.Field highlightText = new HighlightBuilder.Field("ocrText");
        highlightBuilder.field(highlightText);
        searchSourceBuilder.highlighter(highlightBuilder);

        searchSourceBuilder.from(0);
        searchSourceBuilder.size(100);


        // 执行搜索
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // 获取响应信息
        RestStatus status = searchResponse.status();
        if(!status.toString().equals("OK")){
            Status status1 = new Status("failed");
            JSONObject jsonObject = JSONObject.fromObject(status1);
            JsonUtils.ajaxJson(jsonObject.toString(), response);
        }

        // 获取命中信息
        SearchHits hits = searchResponse.getHits();
        List<String> searchList = new ArrayList<String>();  // 存储搜索结果。
        System.out.println("命中信息的数量为："+hits.getTotalHits().value);

        hits.getTotalHits();

        System.out.println("hits.getHits().length为："+hits.getHits().length);

        // 将命中信息存储至列表
        Map<String,Object> tempHit;
        for (SearchHit hit : hits.getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlight = highlightFields.get("ocrText");
            Text[] fragments = highlight.fragments();
            tempHit = hit.getSourceAsMap();
            tempHit.put("ocrText",fragments[0].string());

            JSONObject hitResult = JSONObject.fromObject(tempHit);
            searchList.add(hitResult.toString());
        }
        client.close();

        // 向前端返回列表
        JSONArray jsonArray = JSONArray.fromObject(searchList);
        JsonUtils.ajaxJson(jsonArray.toString(), response);
    }
}
