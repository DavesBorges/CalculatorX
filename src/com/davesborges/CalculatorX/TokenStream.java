package com.davesborges.CalculatorX;

import java.io.IOException;

public interface TokenStream {
    Token read();
    void unread() throws Exception;
    boolean isEOF();
    int available();
    boolean hasPreceeding();
    Token getPreceeding();
}
