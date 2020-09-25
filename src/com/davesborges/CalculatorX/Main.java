package com.davesborges.CalculatorX;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String source;
        TokenStream tokenStream = new BasicTokenStream();

        while(true){
            source = reader.readLine() + '\n';
            tokenStream.addSource(source, false);
            Token t = tokenStream.read();
            while(t != null){
                System.out.println("Token " + t);
                t = tokenStream.read();
            }
        }

    }
}
