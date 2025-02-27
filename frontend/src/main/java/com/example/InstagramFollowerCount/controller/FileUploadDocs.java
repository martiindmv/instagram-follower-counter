package com.example.InstagramFollowerCount.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadDocs {

    @PostMapping(value = "/uploadFollowers", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Object fileWithFollowers(MultipartFile file) throws IOException;

    @PostMapping(value = "/uploadFollowing", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Object fileWithFollowing(MultipartFile file) throws IOException;
}