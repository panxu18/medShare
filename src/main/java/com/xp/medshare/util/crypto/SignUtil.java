package com.xp.medshare.util.crypto;

import com.alibaba.fastjson.JSON;
import com.webank.weid.util.DataToolUtils;

import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class SignUtil {
    public static String sign(String data, String sk) {
        return DataToolUtils.secp256k1Sign(data, new BigInteger(sk));
    }

    public static boolean verify(String data, String signature, String pk) {
        return DataToolUtils.verifySecp256k1Signature(data, signature, new BigInteger(pk));
    }

    public static String sign(Object data, String sk) {
        String message = object2String(data);
        return sign(message, sk);
    }

    public static String verify(Object data, String sk) {
        String message = object2String(data);
        return verify(message, sk);
    }

    private static String object2String(Object data) {
        if (!(data instanceof Map)) {
            return data.toString();
        }
        Map<String, Object> map = (Map<String, Object>) JSON.toJSON(data);
        TreeMap<String, Object> treeMap = map.entrySet().stream().collect(Collectors
                .toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, TreeMap::new));
        String message = treeMap.entrySet().stream().map(Map.Entry::getValue).map(a->object2String(a)).collect(Collectors.joining("&"));
        return message;
    }

}
