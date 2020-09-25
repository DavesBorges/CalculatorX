package com.davesborges.CalculatorX;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class InputStreamWrapper extends PushbackInputStream{
    private Position position;

    public InputStreamWrapper(InputStream in, String file) {
        super(in);
        position = new Position(file);
    }
    public InputStreamWrapper(InputStream in) {
        super(in);
        position = new Position("console input");
    }

    public double readNumber() throws IOException {
        int ch = read();
        String number = "";
        if(ch == '.') number += "0";

        while(ch >= '0' && ch <= '9' || ch == '.'){
            number += (char) ch;
            ch = read();
        }
        if(ch != -1)
            unread(ch);

        return Integer.parseInt(number);
    }

    public String readString() throws IOException {
        int ch = read();
        String readed = "";
        while(!isSpace(ch) && ch != -1){
            readed += (char) ch;
            ch = read();
        }
        if(ch != -1) unread(ch);

        return readed;
    }

    @Override
    public int read() throws IOException {
        int ch = super.read();
        position.advanceColumn();
        return ch;
    }

    @Override
    public void unread(int ch) throws IOException {
        super.unread(ch);
        try {
            position.setColumnNr(position.getColumnNr() -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getLineNr() {
        return position.getLineNr();
    }

    public int getColumnNr() {
        return position.getColumnNr();
    }

    public void setLineNr(int lineNr) throws Exception {
        position.setNewPosition(lineNr);
    }

    public void setColumnNr(int columnNr) throws Exception {
        position.setColumnNr(columnNr);
    }

    public boolean isLetter(int ch){
        return (ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122);
    }

    private boolean isDigit(int ch){
        return ch >= '0' && ch <= '9';
    }

    public boolean isSpace(int ch){
        return (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t');
    }

    public Position getPosition() {
        return position;
    }
}
