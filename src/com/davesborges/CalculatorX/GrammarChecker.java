package com.davesborges.CalculatorX;

import java.util.ArrayList;

public class GrammarChecker {
    private TokenStream tokenStream;
    private Scope scope;

    public GrammarChecker(TokenStream tokenStream, Scope scope){
        this.tokenStream = tokenStream;
        this.scope = scope;
    }
    public boolean parseNextStatement() throws Exception {
        isStatement();
        return true;
    }

    private boolean isStatement() throws Exception {
        Token t = tokenStream.read();
        switch (t.getKind()){
            case Token.name:
                if(!scope.isVariableDeclared(t.getStringValue()) && !scope.isFunctionDeclared(t.getStringValue())){
                    throw new ParseException("Undeclared name " + t.getStringValue(),
                                            t.getLocation());
                }
                t = tokenStream.read();
                tokenStream.unread();
                if(t.getKind() == '='){
                    tokenStream.unread();
                    isAssignment();
                    break;
                }
            case Token.number:
                tokenStream.unread();
                isExpression();
                break;
            case Token.declarationKeyWord:
                tokenStream.unread();
                isDeclaration();
                return true;
            default:
                System.out.println("Not implemented");

        }
        t = tokenStream.read();
        if(t == null){
            tokenStream.unread();
            throw new ParseException("Expected token " + Token.statementEnd, tokenStream.read().getLocation());
        }
        if(t.getKind() != Token.statementEnd){
            throw new ParseException("Expected token " + Token.statementEnd, t.getLocation());
        }
        return true;
    }

    private void isAssignment() throws Exception {
        Token t = tokenStream.read();
        String variableName = t.getStringValue();
        if(!scope.isVariableDeclared(variableName)){
            throw new ParseException(t.getStringValue() + " is not a declared variable ", t.getLocation());
        }
        tokenStream.read();
        isExpression();
    }

    private boolean isDeclaration() throws Exception {
        //read let
        tokenStream.read();
        Token t;
        t = tokenStream.read();
        if(t.getKind() != Token.name){
            throw new ParseException("Expected name ", t.getLocation());
        }
        String name = t.getStringValue();
        t = tokenStream.read();
        switch (t.getKind()){
            case '(':
                tokenStream.unread();
                tokenStream.unread();
                isFunctionDeclaration();
                break;
            case '=':
                isExpression();
                break;
            default:
                throw new ParseException("Expected '=' or '('", t.getLocation());
        }
        if(scope.isFunctionDeclared(name) || scope.isVariableDeclared(name))
            throw new ParseException("Name " + t.getStringValue() + " is already declared", t.getLocation());
        return true;
    }

    private boolean isFunctionDeclaration() throws Exception {
        Token t = tokenStream.read();
        String name = t.getStringValue();

        if(scope.isFunctionDeclared(name))
            throw new ParseException("Function " + t.getStringValue() + " is already declared ", t.getLocation());

        t = tokenStream.read();
        t = tokenStream.read();
        Scope functionScope = new Scope(scope);
        while(t.getKind() != ')'){
            if(t.getKind() != Token.name)
                throw new ParseException("Expected paramter name ", t.getLocation());
            functionScope.declareVariable(t.getStringValue());
            t = tokenStream.read();
            if(t.getKind() ==','){
                t = tokenStream.read();
            }
        }
        t = tokenStream.read();
        if(t.getKind() != '=')
            throw new ParseException("Expected '='", t.getLocation());
        GrammarChecker grammarChecker = new GrammarChecker(tokenStream, functionScope);
        grammarChecker.parseNextStatement();
        return true;
    }

    boolean isExpression() throws Exception {
        if(!isTerm()){
            return false;
        }

        Token t = tokenStream.read();
        if(t != null) {
            while(t != null && (t.getKind() == '+' || t.getKind() == '-')){
                if(!isTerm()){
                    return false;
                }
                t = tokenStream.read();
            }
            if(t != null)
                tokenStream.unread();
        }
        return true;
    }

    private boolean isTerm() throws Exception {
        if(!isPrimary())
            return false;
        Token t = tokenStream.read();
        if(t != null) {
            while(t != null && (t.getKind() == '*' || t.getKind() == '/')){
                if(!isTerm()){
                    return false;
                }
                t = tokenStream.read();
            }

            if(t != null)
                tokenStream.unread();
        }
        return true;
    }

    private boolean isPrimary() throws Exception {
        Token t = tokenStream.read();
        if(t.getKind() == Token.number){
            return true;
        }

        if(t.getKind() == Token.name){
            String name = t.getStringValue();
            if(scope.isVariableDeclared(name)){
                return true;
            }
            if(scope.isFunctionDeclared(name)){
                tokenStream.unread();
                return isFunctionCall();
            }
        }
        if(t.getKind() == '(') {
            isExpression();
            t = tokenStream.read();
            if (t.getKind() != ')')
                throw new ParseException("Expected ')'", t.getLocation());
            return true;
        }
        throw new ParseException("Invalid primary " + t.getStringValue(), t.getLocation());
    }

    private boolean isFunctionCall() throws Exception {
        String functionName;
        Token t = tokenStream.read();
        functionName = t.getStringValue();

        t = tokenStream.read();
        if(t.getKind() != '(') {
            throw new ParseException("Expected function Call", t.getLocation());
        }
        ArrayList<Double> arguments = new ArrayList<>();

        while(t.getKind() != ')' && isExpression()){
            arguments.add(t.getValue());
            t = tokenStream.read();
            if(t.getKind() != ',' && t.getKind() != ')')
                throw new ParseException("Expected ',' or ')' ", t.getLocation());
        }
        if(t.getKind() != ')')
            throw new ParseException("Expected ')'", t.getLocation());

        if(arguments.size() != scope.getParameters(functionName).length){
            throw new ParseException(
                    "Wrong number of arguments. Expected " + scope.getParameters(functionName).length
                    + " but found" + arguments.size(), t.getLocation(false));
        }
        return true;
    }

}
