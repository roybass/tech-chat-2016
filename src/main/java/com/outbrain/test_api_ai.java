package com.outbrain;

import com.outbrain.apiai.ApiAiClient;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by Tal on 02/05/2016.
 */
public class test_api_ai {
    public static void main(String args[]) {
        String inputFileName = "C:\\Users\\Tal\\Technion\\simesterF\\Industrial_project\\test_api_ai.csv";
        String outputFileName = "C:\\Users\\Tal\\Technion\\simesterF\\Industrial_project\\test_api_ai_result.csv";


        try {
            Stream<String> myInputStream = Files.lines(Paths.get(inputFileName));

            OutputStream myOutputStream = new FileOutputStream(outputFileName);
            Writer outputStreamWriter = new OutputStreamWriter(myOutputStream);

            myInputStream.forEach(line ->{
                if (line.length() < 254){
                    try {
                        outputStreamWriter.write(line);

                        outputStreamWriter.write("\t");

                        ApiAiClient ai_app = new ApiAiClient();
                        Map ai_response_map = ai_app.getResponse(line);
                        HashMap result = (HashMap) ai_response_map.get("result");
                        String action = result.get("action").toString();
                        outputStreamWriter.write(action);

                        outputStreamWriter.write("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            outputStreamWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
