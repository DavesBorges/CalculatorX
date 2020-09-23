package com.davesborges.CalculatorX;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class InputStreamWrapper extends PushbackInputStream{
    int lineNr = 0;
    int columnNr = 0;

    public InputStreamWrapper(InputStream in) {
        super(in);
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
        columnNr++;
        return ch;
    }

    @Override
    public void unread(int ch) throws IOException {
        super.unread(ch);
        columnNr--;
    }

    public int getLineNr() {
        return lineNr;
    }

    public int getColumnNr() {
        return columnNr;
    }

    public void setLineNr(int lineNr) {
        this.lineNr = lineNr;
    }

    public void setColumnNr(int columnNr) {
        this.columnNr = columnNr;
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

}
