package org.lys.demo.encode.message_digest;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;
/**
 * @description: 消息摘要算法MD测试
 * MD5：用于网站密码加密
 * @copyright: 福建骏华信息有限公司 (c)2016
 * @createTime: 2016年8月5日上午9:33:34
 * @author：lys
 * @version：1.0
 */
public class MDTest {
	String src ="encode";
	
	@Test
	public void testJDKMD5() throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] md5Bytes =  md.digest(src.getBytes());
		System.out.println(Hex.encodeHexString(md5Bytes));
	}
	
	@Test
	public void testJDKMD2() throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD2");
		byte[] md2Bytes =  md.digest(src.getBytes());
		System.out.println(Hex.encodeHexString(md2Bytes));
	}
	
	@Test
	public void testBCMD4() throws IOException, NoSuchAlgorithmException {
		Security.addProvider(new BouncyCastleProvider());
		MessageDigest md = MessageDigest.getInstance("MD4");
		byte[] md5Bytes =  md.digest(src.getBytes());
		System.out.println(Hex.encodeHexString(md5Bytes));
	}
	
	@Test
	public void testBCMD5() throws IOException {
		Digest digest = new MD5Digest();
		digest.update(src.getBytes(), 0, src.getBytes().length);
		byte[] md5Bytes = new byte[digest.getDigestSize()];
		digest.doFinal(md5Bytes, 0);
		System.out.println(org.bouncycastle.util.encoders.Hex.toHexString(md5Bytes));
	}
	
	@Test
	public void testCCMD5() throws IOException {
		System.out.println(DigestUtils.md5Hex(src));
	}
	
	@Test
	public void testCCMD2() throws IOException {
		System.out.println(DigestUtils.md2Hex(src));
	}
}
