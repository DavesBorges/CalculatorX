package com.davesborges.CalculatorX;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Application {

    public void run() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String content;
        String fileName = "console input";
        Lexer lexer = new BasicLexer();

        while(true){
            System.out.print("> ");
            content = reader.readLine() + ';';
            lexer.addSource(fileName, content, true);

            TokenStream tokenStream = new BasicTokenStream(lexer.getTokens());
            if(tokenStream.read().getStringValue().equals(Token.exitKeyWord))
                return;
            tokenStream.unread();

            Parser parser = new BasicParser(tokenStream);
            String[] results = parser.evaluate();
            Arrays.stream(results).forEach(result -> System.out.println("= " + result));
        }

    }
    private void sendMessage(){
        System.out.println("Thank you for using CalculatorX ");
        System.out.println("Github Repository for suggestions or bug report " + "https://github.com/DavesBorges/CalculatorX");
    }
}
