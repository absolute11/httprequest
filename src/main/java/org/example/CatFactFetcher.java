package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CatFactFetcher {

    public static void main(String[] args) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();) {
            HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");
            CloseableHttpResponse response = httpClient.execute(request);

            ObjectMapper objectMapper = new ObjectMapper();
            CatFact[] catFacts = objectMapper.readValue(response.getEntity().getContent(), CatFact[].class);

            List<CatFact> filteredFacts = Arrays.stream(catFacts)
                    .filter(fact -> fact.getUpvotes() != null && fact.getUpvotes() > 0)
                    .collect(Collectors.toList());

            for (CatFact fact : filteredFacts) {
                System.out.println("ID: " + fact.getId());
                System.out.println("Text: " + fact.getText());
                System.out.println("Type: " + fact.getType());
                System.out.println("User: " + fact.getUser());
                System.out.println("Upvotes: " + fact.getUpvotes());
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}