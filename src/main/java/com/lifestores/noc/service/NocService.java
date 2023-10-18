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

        //healthcheck 1
        try {
            JSONObject jsonResponse = new JSONObject(queryApi("https://domain.com/api/health-check"));
            System.out.println(jsonResponse);
        } catch (Exception e)
        {
            Response response = sendSlack("App Name");
            System.out.println(response);
        }

        //healthcheck 2
        try {
            JSONObject jsonResponse = new JSONObject(queryApi("https://domain.com/api/health-check"));
            System.out.println(jsonResponse);
        } catch (Exception e)
        {
            Response response = sendSlack("App Name");
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
        RequestBody body = RequestBody.create(mediaType, "{\n\"channel\":\"health-monitoring\",\n\"attachments\":[\n{\n\t\"mrkdwn_in\": [\"text\"],\n\"color\": \"#d62e0d\",\n\"text\": \""+ app +" (production) is down!\",\n\"footer\": \"Lifestores Healthcare\",\n\"footer_icon\": \"http://www.domain.com/logo.png\",\n\"ts\": "+ time +"\n}\n]\n}");
        Request request = new Request.Builder()
                .url("https://slack.com/api/chat.postMessage")
                .method("POST", body)
                .addHeader("Authorization", "Bearer Token")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

}
