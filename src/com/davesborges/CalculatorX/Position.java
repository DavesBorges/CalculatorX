package com.davesborges.CalculatorX;

public class Position {
    private int lineNr;
    private int columnNr;
    private String file;

    public Position(Position position){
        lineNr = position.getLineNr();
        columnNr = position.getColumnNr();
        this.file = position.file.toString();
    }

    public Position(String file){
        lineNr = 1;
        columnNr = 0;
        this.file = file;
    }

    public Position(int lineNr, int columnNr, String file) throws Exception {
        checkNonNegativity(lineNr, columnNr);
        this.lineNr = lineNr;
        this.columnNr = columnNr;
        this.file = file;
    }

    public Position(int lineNr, String file) throws Exception {
        checkNonNegativity(lineNr);
        this.lineNr = lineNr;
        this.columnNr = 0;
        this.file = file;
    }

    public void advanceColumn(){
        columnNr++;
    }

    public String getLineToString(){
        return file + " line " + lineNr;
    }

    @Override
    public String toString(){
        return getLineToString() + " col " + columnNr;
    }
    //Setters
    public void setNewPosition(int newLineNr, int newColumnNr) throws Exception {
        checkNonNegativity(newLineNr, newColumnNr);
        this.lineNr = newLineNr;
        this.columnNr = newColumnNr;
    }

    public void setNewPosition(int newLineNr) throws Exception {
        checkNonNegativity(newLineNr);
        this.lineNr = newLineNr;
        this.columnNr = 0;
    }

    public void setColumnNr(int newColumnNr) throws Exception {
        checkNonNegativity(newColumnNr);
        this.columnNr = newColumnNr;
    }

    public void setNewPostion(int[] newPostion) throws Exception {
        checkNonNegativity(newPostion[0]);
        checkNonNegativity(newPostion[2]);

        this.lineNr = newPostion[0];
        this.columnNr = newPostion[1];
    }

    //Getters
    public int[] getPosition(){
        return new int[]{lineNr, columnNr};
    }

    public int getLineNr(){
        return lineNr;
    }

    public int getColumnNr(){
        return columnNr;
    }

    protected void checkNonNegativity(int number) throws Exception {
        if(number < 0)
            throw new Exception("Position cannot have negative values");
    }

    protected void checkNonNegativity(int number1, int number2) throws Exception {
        checkNonNegativity(number1);
        checkNonNegativity(number2);
    }


    public String getFile() {
        return file;
    }
}
