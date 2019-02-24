package com.example.shaleenjain.contactapp.utils;

import android.util.Base64;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class SmsSender {
    // Find your Account Sid and Auth Token at twilio.com/console
    public static final String ACCOUNT_SID = "AC99c1ecb8a760a83a8edbc6f16ab71c8b";
    public static final String AUTH_TOKEN = "902bfd03f71923dd73fbe7d5fffec270";

    public static Call<ResponseBody> sendSMS(String to, String text) {
        TwilloService service = TwilloService.Creator.newTwilloService();

        String auth = "Basic " + Base64.encodeToString(
                (ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes(), Base64.NO_WRAP);

        return service.sendSMS(auth, to, "+14086101599", text);
    }
}
