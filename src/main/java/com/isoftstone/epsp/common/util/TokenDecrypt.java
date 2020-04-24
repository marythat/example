package com.isoftstone.epsp.common.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 令牌解密
 */
public class TokenDecrypt {

    public static void main(String[] args) throws Exception {
        String key = "F65xgjc8alx1edc*";
        Map res = decodeToken(key, "Jqo6fJxvzm+8qwVVfwklcWsjvbPvwPnsOz/FFdAIlBo=", "A2WFR697C7Q968M8");
        System.out.println("res:" + res.toString());
    }

    private static final Logger loger = LoggerFactory.getLogger(TokenDecrypt.class);

    /**
     * 令牌解析
     *
     * @param key   约定的关键字
     * @param token 需要验证的令牌
     * @param iv    需要验证的加密偏移量
     * @return Map result-Boolean 是否通过  data-String:解密后数据 error-String 错误信息
     */
    public static Map<String, Object> decodeToken(String key, String token, String iv) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        try {
            String finalIv = decodeIv(iv);
            token = token.trim();
            //解密令牌
            String decodeToken = decryptAES(token, key, finalIv);
            System.out.println("decodeToken:" + decodeToken);
            String[] res = decodeToken.split(",");

            String timestamp = res[1];
            Long verTimestamp = Long.valueOf(timestamp);
            if ((System.currentTimeMillis() - verTimestamp) > (1000 * 60 * 500)) {
                //申请的数据超过了5分钟的请求时间
                resMap.put("result", Boolean.FALSE);
                resMap.put("error", "验证超时");
            } else {
                String data = res[0];
                resMap.put("result", Boolean.TRUE);
                resMap.put("data", data);
            }
            return resMap;
        } catch (Exception ex) {
            loger.error("令牌解析异常:" + ",token:" + token + ",iv:" + iv, ex);
            resMap.put("result", Boolean.FALSE);
            resMap.put("error", "验证失败");
            return resMap;
        } catch (Error er) {
            loger.error("令牌解析错误:" + ",token:" + token + ",iv:" + iv, er);
            resMap.put("result", Boolean.FALSE);
            resMap.put("error", "验证失败");
            return resMap;
        }
    }

    /**
     * 解密偏移量 加密iv
     *
     * @return
     */
    private static String decodeIv(String iv) {
        List<String> list1 = Arrays.asList("Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y");
        List<String> list2 = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        String finalIv = "";
        for (char i : iv.toCharArray()) {
            String str = String.valueOf(i);
            Integer idx = list2.indexOf(str);
            finalIv += list1.get(idx);
        }
        return finalIv;
    }

    /**
     * AES算吗解密
     *
     * @param token 令牌
     * @param key   约定关键词
     * @param iv    解密的偏移量
     * @return
     * @throws Exception
     */
    private static String decryptAES(String token, String key, String iv) throws Exception {
        try {
            byte[] encrypted1 = new Base64().decode(token.getBytes());

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            int plaintextLength = encrypted1.length;

            if (plaintextLength % 16 != 0) {
                plaintextLength = plaintextLength + (16 - (plaintextLength % 16));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(encrypted1, 0, plaintext, 0, encrypted1.length);

            byte[] original = cipher.doFinal(plaintext);
            String originalString = new String(original);
            return originalString.trim();
        } catch (Exception e) {
            loger.error("AES解密异常" + e);
            return null;
        }

    }
}
