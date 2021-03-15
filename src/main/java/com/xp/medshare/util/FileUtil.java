package com.xp.medshare.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FileUtil {
    private static final String SLASH_CHARACTER = "/";

    public static String saveFile(String filePath, String dataStr) {

        log.info("save data in to [{}]", filePath);
        OutputStreamWriter ow = null;
        FileOutputStream fos = null;
        try {
            File file = new File(filePath);
            fos = new FileOutputStream(file);
            ow = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            ow.write(dataStr);
            return file.getAbsolutePath();
        } catch (IOException e) {
            log.error("writer file exception", e);
        } finally {
            IOUtils.closeQuietly(ow);
            IOUtils.closeQuietly(fos);
        }
        return StringUtils.EMPTY;
    }

    public static String getDataByPath(String path) {

        log.info("get data form [{}]", path);
        FileInputStream fis = null;
        String str = null;
        try {
            fis = new FileInputStream(path);
            byte[] buff = new byte[fis.available()];
            int size = fis.read(buff);
            if (size > 0) {
                str = new String(buff, StandardCharsets.UTF_8);
            }
        } catch (FileNotFoundException e) {
            log.error("the file path is not exists", e);
        } catch (IOException e) {
            log.error("getDataByPath error", e);
        } finally {
            IOUtils.closeQuietly(fis);
        }
        return str;
    }

    public static String checkDir(String path) {

        String checkPath = path;

        // stitching the last slash.
        if (!checkPath.endsWith(SLASH_CHARACTER)) {
            checkPath = checkPath + SLASH_CHARACTER;
        }

        // check the path, create the path when it does not exist.
        File checkDir = new File(checkPath);
        if (!checkDir.exists()) {
            boolean success = checkDir.mkdirs();
            if (!success) {
                log.error("checkDir.mkdirs");
            }
        }
        return checkPath;
    }
}
