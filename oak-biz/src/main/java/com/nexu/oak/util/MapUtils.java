/**
 *
 * Copyright (C), 2002-2017, 铜板街.
 */
package com.nexu.oak.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dongfeng
 * @version $Id: MapUtils.java, v 0.1 2017-1-11 下午5:53:47 dongfeng Exp $
 */
public class MapUtils {
    public static Map buildKeyValueMap(Object... conditions) {
        Map conditionMap = new HashMap();
        for (int i = 0; i < conditions.length; i += 2) {
            conditionMap.put(conditions[i], conditions[i + 1]);
        }
        return conditionMap;
    }

    
    public static Map<String, String> convertFromString(String mapAsString) {
        Map<String, String> map = new HashMap<String, String>();

        String actualInput = mapAsString.substring(1, mapAsString.length() - 1);

        String[] keyValuePairs = actualInput.split(",");
        for (String keyValuePair : keyValuePairs) {
            String[] keyValue = keyValuePair.split("=");
            map.put(keyValue[0].trim(), keyValue.length > 1 ? keyValue[1].trim() : "");
        }

        return map;
    }
}
