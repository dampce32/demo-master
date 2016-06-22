package org.lys.access;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

public class WXTest {

	public static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";// 获取access
	// url
	public static final String APP_ID = "wx5a2ae135bdfee601";
	public static final String SECRET = "525cfbf95f3e962d864a4d9d8ae26e2b";

	public static String getToken(String apiurl, String appid, String secret) {

		String turl = String.format("%s?grant_type=client_credential&appid=%s&secret=%s", apiurl, appid, secret);
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(turl);
		String result = null;
		try {
			HttpResponse res = client.execute(get);
			String responseContent = null; // 响应内容
			HttpEntity entity = res.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			JSONObject json = JSONObject.parseObject(responseContent);
			// 将json字符串转换为json对象
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				if (json.get("errcode") != null) {// 错误时微信会返回错误码等信息，{"errcode":40013,"errmsg":"invalid
													// appid"}
				} else {// 正常情况下{"access_token":"ACCESS_TOKEN","expires_in":7200}
					result = json.get("access_token").toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接 ,释放资源
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	@Test
	public void testAccessToken() {
		System.out.println("=========1获取token=========");
		String accessToken = getToken(GET_TOKEN_URL, APP_ID, SECRET);// 获取token
		if (accessToken != null)
			System.out.println(accessToken);
	}
	
	@Test
	public void testAddMenu() {
		StringBuilder menuContentSB = new StringBuilder();
		menuContentSB.append("{");
		menuContentSB.append("\"button\": [");
		menuContentSB.append("{");
		menuContentSB.append("    \"type\": \"view\", ");
		menuContentSB.append("    \"name\": \"新瑞\", ");
		menuContentSB.append("    \"url\": \"http://wx.gffairs.com/ces-wx/?v=3\"");
		menuContentSB.append("}, ");
		menuContentSB.append("{");
		menuContentSB.append("    \"name\": \"菜单\", ");
		menuContentSB.append("    \"sub_button\": [");
		menuContentSB.append("        {");
		menuContentSB.append("            \"type\": \"view\", ");
		menuContentSB.append("            \"name\": \"实力新瑞\", ");
		menuContentSB.append("            \"url\": \"http://wx.gffairs.com/ces-wx/?v=2\"");
		menuContentSB.append("        }, ");
		menuContentSB.append("        {");
		menuContentSB.append("            \"type\": \"view\", ");
		menuContentSB.append("            \"name\": \"视频\", ");
		menuContentSB.append("            \"url\": \"http://v.qq.com/\"");
		menuContentSB.append("        }, ");
		menuContentSB.append("        {");
		menuContentSB.append("            \"type\": \"click\", ");
		menuContentSB.append("             \"name\": \"赞一下我们\", ");
		menuContentSB.append("             \"key\": \"V1001_GOOD\"");
		menuContentSB.append("         }");
		menuContentSB.append("    ]");
		menuContentSB.append("  }");
		menuContentSB.append(" ]");
		menuContentSB.append("}");
		
		String access_token = "g2kb_T3ueMPIr7hpw75SC6pzAueTmy0Zr4ULyMTq1DCoWY_IUccIyuripuy51jv3Rpks_eaSzffjJT4xNeSD6fFVwrurg1q-pkyASVe1Kzkx4PZEQozCTWrqV22GBo4HBKViAEAQZK"
				+ "";
		
		String turl = String.format("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s", access_token);
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(turl);
		String result = null;
		try {
			StringEntity stringEntity = new StringEntity(menuContentSB.toString(), "UTF-8");
			stringEntity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(stringEntity);// 将参数传入post方法中
			
			HttpResponse res = client.execute(httpPost);
			
			String responseContent = null; // 响应内容
			HttpEntity entity = res.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			JSONObject json = JSONObject.parseObject(responseContent);
			// 将json字符串转换为json对象
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				if (json.get("errcode") != null) {// 错误时微信会返回错误码等信息，{"errcode":40013,"errmsg":"invalid
													// appid"}
				} else {// 正常情况下{"access_token":"ACCESS_TOKEN","expires_in":7200}
					result = json.get("access_token").toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接 ,释放资源
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

}
