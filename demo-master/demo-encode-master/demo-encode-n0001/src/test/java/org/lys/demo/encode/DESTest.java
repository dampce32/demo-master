package org.lys.demo.encode;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;
/**
 * @description: 对称加密算法DES
 * @copyright: 福建骏华信息有限公司 (c)2016
 * @createTime: 2016年8月5日上午9:33:49
 * @author：lys
 * @version：1.0
 */
public class DESTest {
	String src ="encode";
	
	@Test
	public void testJDK() throws Exception{
		// 生成KEY
		KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");			
		keyGenerator.init(56);
		// 产生密钥
		SecretKey secretKey = keyGenerator.generateKey();
		// 获取密钥
		byte[] bytesKey = secretKey.getEncoded();
		
		
		// KEY转换
		DESKeySpec desKeySpec = new DESKeySpec(bytesKey);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
		Key convertSecretKey = factory.generateSecret(desKeySpec);
		
		
		// 加密
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
		byte[] result = cipher.doFinal(src.getBytes());
		System.out.println("jdk des encrypt:" + Hex.encodeHexString(result));
		
		// 解密
		cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
		result = cipher.doFinal(result);
		System.out.println("jdk des decrypt:" + new String(result));
	}
	
	@Test
	public void testBC() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		// 生成KEY
		KeyGenerator keyGenerator = KeyGenerator.getInstance("DES", "BC");
		keyGenerator.getProvider();
		keyGenerator.init(56);
		// 产生密钥
		SecretKey secretKey = keyGenerator.generateKey();
		// 获取密钥
		byte[] bytesKey = secretKey.getEncoded();
		
		
		// KEY转换
		DESKeySpec desKeySpec = new DESKeySpec(bytesKey);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
		Key convertSecretKey = factory.generateSecret(desKeySpec);
		
		
		// 加密
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
		byte[] result = cipher.doFinal(src.getBytes());
		System.out.println("bc des encrypt:" + Hex.encodeHexString(result));
		
		// 解密
		cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
		result = cipher.doFinal(result);
		System.out.println("bc des decrypt:" + new String(result));
	}
}
