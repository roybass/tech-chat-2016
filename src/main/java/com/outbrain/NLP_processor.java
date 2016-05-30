package com.outbrain;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

            return sentences;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // get unimportant words from dictionry and check if they contain words  in the input
    // all words in input that are contained in the dictionary are deleted
    public static String[] delete_words_from_dict(String[] words){
        String inputFileName = "src\\main\\java\\open_nlp_libs\\ignore_words_dict.txt";
        ArrayList<String> sub_list_of_important_words = new ArrayList<String>();
        try {
            FileInputStream fs = new FileInputStream(inputFileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            String firstLine = br.readLine();
            String[] to_delete_words = firstLine.split(",");
            Map to_delete_map = new HashMap();
            for (String word : to_delete_words){
                to_delete_map.put(word.toLowerCase(),"1");
            }

            for (String word : words){
                String lower_case_word = word;

                if(!to_delete_map.containsKey(lower_case_word.toLowerCase())){
                    sub_list_of_important_words.add(word);
                }
            }
        }catch(Exception ex){
            System.out.println("couldnt open and read line from file: " + inputFileName);
            ex.printStackTrace();
        }

        String[] important_words = new String[sub_list_of_important_words.size()];
        important_words = sub_list_of_important_words.toArray(important_words);
        return important_words;

    }
    // analyze the sentence it gets as an argument with openNLP and create POS tagging
    // returns 2 arrays in Object first containing words from sentence second containing tags.
    public static Object[] analyze_POS_sentence(String question_string){
        String[] strings = question_string.split("\\W+|'\\w*");
        if(strings.length == 0) {
            System.out.println("this sentence contains zero words\n");
            return null;
        }
        strings = delete_words_from_dict(strings);
        try {
            InputStream modelIn = new FileInputStream("src\\main\\java\\open_nlp_libs\\en-pos-maxent.bin");
            POSModel model = new POSModel(modelIn);
            POSTaggerME tagger = new POSTaggerME(model);
            String tags[] = tagger.tag(strings);

            Object[] words_tags = create_important_words_array(strings,tags);
            return words_tags;

        }catch(IOException e) {
            System.out.println("got error in analyze_POS_sentence");
            return null;
        }
    }
    // get the relevant noun words from the pos sentence
    // returns object containing 2 arrays
    public static Object[] create_important_words_array(String[] words , String[] tags){

        int index = 0;
        String ptrn = ".*NN.*|JJ.*|VB.*";
        Pattern ptrn_obj = Pattern.compile(ptrn);
        for (int i = 0 ; i < tags.length ; i++){
            if (check_if_empty(words[i]))
                continue;
            tags[i] += "_"+Integer.toString(i);

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
                if (check_if_empty(words[i]))
                    continue;
                final_words[index] = words[i];
                final_tags[index] = tags[i];
                index++;
            }
        }

        return sort_arrays(final_words,final_tags);// new Object[]{final_words,final_tags};

    }
    //checks if the string contains only white space
    public static boolean check_if_empty(String string){

        String ptrn = "^\\s*$";
        Pattern ptrn_obj = Pattern.compile(ptrn);
        Matcher m = ptrn_obj.matcher(string);
        if (m.find()){
            return true;
        }
        return false;
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
        if (tag.indexOf("NNPS_") > -1) {
            return 1;
        }
        if (tag.indexOf("NNP_") > -1) {
            return 2;
        }
        if (tag.indexOf("NNS_") > -1) {
            return 3;
        }
        if (tag.indexOf("NN_") > -1) {
            return 4;
        }
        if (tag.indexOf("VB") > -1) {
            return 5;
        }
        if (tag.indexOf("JJ") > -1) {
            return 6;
        }
        if (tag.indexOf("RB") > -1 && tag.indexOf("WRB") == -1) {
            return 7;
        }

        return 8;
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
