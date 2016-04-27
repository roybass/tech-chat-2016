package com.outbrain;
import com.fasterxml.jackson.databind.util.TypeKey;
import com.outbrain.apiai.ApiAiClient;
import com.outbrain.sphere.SphereClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

import javax.validation.constraints.Null;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat_runner {
    public static void main(String[] args) {
        //String query_string = "Hi. What's the latest comment from trump on Mark zuckerbergs keynote Can you tell me the result of yesterday's UCL matches Real world stuff.   Whats going on with Donald trump Tell me about how the presidential election in America is going";
        //use_open_nlp_api(query_string);
        String query_string = "How about Bernie's ";
        //String[] sentences = devide_to_sentences(query_string);
        Object[] words_and_tags = NLP_processor.analyze_POS_sentence(query_string);
        String[] words = (String[])words_and_tags[0];
        String[] tags = (String[])words_and_tags[1];
        Sphere_connection_manager.get_results_from_sphere(words,tags);
        Sphere_connection_manager.query_sphere(words);
        /*
        query_string = "Tell me about how the presidential election in America is going";
        use_open_nlp_api(query_string);

        query_string = "Hi. What's the latest comment from trump on Mark zuckerbergs keynote";
        use_open_nlp_api(query_string);
        */

        /*
        ApiAiClient ai_app = new ApiAiClient();
        Map ai_response_map = ai_app.getResponse(query_string);
        final HashMap<String, Map> result = new HashMap<>();
        result.put("api.ai",ai_response_map);
        for (String name: result.keySet()){
            Map ai_map = result.get(name);
            System.out.println(name + "\n");
            System.out.println(ai_map + "\n");
            Object ai_result_map = ai_map.get("result");
            System.out.println(ai_result_map.toString());

        }

        SphereClient sphere_app = new SphereClient();
        Map sphere_response_map = sphere_app.getRecommendtionByTerm(query_string);
        result.put("sphere",sphere_response_map);

        for (String name: result.keySet()){
            Map map = result.get(name);
            System.out.println(name + "\n");
            System.out.println(map + "\n");
            //Object result_map = map.get("result");
            //System.out.println(result_map.toString());

        }
        get_urls(sphere_response_map.get("items"));
        /*for(Map.Entry<String,String> entry : ai_response_map.entrySet()){
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
        // ChatController app = new ChatController();
        //Map app.api_query("Barack Obama");
        //app.
        */
    }







}
