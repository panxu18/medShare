package com.xp.medshare.contract.utils;

import com.xp.medshare.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

@Slf4j
public class ContractStoreHelper {
    public static final String CONTRACT_ADDRESS_PATH =
            PropertiesUtil.getProperty("contract.address");

    public static void store(String name, String address) throws IOException {
        File file = getFileByName(name);
        Properties prop = new Properties();
        prop.setProperty("address", address);
        saveFile(prop, file);
        log.info("store contract name:{}, path:{}", name, file.getAbsolutePath());
    }

    public static String load(String name) throws Exception {
        File file = getFileByName(name);
        Properties prop = new Properties();
        prop.load(new FileInputStream(file));
        String contractAddress = prop.getProperty("address");
        if (contractAddress == null || contractAddress.trim().equals("")) {
            throw new Exception(" load Asset contract address failed, please deploy it first. ");
        }
        log.info(" load Asset address from {}, address is {}", name + ".properties", contractAddress);
        return contractAddress;
    }

    public static boolean isDeployed(String name) throws IOException {
        File file = getFileByName(name);
        return file.exists();
    }

    private static File getFileByName(String name) {
        String dir = checkAndCreateDir(CONTRACT_ADDRESS_PATH);
        File directory = new File(dir);
        File file = new File(directory, name + ".properties");
        return file;
    }

    public static String checkAndCreateDir(String path) {
        String checkPath = path;
        File checkDir = new File(path);
        if (!checkDir.exists()) {
            boolean success = checkDir.mkdirs();
            if (!success) {
                log.error("checkDir.mkdirs");
            }
        }
        return checkPath;
    }

    public static String saveFile(Properties properties, File file) throws IOException {
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        properties.store(fileOutputStream, "contract address");
        return file.getAbsolutePath();
    }


}
