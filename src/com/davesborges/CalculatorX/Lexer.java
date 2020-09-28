package com.davesborges.CalculatorX;

public interface Lexer {
    Token[] getTokens() throws Exception;
    void addSource(String source, String content, boolean reset) throws Exception;
}
