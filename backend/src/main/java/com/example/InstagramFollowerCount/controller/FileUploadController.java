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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
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
    @PostMapping("/uploadFollowers")
    public Object fileWithFollowers(@RequestPart("followers") MultipartFile file) throws IOException {
        try {
            String contentFollowers = new String(file.getInputStream().readAllBytes());
            JSONArray followersArray = jsonArrayMapper.mapFileContentToJsonArray(contentFollowers);
            userRelationshipData.setArrayFollowers(followersArray);
            
            return ResponseEntity.ok().body(Map.of("status", "success", "message", "Followers data processed successfully"));
        } catch (Exception exception) {
            LOGGER.error("Error processing followers file", exception);
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error", 
                "message", "The followers file content is invalid: " + exception.getMessage()
            ));
        }
    }

    @Override
    @PostMapping("/uploadFollowing")
    public Object fileWithFollowing(@RequestParam("following") MultipartFile file) throws IOException {
        try {
            String contentFollowing = new String(file.getInputStream().readAllBytes());
            JSONArray followingArray = jsonArrayMapper.mapFileContentWithKeyToJsonArray(contentFollowing);
            
            if (followingArray == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "error", 
                    "message", "The following file content is invalid or missing the required key"
                ));
            }
            
            userRelationshipData.setArrayFollowing(followingArray);
            instagramUserComparator.findNonFollowingBackUsers(userRelationshipData);
            
            return ResponseEntity.ok().body(Map.of("status", "success", "message", "Following data processed successfully"));
        } catch (Exception exception) {
            LOGGER.error("Error processing following file", exception);
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error", 
                "message", "The following file content is invalid: " + exception.getMessage()
            ));
        }
    }

    @PostMapping("/uploadStringFormat")
    public ResponseEntity<Map<String, Object>> postJsonText(@RequestBody Map<String, String> body) {
        try {
            String jsonFollowers = body.get("jsonFollowers");
            String jsonFollowing = body.get("jsonFollowing");
            
            if (jsonFollowers == null || jsonFollowing == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "error", 
                    "message", "Both jsonFollowers and jsonFollowing are required"
                ));
            }
            
            // Mapping to JSON objects
            JSONArray followersArray = jsonArrayMapper.mapFileContentToJsonArray(jsonFollowers);
            JSONArray followingArray = jsonArrayMapper.mapFileContentWithKeyToJsonArray(jsonFollowing);
            
            if (followingArray == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "error", 
                    "message", "The following JSON data is invalid or missing the required key"
                ));
            }
            
            // Process data
            userRelationshipData.setArrayFollowers(followersArray);
            userRelationshipData.setArrayFollowing(followingArray);
            instagramUserComparator.findNonFollowingBackUsers(userRelationshipData);
            
            return ResponseEntity.ok().body(Map.of(
                "status", "success", 
                "message", "JSON data processed successfully"
            ));
        } catch (Exception e) {
            LOGGER.error("Error processing JSON string data", e);
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error", 
                "message", "Error processing JSON data: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/")
    public Object displayTable(Model model, @RequestHeader(value = "Accept", required = false) String acceptHeader) {
        Set<String> difference = instagramUserComparator.getNotFollowingBack();
        
        // Check if it's an API request (expecting JSON)
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            Map<String, Object> response = new HashMap<>();
            response.put("difference", difference);
            response.put("count", difference != null ? difference.size() : 0);
            return ResponseEntity.ok().body(response);
        }
        
        // Otherwise, return the Thymeleaf template for browser requests
        model.addAttribute("difference", difference);
        return "index";
    }
}