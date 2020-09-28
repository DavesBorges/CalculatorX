package com.davesborges.CalculatorX;

import java.util.ArrayList;

public class BasicParser implements Parser{
    private TokenStream tokenStream;
    private TokenStream testTokenStream;
    ArrayList<String> results;

    public BasicParser(TokenStream tokenStream){
        this.tokenStream = tokenStream;
        results = new ArrayList<>();
    }

    @Override
    public String[] evaluate() throws Exception {
        results.clear();
        try{
            while(!tokenStream.isEOF()) {
                testTokenStream = new BasicTokenStream(tokenStream);
                if (isExpression()) {
                    Token t = testTokenStream.read();
                    if(t == null || t.getKind() != Token.statementEnd) {
                        if(t != null){
                            testTokenStream.unread();
                            t = testTokenStream.read();
                            System.out.println("Expected " + Token.statementEnd + " instead of token " + t.getStringValue());
                            String[] resultsArray = new String[results.size()];
                            return results.toArray(resultsArray);
                        }

                        testTokenStream.unread();
                        t = testTokenStream.read();
                        System.out.println("Expected " + Token.statementEnd + " after token " + t.getStringValue());
                        String[] resultsArray = new String[results.size()];
                        return results.toArray(resultsArray);


                    }
                    String result = Double.toString(evaluateExpression());
                    tokenStream.read();

                    results.add(result);
                }
            }
        }catch(Exception exc){
            System.out.println(exc.getMessage());
        }

        String[] resultsArray = new String[results.size()];
        return results.toArray(resultsArray);
    }

    private double evaluateExpression() throws Exception {
        double d = evaluateTerm();
        Token t = tokenStream.read();
        while(true){
            if(t == null)
                return d;
            switch(t.getKind()){
                case '+':
                    d += evaluateTerm();
                    t = tokenStream.read();
                    break;
                case '-':
                    d -= evaluateTerm();
                    t = tokenStream.read();
                    break;
                default:
                    tokenStream.unread();
                    return d;
            }
        }
    }

    private double evaluateTerm() throws Exception {
        double d = evaluatePrimary();
        Token t = tokenStream.read();
        while(true){
            if(t == null){
                return d;
            }
            switch(t.getKind()){
                case '*':
                    d *= evaluateTerm();
                    t = tokenStream.read();
                    break;
                case '/':
                    double secondTerm = evaluateTerm();
                    if(secondTerm == 0){
                        throw new Exception("Cannot divide to zero " + "at " + t.getLocation());
                    }
                    d /= secondTerm;
                    t = tokenStream.read();
                    break;
                default:
                    tokenStream.unread();
                    return d;
            }
        }
    }

    private double evaluatePrimary() throws Exception {
        Token t = tokenStream.read();
        if(t.getKind() == Token.number){
            return t.getValue();
        }

        if(t.getKind() == '('){
            double d = 0;
            d = evaluateExpression();
            if(tokenStream.read().getKind() == ')')
                return d;
        }
        throw new Exception("Unexpected token " + t.getStringValue() + " at " + t.getLocation());
    }

    boolean isExpression() throws Exception {
        if(!isTerm()){
            return false;
        }

        Token t = testTokenStream.read();
        if(t != null) {
            while(t != null && (t.getKind() == '+' || t.getKind() == '-')){
                if(!isTerm()){
                    return false;
                }
                t = testTokenStream.read();
            }
            if(t != null)
                testTokenStream.unread();
        }
        return true;
    }

    private boolean isTerm() throws Exception {
        if(!isPrimary())
            return false;
        Token t = testTokenStream.read();
        if(t != null) {
            while(t != null && (t.getKind() == '*' || t.getKind() == '/')){
                if(!isTerm()){
                    return false;
                }
                t = testTokenStream.read();
            }
            if(t != null)
                testTokenStream.unread();
        }
        return true;
    }

    private boolean isPrimary() throws Exception {
        Token t = testTokenStream.read();
        if(t.getKind() == Token.number){
            return true;
        }
        if(t.getKind() == '(') {
            if (!isExpression() && t.getKind() == ')')
                return false;
        }
        return true;
    }
}
