package com.outbrain;

import com.outbrain.sphere.SphereClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ilya on 4/27/2016.
 */
public class Sphere_connection_manager {
    // query sphere api and returns result
    public static ArrayList query_sphere(String[] words){
        String words_to_be_searched = String.join(" ",words);
        final HashMap<String, Map> result = new HashMap<>();
        SphereClient sphere_app = new SphereClient();
        Map sphere_response_map = sphere_app.getRecommendtionByTerm(words_to_be_searched);
        result.put("sphere",sphere_response_map);


        get_urls(sphere_response_map.get("items"));
        return get_urls(sphere_response_map.get("items"));;
    }
    // gets the relevant words and their tags processing the data.
    // returns the urls found
    public static String[] get_results_from_sphere(String[] words ,String[] tags){
        return null;
    }
    // get the relevant URL`s from argument(outbrain output)
    public static ArrayList get_urls(Object out_brain_result){
        ArrayList out_brain_list = (ArrayList)out_brain_result;
        if (out_brain_list.size() == 0){
            return new ArrayList();
        }
        System.out.println(out_brain_list.toString());
        ArrayList urls = new ArrayList();
        for (int ind = 0; ind < out_brain_list.size() ; ind++){
            Map out_brain_map = (Map)out_brain_list.get(ind);
            Map url_map = (Map)out_brain_map.get("document");
            String url = url_map.get("url").toString();
            urls.add(url);
        }
        return urls;
        //return Urls;
    }
}
