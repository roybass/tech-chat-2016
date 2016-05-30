package com.outbrain;

import com.outbrain.NLP_processor;
import com.outbrain.Sphere_connection_manager;
import com.outbrain.apiai.ApiAiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tal on 16/05/2016.
 */
public class Chat_runner_production {
    public static void main(String[] args) {
    }
    public static String check_with_api_ai(String string){
        ApiAiClient ai_app = new ApiAiClient();
        Map ai_response_map = ai_app.getResponse(string);
        HashMap result = (HashMap) ai_response_map.get("result");
        if(relevant_action(result.get("action").toString())){
            HashMap fulfillment;
            try{
                fulfillment = (HashMap) result.get("fulfillment");
                if(fulfillment.get("speech").toString() != null)
                   return fulfillment.get("speech").toString() + " ";
            } catch (NullPointerException e){}
        }
        return "";
    }
    public static boolean relevant_action(String action){
        if(action.indexOf("smalltalk")>=0)
            return true;
        if(action.indexOf("name.save")>=0)
            return true;
        return false;
    }
    public static String get_Sphere_content(String query_string){
        if(query_string.length() >254){
            return "your message is too long... try something shorter :-)\n";
        }
        String api_ai_res = check_with_api_ai(query_string);
        if(api_ai_res !="")
            return api_ai_res;

        if(!check_if_relevant(query_string))
            return "please don`t give me urls, ask me something else\n";

        String result = new String();
        result = "the results we found:\n";
        String[] sentences = NLP_processor.devide_to_sentences(query_string);
        for (String sentence : sentences) {
            if(NLP_processor.analyze_POS_sentence(sentence) != null) {
                Object[] words_and_tags = NLP_processor.analyze_POS_sentence(sentence);
                String[] words = (String[]) words_and_tags[0];
                String[] tags = (String[]) words_and_tags[1];
                if (no_noun_tags(tags))
                    continue; // no important words in sentence ignore it

                Object[] urls_and_word_list = Sphere_connection_manager.get_results_from_sphere(words, tags);
                if (urls_and_word_list != null) {
                    ArrayList urls = (ArrayList) urls_and_word_list[0];

                    for (Object url : urls)
                    {
                        result += url.toString() + "\n";
                    }

                }
            }
        }

        if (result.length() <= 30) {
            return "I`m sorry, I couldn`t find any relevant content\n";
        }

        return result;
    }
    public static boolean no_noun_tags(String [] tags){
        for (String tag : tags)
        {
            if (tag.indexOf("NN") > -1)
                return false;
        }
        return true;

    }
    // checks if the query is even relevant
    public static boolean check_if_relevant(String query_string){
        //check if url
        String ptrn = "www\\.";
        Pattern ptrn_obj = Pattern.compile(ptrn);
        Matcher m = ptrn_obj.matcher(query_string);
        if (m.find()){
            return false;
        }
        return true;

    }
}
