package com.davesborges.CalculatorX;

import java.util.ArrayList;

public class GrammarEvaluator {
    private TokenStream tokenStream;
    private ArrayList<String> results;
    private Scope scope;

    public GrammarEvaluator(TokenStream tokenStream, Scope scope){
        this.tokenStream = tokenStream;
        results = new ArrayList<>();
        this.scope = scope;
    }

    public String[] evaluate() throws Exception {
        results.clear();
            String result = evaluateStatement();
            results.add(result);

        String[] resultsArray = new String[results.size()];
        return results.toArray(resultsArray);
    }

    private String evaluateStatement() throws Exception {
        Token t = tokenStream.read();
        String result = "";
        switch (t.getKind()){
            case Token.name:
               t = tokenStream.read();
                tokenStream.unread();
                if(t.getKind() == '='){
                    tokenStream.unread();
                    performAssignment();
                    break;
                }
            case Token.number:
                tokenStream.unread();
                result = Double.toString(evaluateExpression());
                break;
            case Token.declarationKeyWord:
                tokenStream.unread();
                performDeclaration();
                return "";
            case Token.statementEnd:
                return "";
            default:
                tokenStream.unread();
                return Double.toString(evaluateExpression());
        }
        tokenStream.read();
        return result;
    }

    private void performDeclaration() throws Exception {
        tokenStream.read();
        tokenStream.read();
        Token t;
        t = tokenStream.read();
        switch (t.getKind()){
            case '(':
                tokenStream.unread();
                tokenStream.unread();
                performFunctionDeclaration();
                break;
            case '=':
                tokenStream.unread();
                tokenStream.unread();
                performVariableDeclaration();
                break;
            default:
                throw new ParseException("Expected '=' or '('", t.getLocation());
        }
    }

    private void performVariableDeclaration() throws Exception {
        String name = tokenStream.read().getStringValue();
        tokenStream.read();
        scope.defineVariable(name, evaluateExpression());
        tokenStream.read();
    }

    private void performFunctionDeclaration(){
        Token t = tokenStream.read();
        String name = t.getStringValue();

        tokenStream.read();
        t = tokenStream.read();
        Scope functionScope = new Scope(scope);
        ArrayList<String> parameters = new ArrayList<>();
        while(t.getKind() != ')'){
            parameters.add(t.getStringValue());
            functionScope.declareVariable(t.getStringValue());
            t = tokenStream.read();
            if(t.getKind() ==','){
                t = tokenStream.read();
            }
        }

        tokenStream.read();
        t = tokenStream.read();
        ArrayList<Token> body = new ArrayList<>();
        while(t != null){
            body.add(t);
            if(t.getKind() == Token.statementEnd){
                break;
            }
            t = tokenStream.read();
        }
        scope.defineFunction(
            name, new UserDefinedFunction(name, parameters.toArray(new String[0]), body.toArray(new Token[0]), functionScope)
        );
    }

    private void performAssignment() throws Exception {
        Token t = tokenStream.read();
        String variableName = t.getStringValue();
        if(!scope.isVariableDeclared(variableName)){
            throw new ParseException(variableName + " is not declared ", t.getLocation());
        }
        tokenStream.read();
        scope.assignVariable(variableName, evaluateExpression());
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
                        throw new ParseException("Cannot divide to zero ", t.getLocation());
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
            if(tokenStream.read().getKind() == Token.exponent)
                return Math.pow(t.getValue(), evaluatePrimary());
            tokenStream.unread();
            return t.getValue();
        }
        if(t.getKind() == Token.name){
            String name = t.getStringValue();
            t = tokenStream.read();
            if(t.getKind() == '('){
                tokenStream.unread();
                tokenStream.unread();
                return performFunctionCall();
            }
            tokenStream.unread();
            return scope.getValue(name);
        }
        if(t.getKind() == '-'){
            return - evaluatePrimary();
        }
        if(t.getKind() == '('){
            double d = 0;
            d = evaluateExpression();
            if(tokenStream.read().getKind() == ')')
                return d;
            return d;
        }
        return 0;
    }

    private double performFunctionCall() throws Exception {
        String functionName = tokenStream.read().getStringValue();
        Token t = tokenStream.read();
        ArrayList<Double> arguments = new ArrayList<>();
        double d = 0;
        while(t.getKind() != ')'){
            d = evaluateExpression();
            arguments.add(d);
            t = tokenStream.read();

        }
        double[] args = new double[arguments.size()];
        for(int i = 0; i < args.length; ++i)
            args[i] = arguments.get(i);
        return scope.
                callFunction(functionName, args);
    }

}
