/*
 *       Copyright© (2019) WeBank Co., Ltd.
 *
 *       This file is part of weid-http-service.
 *
 *       weid-http-service is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       weid-http-service is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with weid-http-service.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.xp.medshare.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * copy from weid-rest-sample
 */
@Slf4j
public class KeyUtil {

    public static final String PRIVATE_KEY_PATH =
        PropertiesUtil.getProperty("privateKey.path");

    private static final String SLASH_CHARACTER = "/";

    public static boolean savePrivateKey(String weId, String privateKey) {

        try {
            if (null == weId) {
                log.error("weId is null");
                return false;
            }

            // get the third paragraph of weId.
            String fileName = weId.substring(weId.lastIndexOf(":") + 1);

            // check whether the path exists or not, then create the path and return.
            String checkPath = FileUtil.checkDir(PRIVATE_KEY_PATH);
            String filePath = checkPath + fileName;

            log.info("save private key into file, weId={}, filePath={}", weId, filePath);

            // save the private key information as the file name for the third paragraph of weId.
            FileUtil.saveFile(filePath, privateKey);
            return true;
        } catch (Exception e) {
            log.error("savePrivateKey error", e);
        }
        return false;
    }

    public static String getPrivateKeyByWeId(String weId) {

        if (null == weId) {
            log.error("weId is null");
            return StringUtils.EMPTY;
        }

        // get the third paragraph of weId.
        String fileName = weId.substring(weId.lastIndexOf(":") + 1);

        // check whether the path exists or not, then create the path and return.
        String checkPath = FileUtil.checkDir(PRIVATE_KEY_PATH);
        String filePath = checkPath + fileName;

        log.info("get private key from file, weId={}, filePath={}", weId, filePath);

        // get private key information from a file according to the third paragraph of weId.
        String privateKey = FileUtil.getDataByPath(filePath);
        return privateKey;
    }

//    public static String getDataByPath(String path) {
//
//        log.info("get data form [{}]", path);
//        FileInputStream fis = null;
//        String str = null;
//        try {
//            fis = new FileInputStream(path);
//            byte[] buff = new byte[fis.available()];
//            int size = fis.read(buff);
//            if (size > 0) {
//                str = new String(buff, StandardCharsets.UTF_8);
//            }
//        } catch (FileNotFoundException e) {
//            log.error("the file path is not exists", e);
//        } catch (IOException e) {
//            log.error("getDataByPath error", e);
//        } finally {
//            IOUtils.closeQuietly(fis);
//        }
//        return str;
//    }
//
//    public static String checkDir(String path) {
//
//        String checkPath = path;
//
//        // stitching the last slash.
//        if (!checkPath.endsWith(SLASH_CHARACTER)) {
//            checkPath = checkPath + SLASH_CHARACTER;
//        }
//
//        // check the path, create the path when it does not exist.
//        File checkDir = new File(checkPath);
//        if (!checkDir.exists()) {
//            boolean success = checkDir.mkdirs();
//            if (!success) {
//                log.error("checkDir.mkdirs");
//            }
//        }
//        return checkPath;
//    }
//
//    public static String saveFile(String filePath, String dataStr) {
//
//        log.info("save data in to [{}]", filePath);
//        OutputStreamWriter ow = null;
//        FileOutputStream fos = null;
//        try {
//            File file = new File(filePath);
//            fos = new FileOutputStream(file);
//            ow = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
//            ow.write(dataStr);
//            return file.getAbsolutePath();
//        } catch (IOException e) {
//            log.error("writer file exception", e);
//        } finally {
//            IOUtils.closeQuietly(ow);
//            IOUtils.closeQuietly(fos);
//        }
//        return StringUtils.EMPTY;
//    }

}
