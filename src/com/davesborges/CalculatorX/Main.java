package com.davesborges.CalculatorX;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String source;
        Lexer tokenStream = new BasicTokenStream();

        while(true){
            source = reader.readLine() + '\n';
            tokenStream.addSource(
                    new String[]
                            {"C:\\Users\\Daves\\Desktop\\PenDrive\\dev\\CalculatorX", source},
                            false
            );
            Token[] tokens = tokenStream.getTokens();
            ArrayList<Token> tokenArrayList = new ArrayList<Token>(Arrays.asList(tokens));
            tokenArrayList.forEach(System.out::println);
        }

    }
}
