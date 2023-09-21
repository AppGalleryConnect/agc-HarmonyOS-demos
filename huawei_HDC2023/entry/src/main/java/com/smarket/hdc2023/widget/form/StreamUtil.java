package com.smarket.hdc2023.widget.form;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
    /**
     * 输入流转字节数组
     *
     * @param inputStream 输入流
     * @return 字节数组
     */
    public static byte[] streamToByte(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            int read = inputStream.read(bytes);
            inputStream.close();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取输入流中的数据
     *
     * @param inputStream 输入流
     * @return 输入流中的数据
     */
    public static String readInputStreams(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = -1;
        try {
            while ((length = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
            String data = baos.toString();
            inputStream.close();
            baos.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
