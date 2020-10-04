package com.davesborges.CalculatorX;

public class Main {

    public static void main(String[] args) throws Exception {
        Application application;
        if(args.length == 2)
            application = new FileOrientedApplication(args);

        else
            application = new Application();

       application.run();

    }
}
