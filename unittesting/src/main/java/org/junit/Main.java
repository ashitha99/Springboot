package org.junit;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Calculator calculator=new Calculator();
        int sum=calculator.add(2,3);
        System.out.println("The result is :"+sum) ;
        int subtraction=calculator.subtract(8,4);
        System.out.println("The result is:"+subtraction);
        int multiplication=calculator.multiple(8,4);
        System.out.println("The result is:"+multiplication);
        int division=calculator.divide(200,10);


    }
}