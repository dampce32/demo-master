package org.lys.demo.demo_elasticSearch_n0002;

import java.net.InetAddress;
import java.util.Date;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.minidev.json.JSONObject;

public class ESTest {

	private TransportClient client = null;

	// 获取客户端
	@SuppressWarnings("resource")
	@Before
	public void getClient() throws Exception {
		Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
		TransportAddress node = new TransportAddress(InetAddress.getByName("192.168.202.132"), 9300);
		client = new PreBuiltTransportClient(settings).addTransportAddress(node);
	}

	@Test
	public void createIndex() throws Exception {
		IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
				.setSource(XContentFactory.jsonBuilder().startObject().field("user", "kimchy")
						.field("postDate", new Date()).field("message", "trying out Elasticsearch").endObject())
				.get();
		System.out.println("索引名称：" + response.getIndex());
		System.out.println("类型：" + response.getType());
		System.out.println("文档ID：" + response.getId()); // 第一次使用是1
		System.out.println("当前实例状态：" + response.status());
	}

	@Test
	public void testIndex4() throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("user", "kimchy");
		jsonObject.put("postDate", "1989-11-11");
		jsonObject.put("message", "trying out Elasticsearch");

		IndexResponse response = client.prepareIndex("qq", "tweet","1").setSource(jsonObject.toString(), XContentType.JSON)
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
     * 删除一个文档
     * @param client
     */
	@Test
    public void deleteIndex(){
        DeleteResponse response=client.prepareDelete("qq", "tweet","1").get();
        if(response.status().getStatus()==200){
            System.out.println("删除成功");
        }
    }
	@Test
	public void getResult() throws Exception {
		SearchResponse response = client.prepareSearch("blog", "index")// 创建查询索引,参数productindex表示要查询的索引库为blog、index
				.setTypes("article") // 设置type
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)// 设置查询类型 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询
																// 2.SearchType.SCAN =扫描查询,无序
				.setQuery(QueryBuilders.termQuery("content", "today")) // 设置查询项以及对应的值
				// .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18)) // 设置Filter过滤
				.setFrom(0).setSize(60)// 设置分页
				.setExplain(true) // 设置是否按查询匹配度排序
				// .addSort("id", SortOrder.DESC)//设置按照id排序
				.execute().actionGet();
		SearchHits hits = response.getHits();
		System.out.println("总数：" + hits.getTotalHits());
		for (SearchHit hit : hits.getHits()) {
			if (hit.getSourceAsMap().containsKey("title")) {
				System.out.println("source.title: " + hit.getSourceAsMap().get("title"));
			}
		}
		System.out.println(response.toString());
		closeClient();
	}

	// 关闭客户端
	@After
	public void closeClient() {
		if (this.client != null) {
			this.client.close();
		}
	}

}
