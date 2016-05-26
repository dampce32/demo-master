package org.lys.access;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

public class WXTest {

	public static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";// 获取access
	// url
	public static final String APP_ID = "wxefa0337d9b34a925";
	public static final String SECRET = "c8d5adcd3818bcbc8a7c12fa5cfd4529";

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
				// TODO Auto-generated catch block
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

}
