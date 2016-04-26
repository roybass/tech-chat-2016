package com.outbrain.apiai;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by rbass on 4/1/16.
 */
@Component
public class ApiAiClient {

  private static final String SUBSCRIPTION_KEY = "";
  private static final String CLIENT_ACCESS_TOKEN = "9067da86644242bfac98d1fc5a160731";
  private static final String DEVELOPER_ACCESS_TOKEN = "68c46f52197e49e48a6653ec7844b5c6";

  private static final String baseUrl = "https://api.api.ai/v1/query?lang=en&v=20160314&q=";
  private final RestTemplate httpClient;

  public ApiAiClient() {
    this.httpClient = new RestTemplate();
  }


  public Map getResponse(final String input) {
    final String url = getUrl(input);

    final HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    headers.add("Authorization", "Bearer " + CLIENT_ACCESS_TOKEN);
    headers.add("ocp-apim-subscription-key", SUBSCRIPTION_KEY);

    final RequestEntity requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

    final ResponseEntity<Map> response = httpClient.exchange(requestEntity, Map.class);
    return response.getBody();
  }


  private String getUrl(String input) {
    try {
      return baseUrl + URLEncoder.encode(input, "UTF-8");

    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
