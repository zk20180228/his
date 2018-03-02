package cn.honry.portal.until;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class AES {
	private static final class AESHolder {
		public static final AES INSTANCE = new AES();
	};

	public static final AES getInstance() {
		return AESHolder.INSTANCE;
	}

	private static int keylength = 128;

	private SecretKey getKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(keylength,new SecureRandom("HONRY_2017".getBytes()));
		// 使用上面这种初始化方法可以特定种子来生成密钥，这样加密后的密文是唯一固定的。
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] keyBytes = secretKey.getEncoded();
		// Key转换
		SecretKey key = new SecretKeySpec(keyBytes, "AES");
		return key;
	}

	/**
	 * 加密
	 * 
	 * @param str
	 *            要加密文件
	 * @return 加密后的文件
	 */
	public String encode(String str) {
		String encode = null;
		try {
			SecretKey key = getKey();
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encodeResult = cipher.doFinal(str.getBytes("utf-8"));
			encode = byte2hex(encodeResult);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return encode;
	}

	/**
	 * 解密
	 * 
	 * @param str
	 * @return
	 */
	public String decode(String str) {
		String decode = null;
		try {
			SecretKey key = getKey();
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(cipher.DECRYPT_MODE, key);
			byte[] decodeBuffer = hex2byte(str);
			byte[] decodeResult = cipher.doFinal(decodeBuffer);
			decode = new String(decodeResult,"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return decode;
	}

	 /** 
	 *  
	 * 字节转换成十六进制的字符串 
	 *  
	 * @param b 
	 * @return  
	 * @return String 
	 * @exception 异常描述 
	 * @see 
	 */  
	public static String byte2hex(byte[] b) {  
	    String hs = "";  
	    String stmp = "";  
	    for (int n = 0; n < b.length; n++) {  
	        stmp = Integer.toHexString(b[n] & 0xFF);  
	        if (stmp.length() == 1)  
	            hs = hs + "0" + stmp;  
	        else {  
	            hs = hs + stmp;  
	        }  
	    }  
	    return hs.toUpperCase();  
	} 
	/** 
     *  
     * 十六进制的字符串转换成字节 
     *  
     * @param strhex 
     * @return  
     * @return byte[] 
     * @exception 异常描述 
     * @see 
     */  
    public static byte[] hex2byte(String strhex) {  
        if (strhex == null) {  
            return null;  
        }  
        int l = strhex.length();  
        if (l % 2 == 1) {  
            return null;  
        }  
        byte[] b = new byte[l / 2];  
        for (int i = 0; i != l / 2; i++) {  
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);  
        }  
        return b;  
    }  
}
