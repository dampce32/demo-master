package org.lys.demo.ehcache.n0002;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CacheLocalManager {
	private static final CacheManager cacheManager = new CacheManager();

	public CacheLocalManager() {
	}

	public static CacheManager getCacheManager() {
		return cacheManager;
	}

	public static Cache getCache(String cacheName) {
		return getCacheManager().getCache(cacheName);
	}

	public static Object readCache(String key, String cacheName) {
		try {
			Cache cache = getCache(cacheName);
			Element element = cache.get(key);
			if (element != null) {
				return element.getValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean removeCache(String key, String cacheName) {
		try {
			Cache cache = getCache(cacheName);
			cache.remove(key);
			cache.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean saveCache(String key, Object value, String cacheName) {
		try {
			Cache cache = getCache(cacheName);
			Element element = new Element(key, value);
			cache.put(element);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean removeAllCache(String cacheName) {
		try {
			Cache cache = getCache(cacheName);
			cache.removeAll();
			cache.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static int cacheSize(String cacheName) {
		try {
			Cache cache = getCache(cacheName);
			return cache.getSize();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static List<String> getCacheKeys(String cacheName) {
		try {
			Cache cache = getCache(cacheName);
			return cache.getKeys();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
