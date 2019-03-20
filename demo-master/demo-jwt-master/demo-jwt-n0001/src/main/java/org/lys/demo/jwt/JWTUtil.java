package org.lys.demo.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
/**
 * JWT工具类
 * @author lys
 *
 */
public class JWTUtil {
	private static final long EXPIRE = 60 * 1000;
	private static final String SECRET = "9RlGH4qWGQKjOkJj";
	public static final String UID = "uid";

	/**
	 * 
	 * @param subject
	 * @param claims
	 * @param key
	 * @param issuedDate
	 * @param expireDate
	 * @return
	 */
	public static String createJWT(String id, String subject, Map<String, Object> claims, String key, Date issuedDate,
			Date expireDate) {
		JwtBuilder builder = Jwts.builder();
		if (StringUtils.isNoneBlank(id)) {
			builder.setId(id);// 设置jti(JWT// ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
								
		}
		if (StringUtils.isNoneBlank(subject)) {
			builder.setSubject(subject);// sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
		}
		if (claims != null && claims.size() > 0) {
			builder.setClaims(claims);// 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
		}
		if (issuedDate != null) {
			builder.setIssuedAt(issuedDate);// 签发时间
		}
		if (issuedDate != null) {
			builder.setExpiration(expireDate); // 设置过期时间
		}
		if (issuedDate != null) {
			builder.setExpiration(expireDate); // 设置过期时间
		}
		builder.signWith(SignatureAlgorithm.HS512, key);// 设置签名使用的签名算法和签名使用的秘钥
		return builder.compact();
	}

	/**
	 * 生成
	 * 
	 * @param uid
	 * @return
	 */
	public static String createToken(String uid) {
		Date nowDate = new Date();
		// 过期时间
		Date expireDate = new Date(nowDate.getTime() + EXPIRE * 1000);
		Map<String, Object> claims = new HashMap<>(1);
		claims.put(UID, uid);
		return createJWT(null, null, claims, SECRET, nowDate, expireDate);
	}
	/**
	 * 获取Claims
	 * @param jwt
	 * @return
	 */
	public static Claims getClaim(String jwt) {
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwt).getBody();
	}
	/**
	 * 用户id
	 * @param token
	 * @return
	 */
	public static String getUid(String token) {
		return getClaim(token).get(UID).toString();
	}
	/**
	 * 过期时间
	 * @param token
	 * @return
	 */
	public static Date getExpiration(String token) {
		return getClaim(token).getExpiration();
	}
	/**
	 * 是否过期
	 * @param token
	 * @return
	 */
	public static boolean isExpired(String token) {
		try {
			final Date expiration = getExpiration(token);
			return expiration.before(new Date());
		} catch (ExpiredJwtException expiredJwtException) {
			return true;
		}
	}
	/**
	 * 签发时间
	 * @param token
	 * @return
	 */
	public static Date getIssuedAt(String token) {
		return getClaim(token).getIssuedAt();
	}

	public static void main(String[] args) {
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE1NTMxMjMzNjQsInVpZCI6IjMzMzMiLCJpYXQiOjE1NTMwNjMzNjR9.s52EdId95odQoNoQkgreVb2Z9j3GGLswIavQK5BrAnZl01haFEjuihhTgf3IkMH7hRoZJ6ME0_ESBlhoeEo1nQ";
		String uid = JWTUtil.getUid(token);
		System.out.println(uid);
		System.out.println(JWTUtil.getExpiration(token));
		System.out.println(JWTUtil.getIssuedAt(token));
		System.out.println(JWTUtil.isExpired(token));
	}

}
