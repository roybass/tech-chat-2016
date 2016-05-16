package com.outbrain;

import com.outbrain.NLP_processor;
import com.outbrain.Sphere_connection_manager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tal on 16/05/2016.
 */
public class Chat_runner_production {
    public static void main(String[] args) {
    }
    // TODO return only the content to be sent to the user
    public static String get_Sphere_content(String query_string){
        String result = new String();
        result = "the results we found:\n";
        if(!check_if_relevant(query_string)){
            return "please don`t give me urls, ask me something else\n";
        }
        String[] sentences = NLP_processor.devide_to_sentences(query_string);
        for (String sentence : sentences) {
            if(NLP_processor.analyze_POS_sentence(sentence) != null) {
                Object[] words_and_tags = NLP_processor.analyze_POS_sentence(sentence);
                String[] words = (String[]) words_and_tags[0];
                String[] tags = (String[]) words_and_tags[1];
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

        if (result.length() <= 20) {
            return "I`m sorry, I couldn`t find any relevant content\n";
        }

        return result;
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
