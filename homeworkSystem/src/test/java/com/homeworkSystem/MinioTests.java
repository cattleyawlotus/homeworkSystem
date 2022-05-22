package com.homeworkSystem;

import com.homeworkSystem.minio.service.MinioService;
import io.minio.*;
import io.minio.messages.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MinioTests {


    @Autowired
    MinioService minioService;
    @Resource
    private MinioClient minioClient;

    @Test
    public void uploadFile() {
//        System.out.println(minioTemplate.checkFileIsExist("lzr/file3_1653103909932.pdf"));
//        System.out.println(minioTemplate.checkFolderIsExist("lzr"));
        File file = new File("C:\\Users\\lzr\\Desktop\\UML_formal-17-12-05.pdf");
        try {

            FileInputStream inputStream = new FileInputStream(file);
            MockMultipartFile mockMultipartFile = new MockMultipartFile(
                    "file1.pdf",
                    "file2.pdf",
                    MediaType.APPLICATION_PDF_VALUE,
                    inputStream
            );
//            System.out.println(mockMultipartFile.getContentType());
            System.out.println(minioService.putObject("class1","homework1/lzr/file3.pdf",mockMultipartFile));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void downloadFile() {
        System.out.println(minioService.checkFileIsExist("class1","homework1/lzr/file3_1653105205638.pdf"));
        System.out.println(minioService.checkFolderIsExist("class1","homework1"));
        System.out.println(minioService.checkFolderIsExist("class1","homework1/lzr"));
        InputStream is= minioService.getObjectByUrl("http://127.0.0.1:9000/1527933308778872833/1527941804287098882/1527954842583044098/BOM_1653128537898.html");
//        InputStream is=minioTemplate.getObject("class1","homework1/lzr/file3_1653105205638.pdf");

        String file = "C:\\Users\\lzr\\Desktop\\out.html";
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            minioClient.downloadObject(
//                    DownloadObjectArgs.builder()
//                            .bucket("1527933308778872833")
//                            .object("1527941804287098882/1527954842583044098/BOM_1653128537898.html")
//                            .filename("123.html")
//                            .build());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

    @Test
    public void deleteFile() {
//        minioTemplate.removeBucket("1527880529104371713");

        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs
                            .builder()
                            .bucket("homework").recursive(true).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                System.out.println(item.objectName());
                minioClient.removeObject(RemoveObjectArgs.builder().bucket("homework").object(item.objectName()).build());
            }
        } catch (Exception e) {

        }

    }

    @Test
    public void minioPolicy() {
        minioService.getBucketPolicy("1527933308778872833");
        minioService.createBucketPolicy(new StringBuilder(""),"test");
        minioService.getBucketPolicy("test");
    }



}
