package com.lifestores.noc.service;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;

@Service
public class NocService {

    @Scheduled(fixedRate = 120000)
    public void queryHealthCheck() throws IOException, InterruptedException {

        //marketplace
        try {
            JSONObject marketPlaceResponse = new JSONObject(queryApi("https://marketplace.lifestoreshealthcare.com/api/health-check"));
            System.out.println(marketPlaceResponse);
        } catch (Exception e)
        {
            Response response = sendSlack("Marketplace");
            System.out.println(response);
        }

        //marketplace helper
        try {
            JSONObject marketPlaceResponse = new JSONObject(queryApi("https://marketplace-helper.lifestoreshealthcare.com/api/health-check"));
            System.out.println(marketPlaceResponse);
        } catch (Exception e)
        {
            Response response = sendSlack("Marketplace Helper");
            System.out.println(response);
        }

        //Erp
        try {
            JSONObject marketPlaceResponse = new JSONObject(queryApi("https://erp.lifestoreshealthcare.com/api/health-check"));
            System.out.println(marketPlaceResponse);
        } catch (Exception e)
        {
            Response response = sendSlack("ERP");
            System.out.println(response);
        }

        //Erp Helper
        try {
            JSONObject marketPlaceResponse = new JSONObject(queryApi("https://erp-helper.lifestoreshealthcare.com/api/health-check"));
            System.out.println(marketPlaceResponse);
        } catch (Exception e)
        {
            Response response = sendSlack("ERP Helper");
            System.out.println(response);
        }

        System.out.println("Cron successful");

    }

    public String queryApi(String api) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(
                        URI.create(api)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();

    }

    public Response sendSlack(String app) throws IOException, InterruptedException {

        Date date = new Date();
        long time = date.getTime() / 1000;

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n\"channel\":\"health-monitoring\",\n\"attachments\":[\n{\n\t\"mrkdwn_in\": [\"text\"],\n\"color\": \"#d62e0d\",\n\"text\": \""+ app +" (production) is down!\",\n\"footer\": \"Lifestores Healthcare\",\n\"footer_icon\": \"http://lifestoreshealthcare.com/LH%20Flower.png\",\n\"ts\": "+ time +"\n}\n]\n}");
        Request request = new Request.Builder()
                .url("https://slack.com/api/chat.postMessage")
                .method("POST", body)
                .addHeader("Authorization", "Bearer xoxb-792270355745-4420970078018-E1apI9cYMC6xsCni3sys4S6A")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

}
