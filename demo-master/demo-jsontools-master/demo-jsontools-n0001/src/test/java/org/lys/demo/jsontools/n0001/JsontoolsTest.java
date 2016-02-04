package org.lys.demo.jsontools.n0001;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.parser.JSONParser;

import antlr.RecognitionException;
import antlr.TokenStreamException;


public class JsontoolsTest {

	@Test
	public void test() throws Exception{
		String ret ="";
		JSONParser parser = new JSONParser(new StringReader(ret));
		Model model =
			(Model)JSONMapper.toJava(parser.nextValue(), Model.class);
		
		System.out.println("addId:"+model.getApp_id());
		System.out.println("result:"+model.getResult());
		System.out.println("respCode:"+model.getResp_code());
		System.out.println("remark:"+model.getRemark());
	}

}
class Model{
	
	private String app_id;
	/**
	 * 结果值
	 */
	private String result;
	/**
	 * 响应码，为0 表示成功
	 */
	private String resp_code;
	/**
	 * 错误信息
	 */
	private String remark;
	
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String appId) {
		app_id = appId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getResp_code() {
		return resp_code;
	}
	public void setResp_code(String respCode) {
		resp_code = respCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
