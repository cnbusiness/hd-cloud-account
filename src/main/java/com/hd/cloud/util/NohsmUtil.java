package com.hd.cloud.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.hd.cloud.AccountServiceApplication.RSAConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NohsmUtil {

	public NohsmUtil(RSAConfig rsaConfig) throws Exception {
		super();
		log.info("rsaConfig={}", rsaConfig);
		loadPrivateKey(rsaConfig.getPrivateKey());
		loadPublicKey(rsaConfig.getPublicKey());
	}

	/**
	 * rsa解密后，对明文再进行一次MD5加密再返回
	 * 
	 * @param base64String
	 * @return
	 * @throws Exception
	 */
	public static String decryptWithBase64(String priKey, String base64String) throws Exception {
		byte[] binaryData = decrypt(loadPrivateKey1(priKey), Base64.decodeBase64(base64String));
		String string = new String(binaryData);
		// 对明文密码进行md5加密
		String md5ofStr = DigestUtils.md5Hex(string.getBytes("UTF-8"));
		return md5ofStr;
	}

	/**
	 * rsa解密后，返回铭文
	 * 
	 * @param base64String
	 * @return
	 * @throws Exception
	 */
	public String decryptWithBase64NoMd5(String base64String) throws Exception {
		byte[] binaryData = decrypt(getPrivateKey(), Base64.decodeBase64(base64String));
		String string = new String(binaryData);
		return string;
	}

	public String encryptWithBase64(String string) throws Exception {
		byte[] binaryData = encrypt(getPublicKey(), string.getBytes());
		String base64String = Base64.encodeBase64String(binaryData);
		return base64String;
	}

	/**
	 * 私钥
	 */
	public static RSAPrivateKey privateKey;

	/**
	 * 公钥
	 */
	public RSAPublicKey publicKey;

	/**
	 * 获取私钥
	 * 
	 * @return 当前的私钥对象
	 */
	public static RSAPrivateKey getPrivateKey() {
		log.info("privateKey={}", privateKey.getFormat());
		return privateKey;
	}

	/**
	 * 获取公钥
	 * 
	 * @return 当前的公钥对象
	 */
	public RSAPublicKey getPublicKey() {
		log.info("publicKey={}", publicKey.getFormat());
		return publicKey;
	}

	/**
	 * 随机生成密钥对
	 */
	public void genKeyPair() {
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keyPairGen.initialize(1024, new SecureRandom());
		KeyPair keyPair = keyPairGen.generateKeyPair();
		this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
		this.publicKey = (RSAPublicKey) keyPair.getPublic();
	}

	/**
	 * 从文件中输入流中加载公钥
	 * 
	 * @param in
	 *            公钥输入流
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public void loadPublicKey(InputStream in) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			loadPublicKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("公钥数据流读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥输入流为空");
		}
	}

	/**
	 * 从字符串中加载公钥
	 * 
	 * @param publicKeyStr
	 *            公钥数据字符串
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public void loadPublicKey(String publicKeyStr) throws Exception {
		try {
			byte[] buffer = Base64.decodeBase64(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			this.publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	/**
	 * 从文件中加载私钥
	 * 
	 * @param keyFileName
	 *            私钥文件名
	 * @return 是否成功
	 * @throws Exception
	 */
	public void loadPrivateKey(InputStream in) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			loadPrivateKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		}
	}

	public void loadPrivateKey(String privateKeyStr) throws Exception {
		try {
			byte[] buffer = Base64.decodeBase64(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			throw new Exception("私钥非法");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	public static RSAPrivateKey loadPrivateKey1(String privateKeyStr) throws Exception {
		try {
			byte[] buffer = Base64.decodeBase64(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			throw new Exception("私钥非法");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	/**
	 * 加密过程
	 * 
	 * @param publicKey
	 *            公钥
	 * @param plainTextData
	 *            明文数据
	 * @return
	 * @throws Exception
	 *             加密过程中的异常信息
	 */
	public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA");// , new BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	/**
	 * 解密过程
	 * 
	 * @param privateKey
	 *            私钥
	 * @param cipherData
	 *            密文数据
	 * @return 明文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
		if (privateKey == null) {
			throw new Exception("解密私钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA");// , new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] output = cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}

	/**
	 * 字节数据转字符串专用集合
	 */
	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	/**
	 * 字节数据转十六进制字符串
	 * 
	 * @param data
	 *            输入数据
	 * @return 十六进制内容
	 */
	public static String byteArrayToString(byte[] data) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			// 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
			stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
			// 取出字节的低四位 作为索引得到相应的十六进制标识符
			stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
			if (i < data.length - 1) {
				stringBuilder.append(' ');
			}
		}
		return stringBuilder.toString();
	}

	public static String decrypt(String enc, String pass) {
		byte[] he, hp, hr;
		he = hexToByte(enc);
		hp = strToByte(pass);
		hr = xor(he, hp);
		return byteToStr(hr);
	}

	static byte[] hexToByte(String hexStr) { // '0a1f35' [0x0a, 0x1f, 0x35]
		byte[] result = new byte[hexStr.length() / 2];
		String t;
		for (int i = 0; i < hexStr.length(); i += 2) {
			byte b;
			t = hexStr.substring(i, i + 2);
			b = (byte) Integer.parseInt(t, 16);
			result[(i + 1) / 2] = b;
		}
		return result;
	}

	static String byteToHex(byte[] bytes) { // [0x0a, 0x1f, 0x35] '0a1f35'
		StringBuffer result = new StringBuffer("");
		for (int b : bytes) {
			String t;
			if (b < 0) {
				b = (b + 0xFF) + 1;
			}
			t = Integer.toHexString(b).toUpperCase();
			if (t.length() < 2) {
				t = '0' + t;
			}
			result.append(t);
		}
		return result.toString();
	}

	static byte[] strToByte(String str) { // 'abcdefg' [0x61, 0x62, 0x63, 0x64,
											// 0x65, 0x66, 0x67]
		byte[] result = new byte[str.length()];

		for (int i = 0; i < str.length(); i++) {
			int t = (int) str.charAt(i);
			result[i] = (byte) t;
		}
		return result;
	}

	static String byteToStr(byte[] bytes) { // [0x61, 0x62, 0x63, 0x64, 0x65,
											// 0x66, 0x67] 'abcdefg'
		return new String(bytes);
	}

	static byte[] xor(byte[] bytes1, byte[] bytes2) {
		byte[] result = new byte[bytes1.length];
		System.out.println("=======================");
		for (int i = 0; i < bytes1.length; i++) {
			byte h1, h2;
			h1 = bytes1[i];
			if (i >= bytes2.length) {
				h2 = (byte) 0;
			} else {
				h2 = bytes2[i];
			}
			result[i] = (byte) (h1 ^ h2);
			System.out.println(result[i]);
		}
		System.out.println("=======================end");

		return result;
	}

}