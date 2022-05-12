package com.elcom.report.config;

import com.elcom.dto.RabbitMQType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportConfig {
    //Typesafe config
    public static final String CONFIG_DIR = System.getProperty("user.dir") + File.separator + "config" + File.separator;

    //Constant
    public static List<String> REPORT_SERVICE_LIST = new ArrayList<>();
    public static final Map<String, String> REPORT_SERVICE_MAP = new HashMap<>();
    public static final Map<String, List<String>> REPORT_SERVICE_PATH_MAP = new HashMap<>();
    //Map chứa kiểu rabbit (RPC, Worker, Pub/Sub) cho path + method
    public static final Map<String, String> RABBIT_TYPE_MAP = new HashMap<>();

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try {
            System.out.println("Loading ReportConfig config...");
            Properties properties = new Properties();
            properties.load(new InputStreamReader(new FileInputStream(CONFIG_DIR + "report.properties"), "UTF-8"));
            Enumeration e = properties.propertyNames();

            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = properties.getProperty(key);
                if (key.equalsIgnoreCase("report.service")) {
                    String[] arrDomain = value.split(",");
                    REPORT_SERVICE_LIST = Arrays.asList(arrDomain);
                } else if(key.equalsIgnoreCase("report.file")){
                    //Load upload dto json file
                    loadRabbitJson(value);
                }else {
                    REPORT_SERVICE_MAP.put(key, value);
                    if (key.contains(".path")) {
                        String[] arrPath = value.split(",");
                        List<String> pathList = Arrays.asList(arrPath);
                        REPORT_SERVICE_PATH_MAP.put(key.replace(".path", ""), pathList);
                    }
                }
            }
            System.out.println("Load UploadConfig config successfull!!!");
        } catch (IOException ex) {
            Logger.getLogger(ReportConfig.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ReportConfig.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReportConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
