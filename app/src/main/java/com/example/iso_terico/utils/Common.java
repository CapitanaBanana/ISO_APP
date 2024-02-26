package com.example.iso_terico.utils;

import com.example.iso_terico.models.GoogleSheetsResponse;
import com.example.iso_terico.models.IGoogleSheets;

public class Common {
    public static String SHEET_NAME = "ranking";


    public static IGoogleSheets IGSGetMethodClient(String baseUrl){
        return GoogleSheetsResponse.getClientGetMethod(baseUrl)
                .create(IGoogleSheets.class);
    }
}
