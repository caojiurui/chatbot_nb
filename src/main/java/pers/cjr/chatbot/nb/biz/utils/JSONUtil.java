package pers.cjr.chatbot.nb.biz.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {

    /**
     * 将json转化为实体POJO
     *
     * @param jsonStr
     * @param obj
     * @return
     */
    public static <T> Object jsonToObj(String jsonStr, Class<T> obj) {
        T t = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            t = objectMapper.readValue(jsonStr, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 将实体POJO转化为JSONObject
     *
     * @param obj
     * @return
     */
    public static <T> JSONObject objectToJsonObject(T obj) {
        return objectToJsonObject(obj, 0);
    }

    /**
     * 将实体POJO转化为JSONObject
     *
     * @param obj
     * @param processType 0:输出全部， 1:输出时不输出空字符串(包括null)， 2：输出时不输出null, 3:输出时不输出默认值
     * @return
     */
    public static <T> JSONObject objectToJsonObject(T obj, int processType)  {
        return JSONObject.parseObject(objectToJsonStr(obj, processType));
    }

    /**
     * 将实体POJO转化为JSONArray
     *
     * @param obj
     * @return
     */
    public static <T> JSONArray objectToJsonArray(T obj) {
        return objectToJsonArray(obj, 0);
    }

    /**
     * 将实体POJO转化为JSONArray
     *
     * @param obj
     * @param processType 0:输出全部， 1:输出时不输出空字符串(包括null)， 2：输出时不输出null, 3:输出时不输出默认值
     * @return
     */
    public static <T> JSONArray objectToJsonArray(T obj, int processType)  {
        return JSONArray.parseArray(objectToJsonStr(obj, processType));
    }

    /**
     * 将实体POJO转化为JSON字符串
     *
     * @param obj
     * @return
     */
    public static <T> String objectToJsonStr(T obj) {
        return objectToJsonStr(obj, 0);
    }

    /**
     * 将实体POJO转化为JSON字符串
     *
     * @param obj
     * @param processType 0:输出全部， 1:输出时不输出空字符串(包括null)， 2：输出时不输出null, 3:输出时不输出默认值
     * @return
     */
    public static <T> String objectToJsonStr(T obj, int processType)  {
        ObjectMapper mapper = new ObjectMapper();
        // Convert object to JSON string
        String jsonStr = "";
        try {
            if(processType == 1) {
                mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            } else if(processType == 2) {
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            } else if(processType == 3) {
                mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
            } else {
                mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
            }
            jsonStr = mapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    /**
     * json转成map
     * @param jsonStr
     * @return
     * @throws Exception
     */
    public static Map<String, Object> jsonToMap(JSONObject jsonObj) throws Exception {
        Map<String, Object> outMap = new HashMap<String, Object>();
        if(null != jsonObj){
            for (String key : jsonObj.keySet()) {
                outMap.put(key, jsonObj.get(key));
            }
        }
        return outMap;
    }

    /**
     * 将实体POJO转化为List<Map<String,Object>>
     *
     * @param jsonArray
     * @return
     * @throws Exception
     */
    public static List<Map<String,Object>> objectToJsonList(JSONArray jsonArray) throws Exception {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonToMap(jsonArray.getJSONObject(i)));
        }
        return list;
    }
}
