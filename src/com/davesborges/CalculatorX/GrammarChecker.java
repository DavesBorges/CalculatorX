package com.davesborges.CalculatorX;

import java.util.ArrayList;

public class GrammarChecker {
    private TokenStream tokenStream;
    private Scope scope;

    public GrammarChecker(TokenStream tokenStream, Scope scope){
        this.tokenStream = tokenStream;
        this.scope = new Scope(scope);
    }
    public boolean parseNextStatement() throws Exception {
        Position position = tokenStream.getCurrentPosition();
        try{
            isStatement();
        }

        catch(ParseException parseException){
            parseException.setStartingPosition(position);
            throw parseException;
        }
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
                tokenStream.read();
                t = tokenStream.read();
                tokenStream.unread();
                tokenStream.unread();
                tokenStream.unread();
                if(t.getKind() == '('){
                    return isFunctionDeclaration();
                }
                isDeclaration();
                break;

            case Token.statementEnd:
                return true;
            default:
                tokenStream.unread();
                isExpression();

        }
        t = tokenStream.read();
        if(t == null){
            tokenStream.unread();
            throw new ParseException("Expected token " + Token.statementEnd, tokenStream.read().getLocation());
        }
        if(t.getKind() != Token.statementEnd){
            throw new ParseException("Expected token " + Token.statementEnd + " before " + t.getStringValue(), t.getLocation());
        }
        return true;
    }

    private void isAssignment() throws Exception {
        Token t = tokenStream.read();
        String variableName = t.getStringValue();
        if(!scope.isVariableDeclared(variableName)){
            throw new ParseException(t.getStringValue() + " is not a declared variable ", t.getLocation());
        }
        if(scope.isConstant(variableName))
            throw new ParseException(t.getStringValue() + " is a constant and cannot be assigned", t.getLocation());

        tokenStream.read();
        isExpression();

    }

    private boolean isDeclaration() throws Exception {
        //read let
        tokenStream.read();
        Token t;
        t = tokenStream.read();
        Token nexToken = t;
        switch (t.getKind()){
            case Token.name:
                String name = t.getStringValue();
                String position = t.getLocation();
                t = tokenStream.read();
                switch (t.getKind()) {
                    case '(':
                        tokenStream.unread();
                        tokenStream.unread();
                        return isFunctionDeclaration();
                    case '=':
                        if(scope.isFunctionDeclared(name) || scope.isVariableDeclared(name)){
                            throw new ParseException("Name " + name + " already declared ", position);
                        }
                        isExpression();
                        scope.declareVariable(name);
                        break;
                    default:
                        throw new ParseException("Expected '=' or '('", t.getLocation());
                }
                break;
            case Token.constKey:
                t = tokenStream.read();
                if(t.getKind() != Token.name)
                    throw new ParseException("Expected name ", t.getLocation());
                name = t.getStringValue();
                position = t.getLocation();
                name = t.getStringValue();
                t = tokenStream.read();
                if(t.getKind() != '=')
                    throw new ParseException("Expected '=' ", t.getLocation());
                isExpression();
                if(scope.isFunctionDeclared(name) || scope.isVariableDeclared(name)){
                            throw new ParseException("Name " + name + " already declared ", position);
                }
                //Random and unimportant number
                scope.defineConstant(name, 10);
                break;
            default:
                throw new ParseException("Expected name or 'const' keyword", t.getLocation());
        }
        return true;

    }

    private boolean isFunctionDeclaration() throws Exception {
        tokenStream.read();
        Token t = tokenStream.read();
        String name = t.getStringValue();

        if(scope.isFunctionDeclared(name))
            throw new ParseException("Function " + t.getStringValue() + " is already declared ", t.getLocation());

        t = tokenStream.read();
        t = tokenStream.read();
        Scope functionScope = new Scope(scope);
        ArrayList<String> parameters = new ArrayList<>();
        while(t.getKind() != ')'){
            if(t.getKind() != Token.name)
                throw new ParseException("Expected paramter name ", t.getLocation());
            parameters.add(t.getStringValue());
            functionScope.declareVariable(t.getStringValue());
            t = tokenStream.read();
            if(t.getKind() ==','){
                t = tokenStream.read();
            }
        }
        t = tokenStream.read();
        if(t.getKind() != '=')
            throw new ParseException("Expected '=' before " + t.getStringValue(), t.getLocation());
        GrammarChecker grammarChecker = new GrammarChecker(tokenStream, functionScope);
        try{
            grammarChecker.parseNextStatement();
        }
        catch (ParseException parseException){
            throw new ParseException(" Invalid expression after '=': (" + parseException.getMessage() + ')', t.getLocation());
        }
        scope.defineFunction(name, new UserDefinedFunction(name, parameters.toArray(new String[0]), new Token[0], functionScope));
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
        if(!isSubTerm())
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

    private boolean isSubTerm() throws Exception {
        isPrimary();
        if(tokenStream.read().getKind() == Token.exponent){
            return isPrimary();
        }
        tokenStream.unread();
        return true;
    }
    private boolean isPrimary() throws Exception {
        Token precedingToken = null;
        if(tokenStream.hasPreceding()){
            precedingToken = tokenStream.getPreceding();
        }
        Token t = tokenStream.read();
        if(t.getKind() == Token.number){
            return true;
        }
        if(t.getKind() == '-'){
            if(precedingToken != null && "+-*/".contains(precedingToken.getStringValue())){
                throw new ParseException("Two consecutive arithimetic operators ", t.getLocation());
            }
            return isPrimary();
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
            else
                throw new ParseException("Name " + t.getStringValue() + " not declared ", t.getLocation());
        }
        if(t.getKind() == '(') {
            isExpression();
            t = tokenStream.read();
            if (t.getKind() != ')')
                throw new ParseException("Expected ')' before " + t.getStringValue(), t.getLocation());
            return true;
        }
        throw new ParseException("Unexpected token " + t.getStringValue(), t.getLocation());
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
                throw new ParseException("Expected ',' or ')' before " + t.getStringValue(), t.getLocation());
        }
        if(t.getKind() != ')')
            throw new ParseException("Expected ')' + before " + t.getStringValue(), t.getLocation());

        if(arguments.size() != scope.getParameters(functionName).length){
            throw new ParseException(
                    "Wrong number of arguments. Expected " + scope.getParameters(functionName).length
                    + " but found " + arguments.size(), t.getLocation(false));
        }
        return true;
    }

}
