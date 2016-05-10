package com.outbrain.sphere;

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
public class SphereClient {

  private static final String apiKey = "2e0be4e2bd994ea68cd2dbf80ccdec99";
  private static final String SPHERE_BASE_URL = "https://sphere-dev.outbrain.com/api/v1/";
  private static final String USER_ID = "4bdc7b9c-1dfb-409f-ab6f-cbf3178d31c4";

  private final RestTemplate httpClient;

  public SphereClient() {
    this.httpClient = new RestTemplate();
  }

  public Map getRecommendtionByTerm(final String searchTerm) {

    System.out.println("sphere query searchTerm : "+searchTerm);
    final URI uri = getUri("/recommendations/documents?limit=20&filter=titlePhrase:", searchTerm);
    //System.out.println(searchTerm );

    final RequestEntity requestEntity = new RequestEntity(getHeaders(), HttpMethod.GET, uri);
    final ResponseEntity<Map> response = httpClient.exchange(requestEntity, Map.class);
    return response.getBody();
  }


  private URI getUri(String path, String searchTerm) {
    try {
      return URI.create(SPHERE_BASE_URL + path + URLEncoder.encode(searchTerm, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public HttpHeaders getHeaders() {
    final HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    headers.add("Authorization", "API_KEY " + apiKey);
    headers.add("X-USER-ID", USER_ID);
    return headers;
  }
}
