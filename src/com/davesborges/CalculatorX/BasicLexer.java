package com.davesborges.CalculatorX;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class BasicLexer implements Lexer {
    private InputStreamWrapper inputStream;
    private boolean bufferFull;
    private Queue<Token> buffer;
    private boolean EOF = false;


    public BasicLexer(String sourceName, String content){
        inputStream = new InputStreamWrapper(new ByteArrayInputStream(content.getBytes()), sourceName);
        bufferFull = false;
        buffer = new PriorityQueue<>();
    }

    public BasicLexer(){
        inputStream = new InputStreamWrapper(new ByteArrayInputStream("".getBytes()));
        buffer = new PriorityQueue<>();
    }

    public Token read() throws Exception {
        if(isEOF())
            return null;
        if(bufferFull){
            Token t = buffer.remove();
            if(buffer.isEmpty())
                bufferFull = false;
            return t;
        }

        try {
            int ch = inputStream.read();
            while(inputStream.isSpace(ch)){
                if(ch == '\n'){
                    inputStream.setColumnNr(0);
                    inputStream.setLineNr(inputStream.getLineNr() + 1);
                    return new Token(Token.statementEnd, inputStream.getPosition());
                }
                ch = inputStream.read();
            }

            if(ch < 0 || ch > 127){
                inputStream.setColumnNr(inputStream.getColumnNr() - 1);
                EOF = true;
                return null;
            }

            if(ch >= '0' && ch <= '9' || ch == '.'){
                inputStream.unread(ch);
                Position position = new Position(inputStream.getPosition());
                position.advanceColumn();
                return (new Token(Token.number, inputStream.readNumber(), new Position(position)));
            }

            if(inputStream.isLetter(ch)){
                inputStream.unread(ch);
                Position position = new Position(inputStream.getPosition());
                position.advanceColumn();
                String string = inputStream.readString();
                if(string.equals(Token.declaration))
                    return new Token(Token.declarationKeyWord, new Position(position));
                return new Token(string, new Position(position));
            }
            return new Token((char) ch, new Position(inputStream.getPosition()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addSource(String sourceName, String content, boolean reset) throws Exception {
        int columnNr = inputStream.getColumnNr();
        int lineNr = inputStream.getLineNr();

        if(!reset){
            StringBuilder temp = new StringBuilder();
            while(!buffer.isEmpty()){
                temp.append(buffer.remove().getStringValue());
            }
            content = temp.toString() + content;
        }
        inputStream = new InputStreamWrapper(new ByteArrayInputStream(content.getBytes()), sourceName);
        if(!reset){
            inputStream.setLineNr(lineNr);
            inputStream.setColumnNr(columnNr);
        }

        EOF = false;
    }

    public void unread(Token token) {
        buffer.add(token);
        bufferFull = true;
    }

    public boolean isEOF() throws IOException {
        return (buffer.isEmpty() && inputStream.available() == 0);
    }

    @Override
    public Token[] getTokens() throws Exception {
        ArrayList<Token> tokens = new ArrayList<>();
        Token t = read();

        while(!isEOF() || t != null){
            tokens.add(t);
            t = read();
        }

        Token[] tokensArray = new Token[tokens.size()];
        return tokens.toArray(tokensArray);
    }
}
