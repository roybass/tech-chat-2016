package com.outbrain;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by ilya on 5/16/2016. s
 */
public class chat_bot {
    public static void main(String[] args) {
        while(true){
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter String: ");
            try{
                String message = br.readLine();
                String result = Chat_runner_production.get_Sphere_content(message);
                System.out.println(result);
            }catch (Exception ex){

            }

        }
    }
}
