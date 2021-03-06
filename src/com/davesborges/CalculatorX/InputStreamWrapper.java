package com.davesborges.CalculatorX;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class InputStreamWrapper extends PushbackInputStream{
    private Position position;
    private int previousColumnNr;

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

        return Double.parseDouble(number);
    }

    public String readString() throws IOException {
        int ch = read();
        String readed = "";
        while(isLetter(ch) || ((ch >= '0' && ch <= '9') || ch == '_')){
            readed += (char) ch;
            ch = read();
        }
        if(ch != -1) unread(ch);

        return readed;
    }

    @Override
    public int read() throws IOException {
        int ch = super.read();
        if(ch == '\n'){
            try{
                previousColumnNr = getColumnNr();
                position.setNewPosition(position.getLineNr() + 1, 0);
                return ch;
            }
            catch (Exception exception){
                System.out.println(exception.getMessage());
            }
        }
        position.advanceColumn();
        return ch;
    }

    @Override
    public void unread(int ch) throws IOException {
        super.unread(ch);
        try {
            if(getColumnNr() == 0){
                position.setNewPosition(getLineNr() - 1, previousColumnNr);
                return;
            }
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

    public int getPreviousColumnNr() {
        return previousColumnNr;
    }
}
