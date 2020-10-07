package com.davesborges.CalculatorX;

import java.util.ArrayList;
import java.util.Arrays;

public class BasicParser implements Parser{
    private TokenStream tokenStream;
    private TokenStream testTokenStream;
    private ArrayList<String> results;
    private Scope scope;

    public BasicParser(TokenStream tokenStream, Scope scope){
        this.tokenStream = tokenStream;
        results = new ArrayList<>();
        this.scope = scope;

    }


    @Override
    public boolean checkGrammar() throws Exception {
        GrammarChecker grammarChecker = new GrammarChecker(new BasicTokenStream(tokenStream), scope);
        grammarChecker.parseNextStatement();
        return true;
    }

    @Override
    public String[] evaluate() throws Exception {
        GrammarEvaluator grammarEvaluator;
        String[] evaluationResult = new String[0];
        if(!tokenStream.isEOF()) {
            grammarEvaluator = new GrammarEvaluator(new BasicTokenStream(tokenStream), scope);
            evaluationResult = grammarEvaluator.evaluate();
            results.addAll(Arrays.asList(evaluationResult));
        }
        return evaluationResult;
    }

    @Override
    public void checkAllGrammar() throws Exception {
        TokenStream tokenStream = new BasicTokenStream(this.tokenStream);
        GrammarChecker grammarChecker = new GrammarChecker(tokenStream, scope);
        while(!tokenStream.isEOF()){
            grammarChecker.parseNextStatement();
        }
    }

    @Override
    public String[] evaluateAll() throws Exception {
        ArrayList<String> results = new ArrayList<>();
        while(!tokenStream.isEOF()){
            GrammarEvaluator grammarEvaluator = new GrammarEvaluator(tokenStream, scope);
            results.addAll(Arrays.asList(grammarEvaluator.evaluate()));
        }
        return results.toArray(new String[0]);
    }


}
