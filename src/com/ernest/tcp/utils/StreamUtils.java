package com.ernest.tcp.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 此类用于演示关于流的读写方法
 */

public class StreamUtils {
    /**
     * 功能：把输入流转换成 byte[]，即把文件内容读入到 byte[]
     *
     * @param is 输入流
     * @return 字节数组
     * @throws Exception 异常捕获
     */
    public static byte[] streamToByteArray(InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    // 创建输出流对象
        byte[] bytes = new byte[1024];  // 字节数组
        int len;
        while ((len = is.read(bytes)) != -1) {
            bos.write(bytes, 0, len);   // 把读取到的数据写入 bos
        }
        byte[] array = bos.toByteArray();   // 一次性转成字节数组
        bos.close();
        return array;
    }
    
    /**
     * 把输入流直接转换成字符串
     *
     * @param is 输入流
     * @return 字符串
     * @throws Exception 异常捕获
     */
    public static String streamToString(InputStream is) throws Exception {
        byte[] bytes = new byte[1024];
        int readLen = 0;
        String info = null;
        StringBuilder decodeInfo = new StringBuilder();
        while ((readLen = is.read(bytes)) != -1) {
            decodeInfo.append(new String(bytes, 0, readLen));
            if (decodeInfo.substring(decodeInfo.toString().length() - 1).equals("#")) {
                info = decodeInfo.substring(0, decodeInfo.toString().length() - 1);
                break;
            }
        }
        return (info != null) ? info : decodeInfo.toString();
    }
}
