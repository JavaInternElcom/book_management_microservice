package com.elcom.search.config;

import com.elcom.dto.RabbitMQType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchConfig {
    //Typesafe config
    public static final String CONFIG_DIR = System.getProperty("user.dir") + File.separator + "config" + File.separator;

    //Constant
    public static List<String> SEARCH_SERVICE_LIST = new ArrayList<>();
    public static final Map<String, String> SEARCH_SERVICE_MAP = new HashMap<>();
    public static final Map<String, List<String>> SEARCH_SERVICE_PATH_MAP = new HashMap<>();
    //Map chứa kiểu rabbit (RPC, Worker, Pub/Sub) cho path + method
    public static final Map<String, String> RABBIT_TYPE_MAP = new HashMap<>();

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try {
            System.out.println("Loading SearchConfig config...");
            Properties properties = new Properties();
            properties.load(new InputStreamReader(new FileInputStream(CONFIG_DIR + "search.properties"), "UTF-8"));
            Enumeration e = properties.propertyNames();

            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = properties.getProperty(key);
                if (key.equalsIgnoreCase("search.service")) {
                    String[] arrDomain = value.split(",");
                    SEARCH_SERVICE_LIST = Arrays.asList(arrDomain);
                } else if(key.equalsIgnoreCase("search.file")){
                    //Load upload dto json file
                    loadRabbitJson(value);
                }else {
                    SEARCH_SERVICE_MAP.put(key, value);
                    if (key.contains(".path")) {
                        String[] arrPath = value.split(",");
                        List<String> pathList = Arrays.asList(arrPath);
                        SEARCH_SERVICE_PATH_MAP.put(key.replace(".path", ""), pathList);
                    }
                }
            }
            System.out.println("Load UploadConfig config successfull!!!");
        } catch (IOException ex) {
            Logger.getLogger(SearchConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void loadRabbitJson(String jsonFile) {
        try {
            FileReader reader = new FileReader(CONFIG_DIR + jsonFile);
            ObjectMapper mapper = new ObjectMapper();
            List<RabbitMQType> rabbitMqTypeList = mapper.readValue(reader, new TypeReference<List<RabbitMQType>>() {
            });
            if (rabbitMqTypeList != null && rabbitMqTypeList.size() > 0) {
                rabbitMqTypeList.forEach((tmp) -> {
                    System.out.println(tmp.getMethod() + " " + tmp.getPath() + " => " + tmp.getRabbit());
                    RABBIT_TYPE_MAP.put(tmp.getMethod() + " " + tmp.getPath(), tmp.getRabbit());
                });
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SearchConfig.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SearchConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
