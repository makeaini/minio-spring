package com.cont.spring.utils;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.credentials.AssumeRoleProvider;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;

public class Test03 {
    //服务所在ip地址和端口
    public static final String ENDPOINT = "http://192.168.2.114:9000/";
    //mc的用户名
    public static final String ACCESS_KEY_COMPANY = "liuwenzhong";
    //mc的密码
    public static final String SECRET_KEY_COMPANY = "liuwenzhong123";
    //aws服务端点
    public static final String REGION = "cn-north-1";
    //上传的bucket名
    public static final String BUCKET = "test";
    //授权策略，允许访问名为bucket的桶的目录
    public static final String ROLE_ARN = "arn:aws:s3:::test/*";
    public static final String ROLE_SESSION_NAME = "anysession";
    //定义策略，可进行二次限定
    public static final String POLICY_GET_AND_PUT = "{\n" +
            " \"Version\": \"2012-10-17\",\n" +
            " \"Statement\": [\n" +
            "  {\n" +
            "   \"Effect\": \"Allow\",\n" +
            "   \"Action\": [\n" +
            "    \"s3:GetObject\",\n" +
            "    \"s3:PutObject\",\n" +
            "    \"s3:DeleteObject\"\n" +
            "   ],\n" +
            "   \"Resource\": [\n" +
            "    \"arn:aws:s3:::*\"\n" +
            "   ]\n" +
            "  }\n" +
            " ]\n" +
            "}";
    public static void main(String[] args) {
        try {
            //创建签名对象
            AssumeRoleProvider provider = new AssumeRoleProvider(
                    ENDPOINT,
                    ACCESS_KEY_COMPANY,
                    SECRET_KEY_COMPANY,
                    3600,//默认3600秒失效，设置小于这个就是3600，大于3600就实际值
                    POLICY_GET_AND_PUT,
                    REGION,
                    ROLE_ARN,
                    ROLE_SESSION_NAME,
                    null,
                    null);
            /**
             * 打印provider签名属性
             */
            System.out.println(provider.fetch().sessionToken());
            System.out.println(provider.fetch().accessKey());
            System.out.println(provider.fetch().secretKey());
            System.out.println(provider.fetch().isExpired());
            //使用签名获取mc对象
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(ENDPOINT)
                    .credentialsProvider(provider)
                    .build();
            String filename = "test3.jpeg";
            try {
                //对象流，获取文件
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream("C:\\Users\\shining\\Desktop\\" + "test2.jpeg"));
                //使用mc对象上传文件
                minioClient.putObject(PutObjectArgs.builder()
                        //桶名和aws服务端点
                        .bucket(BUCKET).region(REGION)
                        //前缀和对象名
                        .object(filename)
                        .stream(bis, bis.available(), -1)
                        .build());
//                minioClient.removeObject(RemoveObjectArgs.builder()
//                        .bucket(BUCKET).region(REGION).object("mx/" + filename).build());
//                System.out.println("文件上传成功！！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
 
    }
}