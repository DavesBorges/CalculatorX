# CalculatorX

A calculator for mathemathics expressions
<br>

#### What is CalculatorX?
  CalculatorX is a calculator that will support decimal number expressions with features such as:
  * Basic Operations with parentheses and operator precedence:
    * Addition
    * Subraction
    * Multiplication
    * Division
    * Exponents
  
  * Additional Features:
    * Variables
      * Constant Variables
    * User Defined Functions
    * File evaluation and File output


#### How to use CalculatorX?
 * Input and Output from/to terminal:
   * cmd: "java -jar CalculatorX.jar"

 * Input from file and output to terminal or File
   * cmd: "java -jar CalculatorX.jar inputFile outputFile"

 * **NOTE: If using the executable release ommit the "java -jar" part**

#### How it Works
 -Just type your mathematical expression and press enter to get the result. 
 Ex: if you type "2 + 5" you will get 7.
 CalculatorX supports addition(+), subraction(-), division(/) and 
	multiplication(*), powers(x^y).
 You can use parentheses to modify the order of evaluation such that if
 you type "2 * (2 + 1)" the result will be 6


##### VARIABLES
   In addition to the basic operations, CalculatorX also supports 
   variables. You can do all the arithimetic operations with variables
   Here is an example of how to use variables: "let x = 22". 
   This declares a variable named x that holds the value 22
   If I want to modify the variable to hold the value 10 instead of 22,
   I would simply type "x = 10"


##### FUNCTIONS
  Built in functions 
  sqrt(x) -> square root
  You can define your own functions in CalculatorX. As an example, 
  here is how to declare a function that computes the double of a number: 
  "let double(x) = x * 2"

  Functions can have zero or more than one argument, for exemple the 
  following function takes 2 parameters and computes their average:
  "let average(x,y) = (x + y) / 2"
  To call a function simply type the name of the function followed by 
  the arguments values. Using as an example the preceding function:
  "average(5,7)" -> the result will be 6


##### To exit just type "exit" and press enter;


