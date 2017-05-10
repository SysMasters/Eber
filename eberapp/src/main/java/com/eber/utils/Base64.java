package com.eber.utils;

/**
 * Created by WangLibo on 2017/5/11.
 */

public class Base64 {
    /**
     * Base64编码
     *
     * @param str 要编码的字符串
     * @return 编码完成的字符串码
     */
    public static String getEncodeStr(String str) {
        byte byteArr[] = android.util.Base64.encode(str.getBytes(), android.util.Base64.DEFAULT);
        return new String(byteArr);
    }

    /**
     * Base64解码
     *
     * @param encodeStr 被编码的字符串
     * @return 解码完成，输出原本字符串
     */
    public static String getDecodeStr(String encodeStr) {
        byte byteArr[] = android.util.Base64.decode(encodeStr, android.util.Base64.DEFAULT);
        return new String(byteArr);
    }
}
