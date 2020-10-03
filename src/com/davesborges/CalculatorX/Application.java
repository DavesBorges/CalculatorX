package com.davesborges.CalculatorX;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Application {

    public void run() throws Exception {
        try(BufferedReader fileReader = new BufferedReader(new FileReader("Instructions.txt"))){
            fileReader.lines().forEach(System.out::println);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String content;
        String fileName = "console input";
        Lexer lexer = new BasicLexer();
        Scope scope = new Scope();
        while(true){
            System.out.print("> ");
            content = reader.readLine() + '\n';
            lexer.addSource(fileName, content, true);

            TokenStream tokenStream = new BasicTokenStream(lexer.getTokens());
            if(tokenStream.read().getStringValue().equals(Token.exitKeyWord)){
                showMessage();
                return;
            }
            tokenStream.unread();

            Parser parser = new BasicParser(tokenStream, scope);
            try{
                parser.checkGrammar();
                String[] results = parser.evaluate();
                Arrays.stream(results).filter( x -> x.length() != 0).forEach(result -> System.out.println("= " + result));
            }
            catch (ParseException parseException){
                System.out.println("Parse Exception " + parseException.getMessage());
                parseException.printStackTrace();
            }

        }

    }
    private void showMessage(){
        System.out.println("Thank you for using CalculatorX ");
        System.out.println("Github Repository for suggestions or bug report " + "https://github.com/DavesBorges/CalculatorX");
    }
}
