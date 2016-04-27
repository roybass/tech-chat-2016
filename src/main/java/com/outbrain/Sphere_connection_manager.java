package com.outbrain;

import com.outbrain.sphere.SphereClient;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ilya on 4/27/2016.
 */
public class Sphere_connection_manager {
    // query sphere api and returns result
    public static void query_sphere(String[] words){
        String words_to_be_searched = String.join(" ",words);
        final HashMap<String, Map> result = new HashMap<>();
        SphereClient sphere_app = new SphereClient();
        Map sphere_response_map = sphere_app.getRecommendtionByTerm(words_to_be_searched);
        result.put("sphere",sphere_response_map);
        for (String name: result.keySet()){
            Map map = result.get(name);
            System.out.println(name + "\n");
            System.out.println(map + "\n");
            //Object result_map = map.get("result");
            //System.out.println(result_map.toString());

        }
        //System.out.println(words_to_be_searched + "\n");

        get_urls(sphere_response_map.get("items"));
        return;
    }
    // gets the relevant words and their tags processing the data.
    // returns the urls found
    public static String[] get_results_from_sphere(String[] words ,String[] tags){
        return null;
    }
    // get the relevant URL`s from argument(outbrain output)
    public static String[] get_urls(Object out_brain_result){
        String out_brain = out_brain_result.toString();
        System.out.println(out_brain);
        //String[] urls = new String[100];
        String[] strings = out_brain.split(",");
        String ptrn = ".*url=(.*)";
        Pattern ptrn_obj = Pattern.compile(ptrn);
        //String[] Urls
        for (String iterator : strings){
            Matcher m = ptrn_obj.matcher(iterator);
            if (m.find()){
                System.out.println("url is: " + m.group(1));
            }else{

            }
        }
        return null; // TODO fix the return val
        //return Urls;
    }
}
