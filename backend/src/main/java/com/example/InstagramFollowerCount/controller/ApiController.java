package com.example.InstagramFollowerCount.controller;

import com.example.InstagramFollowerCount.util.InstagramUserComparator;
import com.example.InstagramFollowerCount.util.UserRelationshipData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final UserRelationshipData userRelationshipData;
    private final InstagramUserComparator instagramUserComparator;

    @Autowired
    public ApiController(UserRelationshipData userRelationshipData, InstagramUserComparator instagramUserComparator) {
        this.userRelationshipData = userRelationshipData;
        this.instagramUserComparator = instagramUserComparator;
    }

    @GetMapping("/followers")
    public ResponseEntity<String> getFollowers() {
        JSONArray followers = userRelationshipData.getWhoFollowsYouArray();
        if (followers == null) {
            return ResponseEntity.ok().body("[]");
        }
        return ResponseEntity.ok().body(followers.toString());
    }

    @GetMapping("/following")
    public ResponseEntity<String> getFollowing() {
        JSONArray following = userRelationshipData.getWhoYouAreFollowingArray();
        if (following == null) {
            return ResponseEntity.ok().body("[]");
        }
        return ResponseEntity.ok().body(following.toString());
    }

    @GetMapping("/not-following-back")
    public ResponseEntity<Map<String, Object>> getNotFollowingBack() {
        Set<String> notFollowingBack = instagramUserComparator.getNotFollowingBack();

        Map<String, Object> response = new HashMap<>();
        if (notFollowingBack != null) {
            response.put("difference", notFollowingBack);
            response.put("count", notFollowingBack.size());
        } else {
            response.put("difference", Set.of());
            response.put("count", 0);
        }

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/account-stats")
    public ResponseEntity<Map<String, Object>> getAccountStats() {
        Map<String, Object> stats = new HashMap<>();

        // Get data from userRelationshipData
        JSONArray followersArray = userRelationshipData.getWhoFollowsYouArray();
        JSONArray followingArray = userRelationshipData.getWhoYouAreFollowingArray();
        Set<String> notFollowingBack = instagramUserComparator.getNotFollowingBack();

        // Get counts
        int followersCount = followersArray != null ? followersArray.length() : 0;
        int followingCount = followingArray != null ? followingArray.length() : 0;
        int notFollowingBackCount = notFollowingBack != null ? notFollowingBack.size() : 0;

        // Calculate followers you don't follow back (optional additional stat)
        Set<String> followersSet = new HashSet<>();
        Set<String> followingSet = new HashSet<>();

        if (followersArray != null) {
            followersSet = instagramUserComparator.extractUsernamesFromJsonArray(followersArray);
        }

        if (followingArray != null) {
            followingSet = instagramUserComparator.extractUsernamesFromJsonArray(followingArray);
        }

        // People who follow you but you don't follow them
        Set<String> followersNotFollowed = new HashSet<>(followersSet);
        followersNotFollowed.removeAll(followingSet);

        // Compile stats
        stats.put("followers", followersCount);
        stats.put("following", followingCount);
        stats.put("notFollowingBack", notFollowingBackCount);
        stats.put("followersNotFollowed", followersNotFollowed.size());

        return ResponseEntity.ok().body(stats);
    }
}