package com.davesborges.CalculatorX;

public interface Lexer {
    public Token[] getTokens() throws Exception;

    public void addSource(String[] source, boolean reset) throws Exception;
}
