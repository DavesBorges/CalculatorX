package com.davesborges.CalculatorX;

import java.util.ArrayList;
import java.util.Arrays;

public class UserDefinedFunction implements Function{
    private String name;
    private Token[] body;
    private String[] parameters;
    private Scope scope;

    public UserDefinedFunction(String name, String[] parameters, Token[] body, Scope scope) {
        this.name = name;
        this.body = body;
        this.parameters = parameters;
        this.scope = scope;
    }

    @Override
    public double onCall(double[] arguments) throws Exception {
        Scope scope = new Scope(this.scope);
        for(int i = 0; i < parameters.length; ++i){
            scope.assignVariable(parameters[i], arguments[i]);
        }
        TokenStream tokenStream = new BasicTokenStream(body);
        Parser parser = new BasicParser(tokenStream, scope);
        String[] results = parser.evaluate();
        return Double.parseDouble(results[results.length - 1]);
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public String[] getParameters(){
        return Arrays.asList(parameters).toArray(new String[parameters.length]);
    }
}
