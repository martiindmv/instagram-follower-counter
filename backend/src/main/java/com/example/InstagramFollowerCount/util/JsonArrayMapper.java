package com.example.InstagramFollowerCount.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class JsonArrayMapper {
    private static final Logger LOGGER = LogManager.getLogger(JsonArrayMapper.class);

    public JSONArray mapFileContentToJsonArray(String jsonContent) {
        JSONArray jsonArray = new JSONArray(jsonContent);
        LOGGER.info("Followers Completed!");
        return jsonArray;
    }

    public JSONArray mapFileContentWithKeyToJsonArray(String jsonContent) {
        JSONObject jsonObject = new JSONObject(jsonContent);
        if (jsonObject.has("relationships_following")) {
            JSONArray jsonArray = jsonObject.getJSONArray("relationships_following");
            LOGGER.info("Following Completed!");
            return jsonArray;
        } else {
            LOGGER.error("Key of the file is not correct!");
        }
        return null;
    }
}
