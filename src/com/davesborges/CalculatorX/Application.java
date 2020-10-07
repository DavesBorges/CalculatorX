package com.davesborges.CalculatorX;

import java.io.*;
import java.util.Arrays;

public class Application {
    private Language language;
    protected BufferedReader reader;
    protected PrintStream outputStream;
    protected String sourceName;
    
    public Application(){
        reader = new BufferedReader(new InputStreamReader(System.in));
        outputStream = System.out;
        sourceName = "console input";
    }
    public void run() throws Exception {
        getUserLanguage();
        String content;
        Lexer lexer = new BasicLexer();
        Scope scope = new Scope();
        while(true){
            outputStream.print("> ");
            content = reader.readLine() + '\n';
            if(content.length() <= 1)
                continue;
            lexer.addSource(sourceName, content, true);

            TokenStream tokenStream = new BasicTokenStream(lexer.getTokens());
            Token t = tokenStream.read();
            if(t.getStringValue().equals(Token.exitKeyWord)){
                showMessage();
                return;
            }
            if(t.getStringValue().equals(Token.helpKeyword)){
                showHelp();
                continue;
            }
            tokenStream.unread();

            Parser parser = new BasicParser(tokenStream, scope);
            try{
                parser.checkGrammar();
                String[] results = parser.evaluate();
                Arrays.stream(results).filter( x -> x.length() != 0).forEach(result -> outputStream.println("= " + result));
            }
            catch (ParseException parseException){
                reportError(parseException, content);
            }

        }

    }

    private void getUserLanguage() throws IOException {
        outputStream.println("Welcome to Calculator X");
        outputStream.println("Type your language and press Enter: \n"+
                "Available languages:\n" +
                "-English\n" +
                "-Portugues");
        outputStream.print("Your Language: ");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String line = bufferedReader.readLine();
        line = line.toUpperCase();
        switch (line){
            case "PORTUGUES":
                this.language = Language.PORTUGUES;
                break;
            default:
                this.language = Language.ENGLISH;
                break;
        }
        switch (language) {
            case PORTUGUES:
                outputStream.println("Digite \"help\" e pressione ENTER para obter ajuda");
                break;
            default:
                outputStream.println("type \"help\" to get instructions");
                break;
        }
    }
    public enum Language{
        ENGLISH, PORTUGUES
    }

    private void showHelp() throws IOException {
        try(BufferedReader bufferedReader = new BufferedReader(
                new FileReader("Instructions.txt"))) {

            String currentLine = bufferedReader.readLine();
            while (currentLine != null) {
                if (currentLine.contains(language.name())) {
                    currentLine = bufferedReader.readLine();
                    while (currentLine != null) {
                        outputStream.println(currentLine);
                        if (currentLine.contains(">>>>>"))
                            break;
                        currentLine = bufferedReader.readLine();
                    }
                }
                currentLine = bufferedReader.readLine();
            }
        }
    }
    private void showMessage() throws InterruptedException, IOException {
        switch (language){
            case PORTUGUES:
                outputStream.println("Obrigado por usar CalculatorX");
                outputStream.println("Repositorio do codigo para sugestoes ou reportar erros " + "https://github.com/DavesBorges/CalculatorX");
                outputStream.println("Pressione ENTER para sair ");
                break;
            default:
                outputStream.println("Thank you for using CalculatorX ");
                outputStream.println("Github Repository for suggestions or bug report " + "https://github.com/DavesBorges/CalculatorX");
                outputStream.println("Press ENTER to EXIT ");
        }
        System.in.read();


    }

    protected void reportError(ParseException parseException, String content) throws IOException {
        Position position = parseException.getStartingPosition();
        InputStreamWrapper inputStreamWrapper =
                new InputStreamWrapper(new ByteArrayInputStream(content.getBytes()), sourceName);
        String errorStatement = "";
        char ch = ' ';
        while(!position.equals(inputStreamWrapper.getPosition())){
            ch = (char) inputStreamWrapper.read();
        }
        errorStatement += ch;
        while(inputStreamWrapper.available() != 0 && !errorStatement.contains(";")){
            ch = (char) inputStreamWrapper.read();
            if(ch == '\n')
                break;

            errorStatement += ch;
        }

        outputStream.println("Parse Exception \"" + errorStatement + "\" " + parseException.getMessage());
    }
}
