package com.example.InstagramFollowerCount.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class InstagramUserComparator {
    private static final Logger LOGGER = LogManager.getLogger(InstagramUserComparator.class);
    private Set<String> notFollowingBack;

    public Set<String> findNonFollowingBackUsers(UserRelationshipData userRelationshipData) {
        Set<String> followersSet = extractUsernamesFromJsonArray(userRelationshipData.getWhoFollowsYouArray());
        Set<String> followingSet = extractUsernamesFromJsonArray(userRelationshipData.getWhoYouAreFollowingArray());

        // Find the difference (elements in followingSet but not in followersSet)
        notFollowingBack = new HashSet<>(followingSet);
        notFollowingBack.removeAll(followersSet);

        LOGGER.info("Users you follow who don't follow you: " + notFollowingBack);
        return notFollowingBack;
    }

    public Set<String> extractUsernamesFromJsonArray(JSONArray jsonArray) {
        Set<String> arrayToSet = new HashSet<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject stringListData = jsonArray.getJSONObject(i).getJSONArray("string_list_data").getJSONObject(0);
            String value = stringListData.getString("value");
            arrayToSet.add(value);
        }
        return arrayToSet;
    }

    public Set<String> getNotFollowingBack() {
        return notFollowingBack;
    }
}
