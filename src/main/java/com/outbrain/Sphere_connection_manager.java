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
    public static ArrayList query_sphere(ArrayList words){
        String words_to_be_searched = String.join(" ",words);
        final HashMap<String, Map> result = new HashMap<>();
        SphereClient sphere_app = new SphereClient();
        Map sphere_response_map = sphere_app.getRecommendtionByTerm(words_to_be_searched);
        result.put("sphere",sphere_response_map);


        return get_urls(sphere_response_map.get("items"));
    }
    /**
     * input params:
     *  @words - array of important words from the search query, the best is first
     *  @tags - array containing the tags of the words
     * output:
     *  an ArrayList of urls & words that were found in their description,
     *  which contains the best answer for the search query -
     *  the one that contains the best( currently biggest words combinations
     */
    public static Object[] get_results_from_sphere(String[] words ,String[] tags){
        if(words.length <=0)
            return null;
        // first convert to array list so it will be easy to change array size
        ArrayList list_of_words = new ArrayList();
        ArrayList list_of_tags = new ArrayList();
        for (int i = 0; i < words.length; i++){
            list_of_words.add(words[i]);
            list_of_tags.add(tags[i]);
        }
        while(list_of_words.size() > 6){
            list_of_words.remove(list_of_words.size() - 1);
            list_of_tags.remove(list_of_tags.size() - 1);
        }
        // all subarrays described in a binary number 1111 all 4 exist 1101 3rd element doesnt exist and the rest do
        ArrayList sorted_list_of_words = sort_by_location(list_of_words,list_of_tags);
        ArrayList result = new ArrayList();
        for (int array_content = (int)Math.pow(2,list_of_words.size()) - 1; array_content > 0 && result.size() == 0;array_content--){
            ArrayList sub_list = get_sub_list(list_of_words,array_content);
            ArrayList sub_list_tags = get_sub_list(list_of_tags,array_content);
            sorted_list_of_words = sort_by_location(sub_list,sub_list_tags);
            result = query_sphere(sorted_list_of_words);

        }

        return new Object[]{result,sorted_list_of_words};
    }
    // sort the string by its location in the original message to enable outBrain search
    public static ArrayList get_sub_list(ArrayList list_of_words_orig,int array_contents){
        ArrayList list_of_words = new ArrayList();

        for (int index = 1;index<=list_of_words_orig.size();index++) {
            if (check_if_binary_on(array_contents, index - 1)) {
                list_of_words.add(list_of_words_orig.get(list_of_words_orig.size() - index));
            }

        }

        return list_of_words;
    }
    public static boolean check_if_binary_on(int num,int index){
        int binary[] = new int[20];
        int cur_index = 0;
        while(num > 0){
            binary[cur_index++] = num%2;
            num = num/2;
        }
        if(binary[index] == 1)
            return true;
        return false;
    }
    public static ArrayList sort_by_location(ArrayList list_of_words_orig,ArrayList list_of_tags_orig){
        ArrayList list_of_words = new ArrayList();
        ArrayList list_of_tags = new ArrayList();

        for (Object o : list_of_tags_orig){
            list_of_tags.add(o);
        }
        for (Object o : list_of_words_orig){
            list_of_words.add(o);
        }
        ArrayList list_of_numbers = new ArrayList();
        for (int i = 0; i < list_of_tags.size(); i++){
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(list_of_tags.get(i).toString());
            if (matcher.find()) {
                list_of_numbers.add(matcher.group());
            }
        }
        for (int i = 0 ; i < list_of_numbers.size() ; i++){
            for (int y = 0 ; y < list_of_numbers.size() ; y++){
                if(Integer.parseInt(list_of_numbers.get(i).toString())<Integer.parseInt(list_of_numbers.get(y).toString()))
                {
                    String temp= list_of_numbers.get(y).toString();
                    list_of_numbers.set(y,list_of_numbers.get(i).toString());
                    list_of_numbers.set(i,temp);
                    temp= list_of_words.get(y).toString();
                    list_of_words.set(y,list_of_words.get(i).toString());
                    list_of_words.set(i,temp);
                }
            }
        }

        return list_of_words;

    }
    // get the relevant URL`s from argument(outbrain output)
    public static ArrayList get_urls(Object out_brain_result){
        ArrayList out_brain_list = (ArrayList)out_brain_result;
        if (out_brain_list.size() == 0){
            return new ArrayList();
        }

        ArrayList urls = new ArrayList();
        for (int ind = 0; ind < out_brain_list.size() ; ind++){
            Map out_brain_map = (Map)out_brain_list.get(ind);
            Map url_map = (Map)out_brain_map.get("document");
            String url = url_map.get("url").toString();
            urls.add(url);
        }
        return urls;
    }
}
