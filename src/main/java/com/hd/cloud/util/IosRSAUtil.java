package com.hd.cloud.util;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class IosRSAUtil {
	/**
	 * 签名算法
	 */
	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	public static String decryptAndSha256(String encryptedStr, String privateKey) {
		try {
			String decryptedStr = decryptByPrivateKey(encryptedStr, privateKey);

			return DigestUtils.sha256Hex(decryptedStr.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成RSA密钥对
	 *
	 * @param
	 * @return keyset
	 */
	public static void generateKey() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KeyPair kp = kpg.genKeyPair();
			PublicKey pbkey = kp.getPublic();
			PrivateKey prkey = kp.getPrivate();
			byte[] pbk = pbkey.getEncoded();
			byte[] prk = prkey.getEncoded();
			System.out.println("pbkey:" + encryptBASE64(pbk));
			System.out.println("prkey:" + encryptBASE64(prk));
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}

	public static String encryptPassword(String password, String publicKey) {
		return encryptByPublicKey(password, publicKey);
	}

	public static String decryptPassword(String encrypt, String privateKey) throws Exception {
		return decryptByPrivateKey(encrypt, privateKey);
	}

	/**
	 * RSA公钥加密
	 *
	 * @return 密文
	 */
	public static String encryptByPublicKey(String data, String key) {
		// 得到公钥
		byte[] publicKey = new byte[0];
		String mi = "";
		try {
			publicKey = decryptBASE64(key);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			RSAPublicKey keyPublic = (RSAPublicKey) kf.generatePublic(keySpec);
			// 加密数据
			Cipher cp = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cp.init(Cipher.ENCRYPT_MODE, keyPublic);

			byte[] binaryData = cp.doFinal(data.getBytes());
			String base64String = Base64.encodeBase64String(binaryData);
			System.out.println("  base64String=" + base64String);

			// 模长
			int key_len = keyPublic.getModulus().bitLength() / 8;
			// 加密数据长度 <= 模长-11
			String[] datas = splitString(data, key_len - 11);
			// 如果明文长度大于模长-11则要分组加密
			for (String s : datas) {
				mi += bcd2Str(cp.doFinal(s.getBytes()));
			}
			// String password = decryptByPrivateKey(mi, PRIVATE_KEY);
			// System.out.println("encrypted:" + mi+" password="+password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mi;

	}

	/**
	 * RSA私钥解密
	 *
	 * @return 明文
	 */
	public static String decryptByPrivateKey(String encrypted, String key) throws Exception {
		// 得到私钥
		byte[] privateKey = decryptBASE64(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		RSAPrivateKey keyPrivate = (RSAPrivateKey) kf.generatePrivate(keySpec);

		// 解密数据
		Cipher cp = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cp.init(Cipher.DECRYPT_MODE, keyPrivate);

		byte[] encryptedByte = Base64.decodeBase64(encrypted);
		byte[] binaryData = cp.doFinal(encryptedByte);
		String base64String = Base64.encodeBase64String(binaryData);
		String string = new String(binaryData);
		System.out.println("  base64String=" + string);

		return string;

	}

	public static String encryptBASE64(byte[] key) throws Exception {
		return Base64.encodeBase64String(key);
	}

	public static byte[] decryptBASE64(String key) throws Exception {
		return Base64.decodeBase64(key);
	}

	/**
	 * ASCII码转BCD码
	 */
	public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = asc_to_bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}

	public static byte asc_to_bcd(byte asc) {
		byte bcd;

		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);
		return bcd;
	}

	/**
	 * BCD转字符串
	 */
	public static String bcd2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;

		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}

	/**
	 * 拆分字符串
	 */
	public static String[] splitString(String string, int len) {
		int x = string.length() / len;
		int y = string.length() % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		String[] strings = new String[x + z];
		String str = "";
		for (int i = 0; i < x + z; i++) {
			if (i == x + z - 1 && y != 0) {
				str = string.substring(i * len, i * len + y);
			} else {
				str = string.substring(i * len, i * len + len);
			}
			strings[i] = str;
		}
		return strings;
	}

	/**
	 * 拆分数组
	 */
	public static byte[][] splitArray(byte[] data, int len) {
		int x = data.length / len;
		int y = data.length % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		byte[][] arrays = new byte[x + z][];
		byte[] arr;
		for (int i = 0; i < x + z; i++) {
			arr = new byte[len];
			if (i == x + z - 1 && y != 0) {
				System.arraycopy(data, i * len, arr, 0, y);
			} else {
				System.arraycopy(data, i * len, arr, 0, len);
			}
			arrays[i] = arr;
		}
		return arrays;
	}

	/**
	 * RSA签名
	 *
	 * @param content
	 *            待签名数据
	 * @param privateKey
	 *            商户私钥
	 * @return 签名值
	 */
	public static String sign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(decryptBASE64(privateKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initSign(priKey);
			signature.update(content.getBytes("UTF-8"));
			byte[] signed = signature.sign();
			return encryptBASE64(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * RSA验签名检查
	 *
	 * @param content
	 *            待签名数据
	 * @param sign
	 *            签名值
	 * @param publicKey
	 *            分配给开发商公钥
	 * @return 布尔值
	 */
	public static boolean verifySign(String content, String sign, String publicKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = decryptBASE64(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initVerify(pubKey);
			signature.update(content.getBytes("UTF-8"));
			boolean bverify = signature.verify(decryptBASE64(sign));
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
