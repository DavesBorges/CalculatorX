package com.davesborges.CalculatorX;

public interface TokenStream {
    Token read();
    void unread(Token token);

    int getLineNr();
    int getColumnNr();

    boolean isEOF();
    void addSource(String source, boolean reset);
}
