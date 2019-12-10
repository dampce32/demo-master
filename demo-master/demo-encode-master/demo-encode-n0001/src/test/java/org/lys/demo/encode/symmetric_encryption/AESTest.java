package org.lys.demo.encode.symmetric_encryption;

import java.security.Key;
import java.security.Security;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.junit.Test;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

public class AESTest {
	public static final String src = "aes test";

	// 用jdk实现:
	@Test
	public void testJDK() {
		try {
			// 生成KEY
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");			
			keyGenerator.init(128);
			// 产生密钥
			SecretKey secretKey = keyGenerator.generateKey();
			// 获取密钥
			byte[] keyBytes = secretKey.getEncoded();
			
			// KEY转换
			Key key = new SecretKeySpec(keyBytes, "AES");
			
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); 
			// 加密
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(src.getBytes());
			System.out.println("jdk aes encrypt:" + Hex.encodeHexString(result));
			
			// 解密
			cipher.init(Cipher.DECRYPT_MODE, key);
			result = cipher.doFinal(result);
			System.out.println("jdk aes decrypt:" + new String(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 用bouncy castle实现:
	@Test
	public void testBC() {
		try {
			Security.addProvider(new BouncyCastleProvider());
			
			// 生成KEY
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "BC");	
			keyGenerator.getProvider();
			keyGenerator.init(128);
			// 产生密钥
			SecretKey secretKey = keyGenerator.generateKey();
			// 获取密钥
			byte[] keyBytes = secretKey.getEncoded();
			
			
			// KEY转换
			Key key = new SecretKeySpec(keyBytes, "AES");
			
			
			// 加密
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(src.getBytes());
			System.out.println("bc aes encrypt:" + Hex.encodeHexString(result));
			
			// 解密
			cipher.init(Cipher.DECRYPT_MODE, key);
			result = cipher.doFinal(result);
			System.out.println("bc aes decrypt:" + new String(result));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";
	/**
	 * 加密给前端
	 * @createTime: 2019年12月10日 上午11:50:46
	 * @author: lin.yisong
	 */
	@Test
	public void testEncodeToJs() {
		try {
			//key L0tG4V4gG5bvrk8HCxbPVw==
			byte[] keyBytes = Base64.getDecoder().decode("4QrcOUm6Wau+VuBX8g+IPg==");
			
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			Key key = new SecretKeySpec(keyBytes, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal("nihao".getBytes());
			System.out.println("加密后的字符串:" + ByteUtils.toHexString(result));
			System.out.println("加密后的字符串Base64加密后:" + Base64.getEncoder().encodeToString(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 解密前端js加密
	 * @createTime: 2019年12月10日 下午12:01:13
	 * @author: lin.yisong
	 */
	@Test
	public void testDecodeJs() {
		try {
			//密钥
			byte[] keyBytes = Base64.getDecoder().decode("4QrcOUm6Wau+VuBX8g+IPg==");
			//加密后的文字
			String deWord ="L0tG4V4gG5bvrk8HCxbPVw==";
			//2f4b46e15e201b96efae4f070b16cf57
			System.out.println(ByteUtils.toHexString(Base64.getDecoder().decode(deWord)));
			Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "AES"));
			byte[] decryptBytes = cipher.doFinal(Base64.getDecoder().decode(deWord));
			System.out.println("解密后的文字:" + new String(decryptBytes));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testHuTool() {
		String content = "test中文";

		// 随机生成密钥
		byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();

		// 构建
		AES aes = SecureUtil.aes(key);

		// 加密
		byte[] encrypt = aes.encrypt(content);
		// 解密
		byte[] decrypt = aes.decrypt(encrypt);

		// 加密为16进制表示
		String encryptHex = aes.encryptHex(content);
		// 解密为字符串
		String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
	}
	
	@Test
	public void testHuToolEncodeToJs() {
		String content = "nihao";
		byte[] key = cn.hutool.core.codec.Base64.decode("4QrcOUm6Wau+VuBX8g+IPg==");
		// 构建
		AES aes = SecureUtil.aes(key);
		// 加密
		byte[] encrypt = aes.encrypt(content);
		String encryptBase64 = cn.hutool.core.codec.Base64.encode(encrypt);
		//L0tG4V4gG5bvrk8HCxbPVw==
		System.out.println(encryptBase64);
	}
	
	@Test
	public void testHuToolDecodeJFromJs() {
		byte[] key = cn.hutool.core.codec.Base64.decode("4QrcOUm6Wau+VuBX8g+IPg==");
		String deWord ="L0tG4V4gG5bvrk8HCxbPVw==";
		// 构建
		AES aes = SecureUtil.aes(key);
		String decryptStr = aes.decryptStr(cn.hutool.core.codec.Base64.decode(deWord));
		System.out.println(decryptStr);
	}
	
}
