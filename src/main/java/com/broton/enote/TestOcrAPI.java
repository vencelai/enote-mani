package com.broton.enote;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;

public class TestOcrAPI {

    public static void main(String[] args) throws  Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        Resource rs = new FileSystemResource("E:\\48104.jpg");

        builder.part("file1", rs, MediaType.parseMediaType(Files.probeContentType(rs.getFile().toPath())));

        MultiValueMap<String, HttpEntity<?>> multipartBody = builder.build();

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody, headers);

        final String SERVER_URL = "http://192.168.0.14:8188/api/v1/chiga-health-ocr";
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(SERVER_URL, httpEntity, String.class);

        System.out.println(responseEntity.getBody());
    }
}
