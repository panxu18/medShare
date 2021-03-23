package com.xp.medshare.util.crypto;

import com.alibaba.fastjson.JSON;
import com.webank.weid.util.DataToolUtils;
import com.xp.medshare.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
public class SignUtil {
    public static String sign(String data, String sk) {
        log.info("sign message {} sk {}",data, sk);
        return DataToolUtils.secp256k1Sign(data, new BigInteger(sk));
    }

    public static boolean verify(String data, String signature, String pk) {
        log.info("verify message {} pk {}",data, pk);
        return DataToolUtils.verifySecp256k1Signature(data, signature, new BigInteger(pk));
    }

    public static String sign(Object data, String pk) {
        try {
            data = ObjectUtil.object2Map(data);
        } catch (IllegalAccessException e) {
            log.info("sign failed", e);
        }
        String message = object2String(data);
        return sign(message, pk);
    }

    public static boolean verify(Object data, String signature, String pk) {
        try {
            data = ObjectUtil.object2Map(data);
        } catch (IllegalAccessException e) {
            log.info("sign failed", e);
        }
        String message = object2String(data);
        return verify(message, signature, pk);
    }

    private static String object2String(Object data) {
        if (!(data instanceof Map)) {
            return data.toString();
        }
        Map<String, Object> map = (Map<String, Object>) JSON.toJSON(data);
        TreeMap<String, Object> treeMap = map.entrySet().stream().filter(e -> e.getValue() != null).collect(Collectors
                .toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, TreeMap::new));
        String message = treeMap.entrySet().stream().map(Map.Entry::getValue).map(a -> object2String(a)).collect(Collectors.joining("&"));
        return message;
    }

    public static void main(String[] args) {
        String message = "期权&看看&老刘&呵呵&&did:weid:1:0x4e0e02178d83ebeb1b03428de152591a119d9acd&2000018&2021-03-20 21:20:27&1171d251-4f0e-42d3-a1fe-6995e46b7578&did:weid:1:0xf11254d7cb57ad5f04f7758457084fb9bf718389";
        String message2 = "期权&看看&老刘&呵呵&&did:weid:1:0x4e0e02178d83ebeb1b03428de152591a119d9acd&2000018&2021-03-20 21:20:27&1171d251-4f0e-42d3-a1fe-6995e46b7578&did:weid:1:0xf11254d7cb57ad5f04f7758457084fb9bf718389";
        String sk = "7823032845421106700993702283784365850412617086487423212416568316027956920400";
        String pk = "10402694065622818871591252065997178769393222836748060918298333223670858323285294355946571379092872916492269880848757747591090581279119953290720492261216240";
        String signature = sign(message, sk);
        Assert.isTrue(verify(message2, signature, pk));
    }

}
