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

public class Chat_runner_dev {
    public static void main(String[] args) {
        ArrayList test = get_Sphere_content("I want to learn more about the presidential elections");
        if(test == null) {
            System.out.println("null\n");
            return;
        }
        System.out.println(test.toString());
    }
    // TODO return only the content to be sent to the user
    public static ArrayList get_Sphere_content(String query_string){
        ArrayList results_for_sentences = new ArrayList();
        if(!check_if_relevant(query_string)){
            return null;
        }
        String[] sentences = NLP_processor.devide_to_sentences(query_string);
        for (String sentence : sentences) {
            if(NLP_processor.analyze_POS_sentence(sentence) != null) {
                Object[] words_and_tags = NLP_processor.analyze_POS_sentence(sentence);
                String[] words = (String[]) words_and_tags[0];
                String[] tags = (String[]) words_and_tags[1];
                Object[] urls_and_word_list = Sphere_connection_manager.get_results_from_sphere(words, tags);
                if (urls_and_word_list != null)
                    results_for_sentences.add(urls_and_word_list);
            }
        }

        if (results_for_sentences.size() <= 0)
            return null;
        return results_for_sentences;
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
