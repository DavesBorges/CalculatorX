package com.davesborges.CalculatorX;

import java.io.IOException;

public interface TokenStream {
    Token read() throws Exception;
    void unread(Token token);
    boolean isEOF() throws IOException;
    void addSource(String[] source, boolean reset) throws Exception;
}
