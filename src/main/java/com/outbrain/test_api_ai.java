package com.outbrain;

import com.outbrain.apiai.ApiAiClient;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by Tal on 02/05/2016.
 * We use currently only first 1,000 lines
 */
public class test_api_ai {
    public static void main(String args[]) {
        String inputFileName = "src\\main\\java\\open_nlp_libs\\test_api_ai_orig.csv";
        String outputFileName = "src\\main\\java\\open_nlp_libs\\test_api_ai_result.csv";


        try {
            Stream<String> myInputStream = Files.lines(Paths.get(inputFileName));

            OutputStream myOutputStream = new FileOutputStream(outputFileName);
            Writer outputStreamWriter = new OutputStreamWriter(myOutputStream);

            myInputStream.forEach(line ->{
                if (line.length() < 254 && !line.contains("\"")){
                    try {
                        outputStreamWriter.write(line);

                        outputStreamWriter.write("\t");

                        ApiAiClient ai_app = new ApiAiClient();
                        Map ai_response_map = ai_app.getResponse(line);
                        HashMap result = (HashMap) ai_response_map.get("result");

                        String action;
                        try{
                            if(result.get("action").toString() != null)
                            {
                                action = result.get("action").toString();
                                outputStreamWriter.write(action);

                            }
                        } catch (NullPointerException e){

                        }
                        outputStreamWriter.write("\t"); // action
                        try{
                            if(result.get("parameters").toString() != null)
                            {
                                String parameters = result.get("parameters").toString();
                                outputStreamWriter.write(parameters);

                            }
                        } catch (NullPointerException e){

                        }
                        outputStreamWriter.write("\t"); // parameters

                        HashMap fulfillment;
                        try{
                            fulfillment = (HashMap) result.get("fulfillment");
                            if(fulfillment.get("speech").toString() != null)
                            {
                                String speech = fulfillment.get("speech").toString();
                                outputStreamWriter.write(speech);
                                outputStreamWriter.write("\t");
                            }
                            if(fulfillment.get("source").toString() != null)
                            {
                                String source = fulfillment.get("source").toString();
                                outputStreamWriter.write(source);
                                outputStreamWriter.write("\t");
                            }
                        } catch (NullPointerException e){

                        }

                        //now get Sphere content
                        System.out.println("current line query; "+line);
                        ArrayList sphere_response = Chat_runner_dev.get_Sphere_content(line);
                        if (sphere_response == null){
                            outputStreamWriter.write("no results for line: " + line + "\t");

                        }else
                            sphere_response.forEach(temp_response ->{
                                Object[] response = (Object[])temp_response;
                                ArrayList urls = (ArrayList) response[0];
                                ArrayList words = (ArrayList) response[1];
                                try{
                                    outputStreamWriter.write(words.toString());
                                    outputStreamWriter.write("\t");

                                    urls.forEach(url -> {
                                        try{
                                            outputStreamWriter.write(url.toString() + ";;;");
                                        }catch (IOException e) {
                                            e.printStackTrace();
                                            System.out.println("failed on request for line:"+line);
                                        }
                                    });
                                    outputStreamWriter.write("\t");

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    System.out.println("failed on request for line:"+line);
                                }

                            });



                        outputStreamWriter.write("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("failed on request for line:"+line);
                    }
                }
            });
            outputStreamWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
