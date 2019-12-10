package org.lys.demo.encode.symmetric_encryption;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.junit.Test;

public class SM4Test {

	public static final String src = "sm4 test";
	@Test
	public void testBC() {
		try {
			Security.addProvider(new BouncyCastleProvider());
			
			// 生成KEY
			KeyGenerator keyGenerator = KeyGenerator.getInstance("SM4", BouncyCastleProvider.PROVIDER_NAME);	
			keyGenerator.getProvider();
			keyGenerator.init(128);//128 -32位16进制；256-64位16进制
			// 产生密钥
			SecretKey secretKey = keyGenerator.generateKey();
			// 获取密钥
//			byte[] keyBytes = secretKey.getEncoded();
			byte[] keyBytes = ByteUtils.fromHexString("8f6bf719aaa8febce57a00fa08567bb7");
			
			//8f6bf719aaa8febce57a00fa08567bb7
			//472d0a59f13c563b36caea2dee7cd4b6
			System.out.println(ByteUtils.toHexString(keyBytes));
			
			// KEY转换
			Key key = new SecretKeySpec(keyBytes, "SM4");
			
			
			// 加密
			Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS5Padding");
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
}
