package org.lys.demo.jwt;

import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {
	/**
	 * 
	 * @param subject 
	 * @param claims
	 * @param key
	 * @param issuedDate
	 * @param expireDate
	 * @return
	 */
	 public static String generateToken(String subject,Map<String, Object> claims,String key,Date issuedDate,Date expireDate){
		 JwtBuilder builder =  Jwts.builder()
				 .setSubject(subject)//主题:该JWT所面向的用户，用于处理特定应用，不是常用的字段
				 .setClaims(claims)// 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
				 .setIssuedAt(issuedDate)//签发时间
				 .setExpiration(expireDate) // 设置过期时间
				 .signWith(SignatureAlgorithm.HS512, key)// 设置签名使用的签名算法和签名使用的秘钥
				 ;
		return builder.compact();
	 }
	
	 
//	 public static Jws<Claims> parserToken() throws Exception {
//		 
//		 Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws);
//
//	        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(pubKey).parseClaimsJws(token);
//	        return claimsJws;
//	    }
//	
//	public static void main(String[] args) {
//		System.out.println(JwtUtil.generateToken());
//		//eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UifQ.ADYHo_BflsXN66qvBXvykMOYV21JH3GDVh-Ma8iPmnU
//	}
    
}
