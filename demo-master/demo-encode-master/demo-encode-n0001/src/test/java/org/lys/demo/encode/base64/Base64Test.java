package org.lys.demo.encode.base64;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Test {
	String src ="encode";
	
	@Test
	public void testJDKBase64() throws IOException {
		BASE64Encoder encoder = new BASE64Encoder();
		String encode = encoder.encode(src.getBytes());
		System.out.println(encode);
		
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] decodeBtyes = decoder.decodeBuffer(encode);
		System.out.println(new String(decodeBtyes));
	}
	
	
	@Test
	public void testCCBase64() throws IOException {
		String encode = Base64.encodeBase64String(src.getBytes());
		System.out.println(encode);
		
		byte[] decodeBtyes = Base64.decodeBase64(encode);
		System.out.println(new String(decodeBtyes));
	}
	
	@Test
	public void testBCBase64() throws IOException {
		byte[] encodeBytes = org.bouncycastle.util.encoders.Base64.encode(src.getBytes());
		System.out.println(new String(encodeBytes));
		
		byte[] decodeBytes = org.bouncycastle.util.encoders.Base64.decode(encodeBytes);
		System.out.println(new String(decodeBytes));
	}

}
