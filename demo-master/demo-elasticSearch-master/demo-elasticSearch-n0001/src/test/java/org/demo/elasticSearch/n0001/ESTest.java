package org.demo.elasticSearch.n0001;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;


public class ESTest {

	private TransportClient client = null;

	// 获取客户端
	@SuppressWarnings("resource")
	@Before
	public void getClient() throws Exception {
		Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
		client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.202.132"), 9300));
	}
	
	@Test
	public void testCreateDoc() throws Exception {
		XContentBuilder content = XContentFactory.jsonBuilder().startObject().field("author", "王五")
				.field("title", "es入门").field("word_count", 1000).field("publish_date", "2019-08-01").endObject();
		
		IndexResponse response = client.prepareIndex("book", "novel", "6")
				.setSource(content)
				.get();
		System.out.println("索引名称：" + response.getIndex());
		System.out.println("类型：" + response.getType());
		System.out.println("文档ID：" + response.getId()); // 第一次使用是1
		System.out.println("当前实例状态：" + response.status());
	}

	@Test
	public void testCreateDocJSONObject() throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("author", "赵六");
		jsonObject.put("title", "activi入门");
		jsonObject.put("word_count", "2000");
		jsonObject.put("publish_date",  "2018-08-01");

		IndexResponse response = client.prepareIndex("book", "novel","7").setSource(jsonObject.toString(), XContentType.JSON)
				.get();
		System.out.println("索引名称：" + response.getIndex());//qq
		System.out.println("类型：" + response.getType());//tweet
		System.out.println("文档ID：" + response.getId()); // 第一次使用是1 
		System.out.println("当前实例状态：" + response.status());//CREATED
	}
	
	@Test
	public void testGet() {
		GetResponse response = client.prepareGet("book", "novel", "1").get();
		System.out.println(response.getSource());
	}

	 /**
     * 	删除一个文档
     * @param client
     */
	@Test
    public void testDelete(){
        DeleteResponse response=client.prepareDelete("book", "novel","1").get();
        if(response.status().getStatus()==200){
            System.out.println("删除成功");
        }
    }
	
	@Test
    public void testUpdate() throws IOException, InterruptedException, ExecutionException{
		UpdateRequest request = new UpdateRequest("book", "novel","1");
		
        XContentBuilder content = XContentFactory.jsonBuilder().startObject();
        content.field("author",  "赵六");
        content.endObject();
        request.doc(content);
        
        client.update(request).get();
    }
	@Test
	public void testSearch() throws Exception {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.must(QueryBuilders.matchQuery("author", "赵六"));//模糊查询
		boolQuery.must(QueryBuilders.matchQuery("title", "入门"));
		
		RangeQueryBuilder rangeQuery =  QueryBuilders.rangeQuery("word_count").from(1000).to(2000);//字段级别查询
		
		boolQuery.filter(rangeQuery);
		
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch("book")// 创建查询索引,参数productindex表示要查询的索引库为blog、index
				.setTypes("novel") // 设置type
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)// 设置查询类型 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询
																// 2.SearchType.SCAN =扫描查询,无序
				.setQuery(boolQuery) // 设置查询项以及对应的值
				.setFrom(0).setSize(10);// 设置分页;
		
		System.out.println(searchRequestBuilder);
		SearchResponse response = searchRequestBuilder.get();
		SearchHits hits = response.getHits();
		System.out.println("总数：" + hits.getTotalHits());
		for (SearchHit hit : hits.getHits()) {
			System.out.println(hit.getSourceAsMap());
		}
		System.out.println(response.toString());
		closeClient();
	}
	
	@Test
    public void testSearchArray() throws Exception {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.should(QueryBuilders.termQuery("roles", "role1"));
        boolQuery.should(QueryBuilders.termQuery("roles", "role2"));
        
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("book")// 创建查询索引,参数productindex表示要查询的索引库为blog、index
                .setTypes("lunatic") // 设置type
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)// 设置查询类型 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询
                                                                // 2.SearchType.SCAN =扫描查询,无序
                .setQuery(boolQuery) // 设置查询项以及对应的值
                .setFrom(0).setSize(10);// 设置分页;
        
        System.out.println(searchRequestBuilder);
        SearchResponse response = searchRequestBuilder.get();
        SearchHits hits = response.getHits();
        System.out.println("总数：" + hits.getTotalHits());
        for (SearchHit hit : hits.getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
        System.out.println(response.toString());
        closeClient();
    }
	
	@Test
	public void testHighLight() throws Exception {
		QueryBuilder matchQuery = QueryBuilders.matchQuery("title", "入门");
        HighlightBuilder hiBuilder=new HighlightBuilder();
        hiBuilder.preTags("<h2>");
        hiBuilder.postTags("</h2>");
        hiBuilder.field("title");

     // 搜索数据
        
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("book").setTypes("novel") // 设置type
                .setQuery(matchQuery)
                .highlighter(hiBuilder);
        System.out.println("查询json："+searchRequestBuilder);
        SearchResponse response = searchRequestBuilder.execute().actionGet();
        //获取查询结果集
        SearchHits searchHits = response.getHits();
        System.out.println("共搜到:"+searchHits.getTotalHits()+"条结果!");
        //遍历结果
        for(SearchHit hit:searchHits){
            System.out.println("String方式打印文档搜索内容:");
            System.out.println(hit.getSourceAsString());
            System.out.println("Map方式打印高亮内容");
            System.out.println(hit.getHighlightFields());
 
            System.out.println("遍历高亮集合，打印高亮片段:");
            Text[] text = hit.getHighlightFields().get("title").getFragments();
            for (Text str : text) {
                System.out.println(str.string());
            }
        }
	}
	

	// 关闭客户端
	@After
	public void closeClient() {
		if (this.client != null) {
			this.client.close();
		}
	}

}
