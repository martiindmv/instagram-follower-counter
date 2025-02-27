package com.example.InstagramFollowerCount.controller;

import com.example.InstagramFollowerCount.util.InstagramUserComparator;
import com.example.InstagramFollowerCount.util.JsonArrayMapper;
import com.example.InstagramFollowerCount.util.UserRelationshipData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Controller
public class FileUploadController implements FileUploadDocs {
    private static final Logger LOGGER = LogManager.getLogger(FileUploadController.class);

    @Autowired
    private UserRelationshipData userRelationshipData;
    @Autowired
    private JsonArrayMapper jsonArrayMapper;
    @Autowired
    private InstagramUserComparator instagramUserComparator;

    @Override
    public Object fileWithFollowers(@RequestPart("followers") MultipartFile file) throws IOException {

        try {
            String contentFollowers = new String(file.getInputStream().readAllBytes());
            JSONArray followersArray = jsonArrayMapper.mapFileContentToJsonArray(contentFollowers);
            userRelationshipData.setArrayFollowers(followersArray);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("The followers file content is invalid.");
        }
        return "redirect:/";
    }

    @Override
    public Object fileWithFollowing(@RequestParam("following") MultipartFile file) throws IOException {
        try {

            String contentFollowing = new String(file.getInputStream().readAllBytes());

            //Mapping the file to a JSON object
            JSONArray followingArray = jsonArrayMapper.mapFileContentWithKeyToJsonArray(contentFollowing);

            //Assign an object in JSONArrays file in order to compare after
            userRelationshipData.setArrayFollowing(followingArray);
            instagramUserComparator.findNonFollowingBackUsers(userRelationshipData);

        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("The followers file content is invalid.");
        }
        return "redirect:/";
    }

    //TODO we correctly process the message however the message appears as not resolved by thymeleaf.
    //TODO Add this on swagger.
    @PostMapping("/uploadStringFormat")
    public String postJsonText(@RequestBody Map<String, String> body) {
        String jsonFollowers = body.get("jsonFollowers");
        String jsonFollowing = body.get("jsonFollowing");
        //Mapping to a JSON object
        JSONArray followersArray = jsonArrayMapper.mapFileContentToJsonArray(jsonFollowers);
        JSONArray followingArray = jsonArrayMapper.mapFileContentWithKeyToJsonArray(jsonFollowing);

        //Assign an object in JSONArrays file in order to compare after
        userRelationshipData.setArrayFollowers(followersArray);

        userRelationshipData.setArrayFollowing(followingArray);

        instagramUserComparator.findNonFollowingBackUsers(userRelationshipData);

        return "redirect:/";
    }

    @GetMapping("/")
    public String displayTable(Model model) {
        Set<String> difference = instagramUserComparator.getNotFollowingBack();
        model.addAttribute("difference", difference);
        return "index";
    }

    @GetMapping("/getUsername")
    public String handleFileUpload(@RequestParam("username") String username) {

        return "index";
    }

}