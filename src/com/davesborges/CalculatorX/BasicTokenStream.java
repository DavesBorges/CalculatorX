package com.davesborges.CalculatorX;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;

public class BasicTokenStream implements TokenStream {
    private InputStreamWrapper inputStream;
    private boolean bufferFull;
    private Queue<Token> buffer;
    private boolean EOF = false;

    public BasicTokenStream(String source){
        inputStream = new InputStreamWrapper(new ByteArrayInputStream(source.getBytes()));
        bufferFull = false;
        buffer = new PriorityQueue<>();
    }

    public BasicTokenStream(){
        inputStream = new InputStreamWrapper(new ByteArrayInputStream("".getBytes()));
        buffer = new PriorityQueue<>();
    }

    @Override
    public Token read() throws Exception {
        if(EOF)
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
                }
                ch = inputStream.read();
            }

            if(ch < 0 || ch > 127){
                inputStream.setColumnNr(inputStream.getColumnNr() - 1);
                return null;
            }

            if(ch >= '0' && ch <= '9' || ch == '.'){
                inputStream.unread(ch);
                return (new Token(Token.number, inputStream.readNumber(), new Position(inputStream.getPosition())));
            }

            if(inputStream.isLetter(ch)){
                inputStream.unread(ch);
                return new Token(inputStream.readString(), new Position(inputStream.getPosition()));
            }
            return new Token((char) ch, new Position(inputStream.getPosition()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addSource(String source, boolean reset) throws Exception {
        int columnNr = inputStream.getColumnNr();
        int lineNr = inputStream.getLineNr();

        if(!reset){
            StringBuilder temp = new StringBuilder();
            while(!buffer.isEmpty()){
                temp.append(buffer.remove().getRaw());
            }
            source = temp.toString() + source;
        }
        inputStream = new InputStreamWrapper(new ByteArrayInputStream(source.getBytes()));
        if(!reset){
            inputStream.setLineNr(lineNr);
            inputStream.setColumnNr(columnNr);
        }
    }

    @Override
    public void unread(Token token) {
        buffer.add(token);
        bufferFull = true;
    }

    @Override
    public int getLineNr() {
        return inputStream.getLineNr();
    }

    @Override
    public int getColumnNr() {
        return inputStream.getColumnNr();
    }

    @Override
    public boolean isEOF() {
        return EOF;
    }
}
