package com.isoftstone.epsp.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * 令牌加密处理
 */
public class TokenEncrypt {
    public static final String ENCRYPT_KEY = "F65xgjc8alx1edc*";

    public static void main(String[] args) throws Exception {
        String key = "F65xgjc8alx1edc*";
        String data = "userId";
        Map<String, String> map = getTokenIv(key, data);
        System.out.println("放在header中，参数名iv");
        System.out.println(map.get("iv"));
        System.out.println("放在header中，参数名token");
        System.out.println(map.get("token"));
    }



    /**
     * 签名
     *
     * @param key 约定的秘钥
     * @param data 数据明文
     * @return map: token:令牌,iv:偏移变量
     * @throws Exception
     */
    public static Map<String, String> getTokenIv(String key,String data) throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        Map<String, String> map = GetIv();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String tokenString = encryptAES(data+"," + timestamp, key, map.get("iv"));
        result.put("token", tokenString);
        result.put("iv", map.get("outIv"));
        return result;
    }


    /**
     * 生成 加密偏移量  iv本地加密，outIv输出
     * @return
     */
    public static Map<String, String> GetIv()
    {
        List<String> list1 = Arrays.asList("Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y");
        List<String> list2 = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        List<String> ivList = GetString(16);

        String iv = "";
        String outIv = "";

        for (int i = 0; i < ivList.size(); i++) {
            int charIndex = list1.indexOf(ivList.get(i));
            iv += list1.get(charIndex);
            outIv += list2.get(charIndex);
        }

        Map<String, String> result = new HashMap<String, String>();
        result.put("iv", iv);
        result.put("outIv", outIv);
        return result;
    }

    private static List<String> GetString(int count)
    {
        List<String> list = new LinkedList<String>();
        Random random = new Random();
        for (int i = 0; i < count; i++)
        {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            String val="";
            if ("char".equalsIgnoreCase(charOrNum)) {
                val = String.valueOf((char) (65 + random.nextInt(26)));
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val = String.valueOf(random.nextInt(10));
            }
            list.add(val);
        }
        return list;
    }

    /**
     * AES算法加密明文
     * @param data 明文
     * @param key  密钥，长度16
     * @param iv   偏移量，长度16
     * @return 密文
     */
    public static String encryptAES(String data, String key, String iv) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes("utf-8");
            int plaintextLength = dataBytes.length;

            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes("utf-8"), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes("utf-8"));

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return encode(encrypted).trim();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 编码
     *
     * @param byteArray
     * @return
     */
    private static String encode(byte[] byteArray) {
        return new String(new Base64().encode(byteArray));
    }
}
