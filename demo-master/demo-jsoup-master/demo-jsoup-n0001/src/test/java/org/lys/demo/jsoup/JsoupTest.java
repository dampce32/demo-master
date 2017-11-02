package org.lys.demo.jsoup;


import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class JsoupTest {

	@Test
	public void testParse() {
		String html = "<html><head><title>First parse</title></head>"
				  + "<body><p>Parsed HTML into a doc.</p></body></html>";
		Document doc = Jsoup.parse(html);
		System.out.println(doc.html());
	}
	
	@Test
	public void testParseBodyFragment() {
		String html = "<div><p>Lorem ipsum.</p>";
		Document doc = Jsoup.parseBodyFragment(html);
		Element body = doc.body();
		System.out.println(body.html());
	}
	
	@Test
	public void testConnect() throws IOException {
		Document doc = Jsoup.connect("http://jsdj.sport.gov.cn/").userAgent("Mozilla").get();
		String title = doc.title();
		System.out.println(title);
	}
	
	

}
