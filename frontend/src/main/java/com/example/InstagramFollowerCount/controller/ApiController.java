package com.example.InstagramFollowerCount.controller;

import com.example.InstagramFollowerCount.util.UserRelationshipData;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final UserRelationshipData userRelationshipData;

    @Autowired
    public ApiController(UserRelationshipData userRelationshipData) {
        this.userRelationshipData = userRelationshipData;
    }

    //TODO This works in a string, now thing about what you want to return whenever the API is called.
    @GetMapping("/followers")
    public String getFollowers() {
        return (userRelationshipData.getWhoFollowsYouArray()).toString();
    }

    @GetMapping("/following")
    public JSONArray getFollowing() {
        return userRelationshipData.getWhoYouAreFollowingArray();
    }
}
