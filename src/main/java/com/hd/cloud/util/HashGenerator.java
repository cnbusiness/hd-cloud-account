package com.hd.cloud.util;

import org.apache.commons.codec.digest.DigestUtils;

import com.hlb.cloud.util.ByteUtils;
import com.hlb.cloud.util.MD5Util;

public class HashGenerator {

	private static String gen(String key1, String key2) {
		String mixStr = key2 + key1;
		String retStr;
		try {
			byte[] tshash = MD5Util.getMD5ofStr(mixStr, false).getBytes();
			retStr = mixStr + ByteUtils.bytesToHexString(tshash);
			retStr = DigestUtils.sha256Hex(retStr).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
			retStr = key1;
		}

		return retStr;
	}

	/**
	 * 生成一个每min分钟换一次的时间戳
	 * 
	 * @param min
	 * @return
	 */
	private static long getTimeStamp(int min) {
		long ts = System.currentTimeMillis();
		return ts - (ts % (min * 60000));
	}

	public static String generateToken(String input) {
		String ts = String.valueOf(System.currentTimeMillis());
		return gen(input, ts);
	}

	/**
	 * 生成加密密钥 KEY=MD5(input + MD5(TIMESTAMP))
	 * 
	 * @param userId
	 * @return
	 */
	public static String generateEncryptKey(String input) {
		String ts = String.valueOf(getTimeStamp(15));
		return gen(input, ts);
	}

	/**
	 * 生成加密密钥 KEY=MD5(input + MD5(TIMESTAMP))
	 * 
	 * @param userId
	 * @return
	 */
	public static String generateEncryptKey(String input, int point) {
		String ts = String.valueOf(getTimeStamp(15) - point * 15 * 60000);
		return gen(input, ts);
	}
}
