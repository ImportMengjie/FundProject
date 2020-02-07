package com.mengjie.config;

public class API {

    public static String baseURL = "http://192.168.3.5:8080";

    public static String fundListURL = baseURL+"/api/fund?page=%d&pagesize=%d";

    public static String searchFundListURL = baseURL+"/api/fund?keyword=%s";

    public static String fundURL = baseURL+"/api/fund/%s";
}
