package com.broton.enote;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.DigestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {
    // java计算两个时间搓是否大于五分钟  https://blog.csdn.net/weixin_40738258/article/details/140114795

    /**
     * 32位长度随机密码key
     */
    private static final String PASSWORD = "MEqLCnG2Q0IfauMDbZq1lP46uP4BHsiv";

    public static void main(String[] args) throws Exception {
        System.out.println(getROCDateTime());

        /*
        String pcname = InetAddress.getLocalHost().getHostName();
        System.out.println(pcname);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        Resource rs = new FileSystemResource("E:\\48104.jpg");

        builder.part("file1", rs, MediaType.parseMediaType(Files.probeContentType(rs.getFile().toPath())));

        MultiValueMap<String, HttpEntity<?>> multipartBody = builder.build();

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody, headers);

        final String SERVER_URL = "http://192.168.0.102:8188/api/v1/chiga-health-ocr";
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(SERVER_URL, httpEntity, String.class);

        System.out.println(responseEntity.getBody());
         */
/*
        String str = String.valueOf(System.currentTimeMillis() + (30 * 1000));
        System.out.println("原始内容："+str);

        String encrypt = encrypt(str);
        System.out.println("加密后的内容："+encrypt);

        //String decrypt = decrypt(encrypt);
        String decrypt = decrypt("U2FsdGVkX191cIeLghF5Bywdy0PUK1jZZpCeY0Gqs+k=");
        System.out.println("解密后的内容："+decrypt);

        long paramTimestamp = Long.parseLong(decrypt);
        long nowtimeStamp = System.currentTimeMillis();
        long diff = paramTimestamp - nowtimeStamp;
        if (diff > 1 * 60 * 1000) {
            System.out.println("超時1分鐘以上");
        } else {
            System.out.println("檢核成功無超時");
        }
 */
    }

    /**
     * 取得民國年日期時間。
     * @return 民國年日期時間。
     *
     */
    public static String getROCDateTime() {
        DateFormat my = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
        String today = my.format(new Date());
        today = new Integer(new Integer(today.substring(0, 4)).intValue() - 1911).toString() + "/" +
                today.substring(4, 6) + "/" + today.substring(6, 8) + today.substring(8, 17);
        return today;
    }

    /**
     * AES256加密
     * @param content
     * @return
     */
    public static String encrypt(String content) {
        try {
            if(content == null) {
                return "";
            }
            //根据给定的字节数组构造一个密钥。enCodeFormat：密钥内容；"AES"：与给定的密钥内容相关联的密钥算法的名称
            SecretKeySpec key = new SecretKeySpec(PASSWORD.getBytes(StandardCharsets.UTF_8), "AES");
            //将提供程序添加到下一个可用位置
            Security.addProvider(new BouncyCastleProvider());
            //创建一个实现指定转换的 Cipher对象，该转换由指定的提供程序提供。
            //"AES/ECB/PKCS7Padding"：转换的名称；"BC"：提供程序的名称
            //Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            //cipher.init(Cipher.ENCRYPT_MODE, key);
            //byte[] byteContent = content.getBytes("utf-8");
            //byte[] cryptograph = cipher.doFinal(byteContent);
            //return new String(Base64.encode(cryptograph));
        } catch (Exception e) {
            //log.error("AES256 加密失败：", e.getMessage());
        }
        return null;
    }

    /**
     * AES256解密
     * @param cryptograph
     * @return
     */
    public static String decrypt(String cryptograph) {
        try {
            if(cryptograph == null) {
                return "";
            }
            SecretKeySpec key = new SecretKeySpec(PASSWORD.getBytes(StandardCharsets.UTF_8), "AES");
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] content = cipher.doFinal(Base64.decode(cryptograph));
            return new String(content);
        } catch (Exception e) {
            //log.error("AES256 解密失败：", e.getMessage());
        }
        return null;
    }
}


