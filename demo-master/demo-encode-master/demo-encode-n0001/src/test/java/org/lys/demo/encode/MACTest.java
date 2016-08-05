package org.lys.demo.encode;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.junit.Test;
/**
 * @description: 消息摘要算法SHA(安全散列算法)
 * 应用：CRT
 * @copyright: 福建骏华信息有限公司 (c)2016
 * @createTime: 2016年8月5日上午9:33:49
 * @author：lys
 * @version：1.0
 */
public class MACTest {
	String src ="encode";
	/**
	 * @description: 测试sha-1
	 * @createTime: 2016年8月5日 上午9:41:46
	 * @author: lys
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException 
	 */
	@Test
	public void testJDKHmacMD5() throws NoSuchAlgorithmException, InvalidKeyException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacMD5");//初始化KeyGenerator
		SecretKey secretKey = keyGenerator.generateKey();//产生密钥
		byte[] key = secretKey.getEncoded();//获得密钥
		
		SecretKey restoreSecretKey = new SecretKeySpec(key, "HmacMD5");//还原密钥
		Mac mac = Mac.getInstance(restoreSecretKey.getAlgorithm());//实例化Mac
		mac.init(restoreSecretKey);//初始化密钥
		
		byte[] hmacMd5Bytes = mac.doFinal(src.getBytes());//执行摘要
		System.out.println(new String(Hex.encodeHexString(hmacMd5Bytes)));
	}
	
	@Test
	public void testBCHmacMD5() throws IOException {
		HMac hmac = new HMac(new MD5Digest());
		hmac.init(new KeyParameter(org.bouncycastle.util.encoders.Hex.decode("aaaaaaaaaa")));
		hmac.update(src.getBytes(),0,src.getBytes().length);
		
		byte[] bytes = new byte[hmac.getMacSize()];
		hmac.doFinal(bytes, 0);
		System.out.println(org.bouncycastle.util.encoders.Hex.toHexString(bytes));
	}
}
