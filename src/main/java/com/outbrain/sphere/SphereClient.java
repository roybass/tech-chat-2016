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

  private static final String apiKey = "6110537c404b4657bfa02a4a784ee691";
  private static final String SPHERE_BASE_URL = "https://sphere-dev.outbrain.com/api/v1/";
  private static final String USER_ID = "b9151414-da85-4d93-a4d4-415ce4111a1b"; //UUID standard

  /*  Roy's credentials
  private static final String apiKey = "ccb15738cc9e4a508b9cdcc84cdabdaf";
  private static final String SPHERE_BASE_URL = "https://sphere-dev.outbrain.com/api/v1/";
  private static final String USER_ID = "f21afc32-0c1e-4771-b47e-daeb55803215";
  */
  private final RestTemplate httpClient;

  public SphereClient() {
    this.httpClient = new RestTemplate();
  }

  public Map getRecommendtionByTerm(final String searchTerm) {
    final URI uri = getUri("/recommendations/documents?limit=1&filter=titlePhrase:", searchTerm);
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
