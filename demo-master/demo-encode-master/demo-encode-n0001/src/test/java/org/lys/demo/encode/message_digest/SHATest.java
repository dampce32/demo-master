package org.lys.demo.encode.message_digest;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.junit.Test;

import cn.hutool.crypto.digest.DigestUtil;
/**
 * @description: 消息摘要算法SHA(安全散列算法)
 * 应用：
 * @copyright: 福建骏华信息有限公司 (c)2016
 * @createTime: 2016年8月5日上午9:33:49
 * @author：lys
 * @version：1.0
 */
public class SHATest {
	String src ="encode";
	/**
	 * @description: 测试sha-1
	 * @createTime: 2016年8月5日 上午9:41:46
	 * @author: lys
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void testJDK() throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA");
		md.update(src.getBytes());
		System.out.println(Hex.encodeHexString(md.digest()));
	}
	
	@Test
	public void testBC() throws IOException {
		Digest digest = new SHA1Digest();
		digest.update(src.getBytes(), 0, src.getBytes().length);
		byte[] md5Bytes = new byte[digest.getDigestSize()];
		digest.doFinal(md5Bytes, 0);
		System.out.println(org.bouncycastle.util.encoders.Hex.toHexString(md5Bytes));
	}
	
	@Test
	public void testBC224() throws IOException {
		Digest digest = new SHA224Digest();
		digest.update(src.getBytes(), 0, src.getBytes().length);
		byte[] md5Bytes = new byte[digest.getDigestSize()];
		digest.doFinal(md5Bytes, 0);
		System.out.println(org.bouncycastle.util.encoders.Hex.toHexString(md5Bytes));
	}
	
	@Test
	public void testHuToolSHA256() throws IOException {
		DigestUtil.sha256Hex("a123456");
	}
	
	@Test
	public void testCC() throws IOException {
		System.out.println(DigestUtils.sha1Hex(src));
	}
}
