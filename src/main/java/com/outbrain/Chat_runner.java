package com.outbrain;
import com.fasterxml.jackson.databind.util.TypeKey;
import com.outbrain.apiai.ApiAiClient;
import com.outbrain.sphere.SphereClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat_runner {

    public static void use_open_nlp_api(String question_string){
        String[] strings = question_string.split("'| +");
        try {
            InputStream modelIn = new FileInputStream("C:\\Users\\ilya\\Desktop\\NLP CODE\\openNLP\\apache-opennlp-1.6.0-bin\\apache-opennlp-1.6.0\\bin\\en-pos-maxent.bin");
            POSModel model = new POSModel(modelIn);
            POSTaggerME tagger = new POSTaggerME(model);
            String tags[] = tagger.tag(strings);

            Object[] words_tags = get_noun_array(strings,tags);
            strings = (String[])words_tags[0];
            tags = (String[])words_tags[1];
            for (int i = 0 ; i < tags.length ; i++){
                System.out.println(strings[i] + "_" + tags[i]  + "\n");
            }


        }catch(IOException e){
            return;
        }
    }
    public static Object[] get_noun_array(String[] words ,String[] tags){
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
    public static int get_value_of_tag(String tag){
        if (tag.compareTo("NN") == 0){
            return 4;
        }
        if (tag.compareTo("NNP") == 0){
            return 2;
        }
        if (tag.compareTo("NNS") == 0){
            return 3;
        }
        if (tag.compareTo("NNPS") == 0){
            return 1;
        }
        return 5;
    }
    public static void main(String[] args) {
        //String query_string = "Hi. What's the latest comment from trump on Mark zuckerbergs keynote Can you tell me the result of yesterday's UCL matches Real world stuff.   Whats going on with Donald trump Tell me about how the presidential election in America is going";
        //use_open_nlp_api(query_string);
        String query_string = "How about Bernie's 48k   rally in Manhattan last night";
        use_open_nlp_api(query_string);
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
    public static void get_urls(Object out_brain_result){
        String out_brain = out_brain_result.toString();
        System.out.println(out_brain);
        //String[] urls = new String[100];
        String[] strings = out_brain.split(",");
        String ptrn = ".*url=(.*)";
        Pattern ptrn_obj = Pattern.compile(ptrn);
        for (String iterator : strings){
            Matcher m = ptrn_obj.matcher(iterator);
            if (m.find()){
                System.out.println("url is: " + m.group(1));
            }else{

            }
        }
    }
}
