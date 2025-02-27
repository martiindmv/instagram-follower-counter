package com.example.InstagramFollowerCount.util;

import org.json.JSONArray;
import org.springframework.stereotype.Component;

@Component
public class UserRelationshipData {
    private JSONArray jsonArrayFollowers;
    private JSONArray jsonArrayFollowing;

    public JSONArray getWhoFollowsYouArray() {
        return jsonArrayFollowers;
    }

    public JSONArray getWhoYouAreFollowingArray() {
        return jsonArrayFollowing;
    }

    public void setArrayFollowers(JSONArray jsonArrayFollowers) {
        this.jsonArrayFollowers = jsonArrayFollowers;
    }

    public void setArrayFollowing(JSONArray jsonArrayFollowing) {
        this.jsonArrayFollowing = jsonArrayFollowing;
    }
}
