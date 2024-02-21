package com.example.iso_terico.utils;

import com.example.iso_terico.models.GoogleSheetsResponse;
import com.example.iso_terico.models.IGoogleSheets;

public class Common {
    public static String SHEET_NAME = "preguntas";
    public static String GOOGLE_SHEET_ID = "1eCEHGeCz1Nb-CxZFvduh0J3-eTRTEim_8iURbV5Ytcc";
    public static String BASE_URL = "https://script.google.com/macros/s/AKfycbwhxEY35SqaccXgyVNVR_q4jhpA1fsN5ngwoWc_-_zQJuEkoJBopc-Fq6Xu60OHM087/";

    public static IGoogleSheets IGSGetMethodClient(String baseUrl){
        return GoogleSheetsResponse.getClientGetMethod(baseUrl)
                .create(IGoogleSheets.class);
    }
}
