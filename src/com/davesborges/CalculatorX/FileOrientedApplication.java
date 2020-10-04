package com.davesborges.CalculatorX;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FileOrientedApplication extends Application {
    private String sourceName;

    public FileOrientedApplication(String[] args) throws Exception {
        if(args.length != 2)
            throw new Exception("Missing arguments");

        outputStream = new PrintStream(new FileOutputStream(args[1]));
        reader = new BufferedReader(new FileReader(args[0]));
        sourceName = args[0];
    }
    @Override
    public void run() throws Exception {
        String[] lines;
        lines = (String[]) reader.lines().collect(Collectors.toList()).toArray(new String[0]);
        String code = "";
        for(String line : lines){
            code += line + "\n";
        }
        Scope scope = new Scope();
        try{
            BasicLexer lexer = new BasicLexer(sourceName, code);
            TokenStream tokenStream = new BasicTokenStream(lexer.getTokens());

            Parser parser = new BasicParser(tokenStream, scope);
            parser.checkAllGrammar();
            String[] results = parser.evaluateAll();
            String output = "";
            for(int i = 0; i < results.length; ++i){
                if(results[i].length() != 0){
                    if(output.length() != 0)
                        output += '\n';
                    output += results[i];
                }
            }
            outputStream.print(output);
        }catch (ParseException parseException){
            outputStream.println("Parse Exception " + parseException.getMessage());
        }
        catch (Exception exception){
            outputStream.println("EXCEPTION " + exception.getMessage());
        }
    }
}
