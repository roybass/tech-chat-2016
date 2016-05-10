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
        ArrayList test = get_Sphere_content("Whos winning the presidential election in pennsylvania");
        if(test == null) {
            System.out.println("null\n");
            return;
        }
        System.out.println(test.toString());
    }
    public static ArrayList get_Sphere_content(String query_string){
        String[] sentences = NLP_processor.devide_to_sentences(query_string);
        ArrayList results_for_sentences = new ArrayList();
        for (String sentence : sentences) {
            Object[] words_and_tags = NLP_processor.analyze_POS_sentence(sentence);
            String[] words = (String[]) words_and_tags[0];
            String[] tags = (String[]) words_and_tags[1];
            Object[] urls_and_word_list = Sphere_connection_manager.get_results_from_sphere(words, tags);
            if (urls_and_word_list != null)
                results_for_sentences.add(urls_and_word_list);

        }

        if (results_for_sentences.size() <= 0)
            return null;
        return results_for_sentences;
    }







}
