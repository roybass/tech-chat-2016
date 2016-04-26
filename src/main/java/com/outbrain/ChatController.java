package com.outbrain;

import com.outbrain.apiai.ApiAiClient;
import com.outbrain.sphere.SphereClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rbass on 4/1/16.
 */
@RestController
public class ChatController {

  private final ApiAiClient apiAiClient;
  private final SphereClient sphereClient;

  @Autowired
  public ChatController(final ApiAiClient apiAiClient, final SphereClient sphereClient) {
    this.apiAiClient = apiAiClient;
    this.sphereClient = sphereClient;
  }
  public Map api_query(final String input){
    return apiAiClient.getResponse(input);
  }
  @RequestMapping("/chat")
  @ResponseBody
  public Map handleChat(@RequestParam final String input) {
    final Map apiApiResponse = apiAiClient.getResponse(input);
    final Map sphereResponse = sphereClient.getRecommendtionByTerm(input);
    final HashMap<String, Map> result = new HashMap<>();

    result.put("API.AI", apiApiResponse);
    result.put("Sphere", sphereResponse);
    return result;
  }
}
