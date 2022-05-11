//package com.elcom.search.config;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.*;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class SearchConfig {
//    //Typesafe config
//    public static final String CONFIG_DIR = System.getProperty("user.dir") + File.separator + "config" + File.separator;
//
//    //Constant
//    public static List<String> SEARCH_SERVICE_LIST = new ArrayList<>();
//    public static final Map<String, String> SEARCH_SERVICE_MAP = new HashMap<>();
//    public static final Map<String, List<String>> SEARCH_SERVICE_PATH_MAP = new HashMap<>();
////    public static final Map<String, UploadDTO> SEARCH_DEFINE_MAP = new HashMap<>();
//
//    static {
//        loadConfig();
//    }
//
//    private static void loadConfig() {
//        try {
//            System.out.println("Loading SearchConfig config...");
//            Properties properties = new Properties();
//            properties.load(new InputStreamReader(new FileInputStream(CONFIG_DIR + "upload.properties"), "UTF-8"));
//            Enumeration e = properties.propertyNames();
//
//            while (e.hasMoreElements()) {
//                String key = (String) e.nextElement();
//                String value = properties.getProperty(key);
//                if (key.equalsIgnoreCase("upload.service")) {
//                    String[] arrDomain = value.split(",");
//                    UPLOAD_SERVICE_LIST = Arrays.asList(arrDomain);
//                } else if(key.equalsIgnoreCase("upload.file")){
//                    //Load upload dto json file
//                    loadUploadDTOJson(value);
//                }else {
//                    UPLOAD_SERVICE_MAP.put(key, value);
//                    if (key.contains(".path")) {
//                        String[] arrPath = value.split(",");
//                        List<String> pathList = Arrays.asList(arrPath);
//                        UPLOAD_SERVICE_PATH_MAP.put(key.replace(".path", ""), pathList);
//                    }
//                }
//            }
//            System.out.println("Load UploadConfig config successfull!!!");
//        } catch (IOException ex) {
//            Logger.getLogger(UploadConfig.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//}
