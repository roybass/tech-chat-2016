package com.outbrain;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ilya on 4/27/2016.
 */
public class NLP_processor {
    // get a text as an argument and returns an array of strings, each of them represent a sentence in the text
    public static String[] devide_to_sentences(String query_string){
        // see documentation here: https://opennlp.apache.org/documentation/1.6.0/manual/opennlp.html#tools.sentdetect
        // To instantiate the Sentence Detector the sentence model must be loaded first.
        try {
            InputStream modelIn = new FileInputStream("src\\main\\java\\open_nlp_libs\\en-sent.bin");
            SentenceModel model = new SentenceModel(modelIn);
            // After the model is loaded the SentenceDetectorME can be instantiated.
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
            String sentences[] = sentenceDetector.sentDetect(query_string);
            System.out.println(sentences);

            return sentences;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // analyze the sentence it gets as an argument with openNLP and create POS tagging
    // returns 2 arrays in Object first containing words from sentence second containing tags.
    public static Object[] analyze_POS_sentence(String question_string){
        String[] strings = question_string.split("'| +");
        try {
            InputStream modelIn = new FileInputStream("src\\main\\java\\open_nlp_libs\\en-pos-maxent.bin");
            POSModel model = new POSModel(modelIn);
            POSTaggerME tagger = new POSTaggerME(model);
            String tags[] = tagger.tag(strings);

            Object[] words_tags = create_noun_array(strings,tags);
            strings = (String[])words_tags[0];
            tags = (String[])words_tags[1];
            /*for (int i = 0 ; i < tags.length ; i++){
                System.out.println(strings[i] + "_" + tags[i]  + "\n");
            }*/
            return words_tags;

        }catch(IOException e) {
            System.out.println("got error in analyze_POS_sentence");
            return null;
        }
    }
    // get the relevant noun words from the pos sentence
    // returns object containing 2 arrays
    public static Object[] create_noun_array(String[] words , String[] tags){
        int index = 0;
        String ptrn = ".*NN.*";
        Pattern ptrn_obj = Pattern.compile(ptrn);
        for (int i = 0 ; i < tags.length ; i++){
            Matcher m = ptrn_obj.matcher(tags[i]);
            if (m.find()){
                index++;
            }
        }
        String[] final_words = new String [index];
        String[] final_tags = new String [index];
        index = 0;
        for (int i = 0 ; i < tags.length ; i++){
            Matcher m = ptrn_obj.matcher(tags[i]);
            if (m.find()){
                final_words[index] = words[i];
                final_tags[index] = tags[i];
                index++;
            }
        }

        return sort_arrays(final_words,final_tags);// new Object[]{final_words,final_tags};

    }
    // sort the pos elements
    public static Object[] sort_arrays(String[] words ,String[] tags){
        for (int i = 0 ; i < tags.length ; i++){
            for (int y = 0 ; y < tags.length ; y++){
                if(get_value_of_tag(tags[i])<get_value_of_tag(tags[y]))
                {
                    String temp= tags[y];
                    tags[y]= tags[i];
                    tags[i]=temp;
                    temp = words[y];
                    words[y]= words[i];
                    words[i]=temp;
                }
            }
        }
        return new Object[]{words,tags};
    }
    // get value for each tag to enable sort
    public static int get_value_of_tag(String tag) {
        if (tag.compareTo("NN") == 0) {
            return 4;
        }
        if (tag.compareTo("NNP") == 0) {
            return 2;
        }
        if (tag.compareTo("NNS") == 0) {
            return 3;
        }
        if (tag.compareTo("NNPS") == 0) {
            return 1;
        }
        return 5;
    }
    /*public static Set<Set<String>> powerSet(Set<String> originalSet) {
        Set<Set<String>> sets = new HashSet<Set<String>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<String>());
            return sets;
        }
        List<String> list = new ArrayList<String>(originalSet);
        String head = list.get(0);
        Set<String> rest = new HashSet<String>(list.subList(1, list.size()));
        for (Set<String> set : powerSet(rest)) {
            Set<String> newSet = new HashSet<String>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }*/
}
