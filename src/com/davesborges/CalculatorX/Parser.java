package com.davesborges.CalculatorX;

public interface Parser {
    boolean checkGrammar() throws Exception;

    String[] evaluate() throws Exception;

    void checkAllGrammar() throws Exception;

    String[] evaluateAll() throws Exception;
}
