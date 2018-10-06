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

public class RSAUtil {

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

	/**
	 * RSA公钥加密
	 * 
	 * @param 明文
	 * @param 公钥
	 * @return 密文
	 */
	public static String encryptByPublicKey(String data, String key) throws Exception {
		// 得到公钥
		byte[] publicKey = decryptBASE64(key);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		RSAPublicKey keyPublic = (RSAPublicKey) kf.generatePublic(keySpec);
		// 加密数据
		Cipher cp = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cp.init(Cipher.ENCRYPT_MODE, keyPublic);

		// 模长
		int key_len = keyPublic.getModulus().bitLength() / 8;
		// 加密数据长度 <= 模长-11
		String[] datas = splitString(data, key_len - 11);
		String mi = "";
		// 如果明文长度大于模长-11则要分组加密
		for (String s : datas) {
			mi += bcd2Str(cp.doFinal(s.getBytes()));
		}
		System.out.println("encrypted:" + mi);
		return mi;

	}

	/**
	 * RSA私钥解密
	 * 
	 * @param 密文
	 * @param 私钥
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

		// 模长
		int key_len = keyPrivate.getModulus().bitLength() / 8;
		byte[] bytes = encrypted.getBytes();
		byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
		System.err.println(bcd.length);
		// 如果密文长度大于模长则要分组解密
		String ming = "";
		byte[][] arrays = splitArray(bcd, key_len);
		for (byte[] arr : arrays) {
			ming += new String(cp.doFinal(arr));
		}
		System.out.println("decrypted:" + ming);
		return ming;

	}

	public static String encryptBASE64(byte[] key) throws Exception {
		return Base64.encodeBase64String(key);
	}

	public static byte[] decryptBASE64(String key) throws Exception {
		return Base64.decodeBase64(key);
	}

	/**
	 * ASCII码转BCD码
	 * 
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
	 * @param encode
	 *            字符集编码
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
	 * @param encode
	 *            字符集编码
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
			System.out.println("bverify:" + bverify);
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		// 生成密钥对
		// generateKey();

		// 测试
		String plainText = "abcd12349999999999999999999999992kwwowokskuiejdiekdidi&luiuihhiiSMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCbuOyFtgcKZ1YLmZjklnSeCRWfVx2fowWVbU0uzAE8AItDdxRAAuQeTd/9X4LlxhkgafgtxHqhxh+Bq0Y6ZQsvROpAmQ+4F0kr3EkXXpIAuw1GSXL342Uj/jHHenUrmX4UB6nFm5jxz2bK0y6VyRL4L2W88t7IoUmt2NvQvDby0YoAghE7K5/WBXKwG+2lIzwo2NxKTFK529oQBCHcKZpZTt5Xpkwy8UBibrHKnm1rXFgkXHsmXpqb0Xp3ofR/hVkr7CmJLMJCuGhC/IAmKWAAV+mlDHbPzMajztBhsypYDexeTH4VeJP2p0/qpCDLJpq5nkOCPXqzupWzj2HFNRXDAgMBAAECggEAF57sWbfrx3s3RRkuZPYhiFtOaVYYiz96OFlVDNUrFsPqW/hzHOaTXbloUZGsVFhBNXZ9i8uy4lkgLFYjl/X9xVTJx7Sv+relhL6sY5wXv36gG9pGUy2uCNClEmIT5eIpirZC3VOl1oAkUn3IIjZW50ihQRECFCLK/1DWvwndt+UuUFE/l69ljDnRfB4na+X5WH9IQ+sUOgQGj+GXJRSU/9PUMmgO9RipvaVAphJF1go0aUY/hBke7TbqtLsoI3Y7GzofeT8r+ZOcbaMy0MQoFoN9TaNjM6QQYPAyd1QKHTKaQTBALJc76FKoJzSD2TNWMyyWAwnOb3WAl4+TRIVh+QKBgQDPneD1JBGCr+KqrKGYvWgUGTqe7g/M4e6369blKqGelS1oHZlb6GwLS6isOIZuzQ45zXLRwcL8Xhk/Y5HxY/jAXquxhQ09ZZWCJeytaLV5wpHPR5b7bXViKCMpISmwgTrTjF302/fKGypnV17Y5eBmOJ//CjIWhtJCQdehiB6/xQKBgQDAAxnWTeBsbFOC7DuwZoX/ioQU80nVNV+jKTHEzQqRyU+vmJROpGeFA9iU0gMWHzFKXOv1gb4gBa/+APvgorYr78DmxAKK2EEN5NRF51N7QCDOV5HWxuvt3p7RhfOC0grC0F+nquDbLJD0/cNwLdALfl8+lNb/6pJ3uiC++WuP5wKBgBc5XuetIDRRPna/FtiCH3lYFSGgGa5ee5ihTgmWiju2Dj0+SmBokGV3EigprogIchXvMG/cT9ZxJDveBH5PI41JScVyz7dVfLLxURdYxiwXor3gETiQ1vei68P3DLXMzurJId15Ga/G+ChVMqvqkv58SRF7b3zrmeuFd/x+wInNAoGBALA26FJNnCXE8ItZo5nLLTSM+ZSEhgDef8usdZlIF2gd2TysiYb2yG5BTlWduullbQXy6zWfgU3ACnjQjFfm28rMuDrQLgkgV+uZUtpoZqvY+YKEyTnTyxe9VAGzrfwkKW4Kb47feoaKiunbra3YzXKO4nf7fqjUrOCl94FsQ7OHAoGABzPgcBGLj0rHMv/wuiE9r14b4dR+cGLQDjrdM4L9Y4i48hvhV1qH+MM7BE30xkrDqLJ88ZO1CISl4pXjp1AEt9zpkpRW0+MmOZ/hVje744Pxkf1NS8t+mhnGrlroUTiqkNT7/t5SvajKsxCj1g/LpQDI6gh0NBsNcwsPWU0RgRE=";
		System.out.println("plainText:" + plainText);
		String pbk = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsScoWwXoAbcfrm8opMwyGaUotEY3qLjmwVky4HFoxWHZQZgn/dQ1P7X/HhpZfJbCeXJzpvtvp/cQP6PrxCaYneShmEZcEofpyaYDykF7bCtyjlehF0sctUWcpTqB1W8F/BB7yRFCkTbs3X6Fl5ars4QZMU/gsu9HrtrY63eEfAAuCRzP658rrJD2A2Tvl59q5hgPw/yF7OuhdVoj6l71cWaql8jdDSyc/dRlk6ACoF7EGNBg29NCZ5UXNS97wTEKTMQwxbnRv/WWokYc34DKhRTlYikDXSiSNy0nkC4Tm6A7wpF9Th8//qfH1STxLvFcsQ2tR6GPw3MrhxTRjq7HuwIDAQAB";

		String pik = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCxJyhbBegBtx+ubyikzDIZpSi0RjeouObBWTLgcWjFYdlBmCf91DU/tf8eGll8lsJ5cnOm+2+n9xA/o+vEJpid5KGYRlwSh+nJpgPKQXtsK3KOV6EXSxy1RZylOoHVbwX8EHvJEUKRNuzdfoWXlquzhBkxT+Cy70eu2tjrd4R8AC4JHM/rnyuskPYDZO+Xn2rmGA/D/IXs66F1WiPqXvVxZqqXyN0NLJz91GWToAKgXsQY0GDb00JnlRc1L3vBMQpMxDDFudG/9ZaiRhzfgMqFFOViKQNdKJI3LSeQLhOboDvCkX1OHz/+p8fVJPEu8VyxDa1HoY/DcyuHFNGOrse7AgMBAAECggEAFFaOh4SyEiBX/sq8O89FoTQY7DlzgmdX9rKq6KGciaymKKWzUazfQCVA2M/lniwgv5ioNm9/biUrNGM5WhekRdxNwRGy2F3KNWBd5waFiPGkATuGPwpcjWMkjMOezEYmxb2OuJLGta08nGE+LR0bMUzlJ4goH+Lgs+5kIQRjrWiW6il+JXshvzw2w0qM256bZa8K/IYRcwDXwhGr//B9X2X5HIyO8NwWbQPkD/NnpCRmNGXmhJx3394reD+SLlRlEECyG/9bWzFnWA4nVO5M3lLwU2WcjgQgneA2ndymbEVsvdh3OgOFZGTfhzBcJP2Yp8KnyCnNQtsJSYUIy3TGQQKBgQDgHQbikFI+laHhw9C640i5aP2FGvmK4d4Jdw7uzf/zN1KrXWJOhB62KvBybsQTFZIuFygH8phA+gccvupt9EwAXcTCA5AM4mh93fRAjwV87/KVzClWPNAsUU122BdH7z8qn3PWaw3nFULfbWQmQ0nMjfcFOwJueMaijB9G2U8diQKBgQDKW6wIVsQJuca55W//sEYfciIBFH4316qOgOYnCbWFs3/nu8XXtS7clDTkLbk+oWPYzaonSTbFIAueg4D9ikFVciLfsjCYjACxjhoU030C1/bxDfiZ0CkVH6/nOUVU1B/6TC0hbT/puQCbPFd5wqE/HNn47n13JaUuUfuWZW5OIwKBgCsn68d9GxlKHx4mYu9aDQsHGKqlAyCnVeZkss9xg7QGFzlAhkmwT2ms4xhmFAPIx5lwhBjjskXw36W9bc82l9wKMnVZ0kdo4c47C2nTIK9qVUBbTCp7E2s1TMFx4ynK/FLs2ZWUj9zLz1DO89YaZv+o5sRvFIzJXRa3/N3bwsxhAoGAEiZCucS+dHjbIBU6BF7VC0D7V8l0wKFxRLK+8cVnKIS+9hPbXMt9Vn9cdxK6yKlFAU8A8eEZLnj/UcpNqW/bCETiZ9ndjfEQptwt7qxBTB3+ogQ0xebrqF9dMYnrc/5Kl/J5IcRgtR/ItCOnXN5eBAEcc71AN42C0TK9M/IJAK0CgYEAqU1JxIkmgEbhC/4meUG1kyPTu9s/7dkGqmH6976MaxH3mvuKC1cmDitG0P0SM9rJy82ypvAXgYZedMupKjUYZggbVMFZxjhczDXhjqE0vbdMdSi6rnvn/q3epNoITorxg2+94GwC67OMHirJGNCrpsVH7eI6GzR0R4psXiVrqrU=";
		try {
			String encryptedStr = encryptByPublicKey(plainText, pbk);
			System.out.println("encryptedStr:" + encryptedStr);

			String decryptedStr = decryptByPrivateKey(encryptedStr, pik);
			System.out.println("decryptedStr:" + decryptedStr);
			// String encryptedStr = sign(plainText,pik);
			// System.out.println("encryptedStr:" + encryptedStr);
			//
			// boolean result = verifySign(plainText, encryptedStr, pbk);
			// System.out.println("result:"+ result);
			//
			// if (result){
			// System.out.println("Success");
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
