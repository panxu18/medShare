package com.xp.medshare.util;

import com.alibaba.fastjson.JSONObject;
import com.xp.medshare.model.domodel.CryptoDo;
import com.xp.medshare.util.crypto.Crypto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CryptoUtil {
    public static final String CRYPTO_PATH =
            PropertiesUtil.getProperty("crypto.path");

    public static final String RECRYPTO_PATH =
            PropertiesUtil.getProperty("recrypto.path");

    public static boolean saveCrypto(String id, CryptoDo crypto) {

        try {
            String fileName = id;
            String checkPath = FileUtil.checkDir(CRYPTO_PATH);
            String filePath = checkPath + fileName;
            log.info("save crypto into file, id={}, filePath={}", id, filePath);
            FileUtil.saveFile(filePath, JSONObject.toJSONString(crypto));
            return true;
        } catch (Exception e) {
            log.error("savePrivateKey error", e);
        }
        return false;
    }

    public static Crypto getCrypto(String id) {
        String fileName = id;
        String checkPath = FileUtil.checkDir(CRYPTO_PATH);
        String filePath = checkPath + fileName;
        log.info("get crypto from file, id={}, filePath={}", id, filePath);

        String cryptoJson = FileUtil.getDataByPath(filePath);
        Crypto crypto = JSONObject.parseObject(cryptoJson, Crypto.class);
        return crypto;
    }

    public static boolean saveReCrypto(String id, CryptoDo crypto) {

        try {
            String fileName = id;
            String checkPath = FileUtil.checkDir(RECRYPTO_PATH);
            String filePath = checkPath + fileName;
            log.info("save crypto into file, id={}, filePath={}", id, filePath);
            FileUtil.saveFile(filePath, JSONObject.toJSONString(crypto));
            return true;
        } catch (Exception e) {
            log.error("savePrivateKey error", e);
        }
        return false;
    }





}
