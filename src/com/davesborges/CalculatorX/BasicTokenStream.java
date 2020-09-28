package com.davesborges.CalculatorX;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BasicTokenStream implements TokenStream {
    private Token[] tokens;
    private int getIndex = 0;
    private Position startingPosition;

    public BasicTokenStream(TokenStream tokenStream) {
        Token[] tokens = new Token[tokenStream.available()];
        Arrays.stream(tokens).map(token -> tokenStream.read()).collect(Collectors.toList())
                .toArray(tokens);
        this.tokens = tokens;
        Arrays.stream(tokens).forEach(token -> {
            try {
                tokenStream.unread();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public BasicTokenStream(Token[] tokens) {
        this.tokens = tokens;
    }

    @Override
    public Token read() {
        if (getIndex == tokens.length) {
            return null;
        }
        return tokens[getIndex++];
    }

    @Override
    public void unread() throws Exception {
        if (getIndex == 0) {
            throw new Exception("Nothing has been read");
        }
        --getIndex;
    }

    @Override
    public boolean isEOF() {
        return getIndex >= tokens.length;
    }

    @Override
    public int available() {
        return tokens.length - getIndex;
    }

}