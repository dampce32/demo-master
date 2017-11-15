package org.tempuri;

import javax.xml.ws.Holder;

import org.junit.Test;

public class SelectCommandTest {

	@Test
	public void testSelectCommand(){
		Holder selectCommandResult = new Holder();
		Holder strErr = new Holder();
		CommService commService = new CommService();
		CommServiceSoap commServiceSoap = commService.getCommServiceSoap();
		commServiceSoap.selectCommand("GetData", "安康达|8|1|2015-03-01|2015-03-02||||||", " AKD_command ", selectCommandResult, strErr);
		System.err.println(selectCommandResult.value);
	
	}
}
