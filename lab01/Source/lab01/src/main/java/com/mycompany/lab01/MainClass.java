/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lab01;

import java.util.Scanner;
/**
 *
 * @author GTAO
 */

class Term {
    
    float factor;
    int power;
    
    Term stringToTerm(String s){
        String[] Str = new String[2];
        Str[0] = Str[1] = "";
        boolean flag = true, ispow1 = true;
        for (int i = 0; i < s.length(); i++){
            if (s.charAt(i) == 'x'){
                flag = false;
                continue;
            }
            if (s.charAt(i) == '^'){
                ispow1 = false;
                continue;
            }
            if (flag) Str[0] += s.charAt(i);
            else if (ispow1) Str[1] = "1";
            else Str[1] += s.charAt(i);
        }
        
        Term ret = new Term();
        
        
        if (Str[0].length() == 0) ret.factor = 0;
        else ret.factor = Float.parseFloat(Str[0]);
        if (Str[1].length() == 0) ret.power = 0;
        else ret.power = Integer.parseInt(Str[1]);
        return ret;
    }
    
    String termToString(){
        String left, right;
        if (this.factor == 0) return "0";
        else if (this.factor == 1) left = "";
        else left = String.format("%.4f", this.factor);
        if (this.power == 0) right = "";
        else if (this.power == 1) right = "x";
        else right = "x^"+String.format("%.4f", this.power);
        if (left == right) return "1";
        return left+right;
    }
    
    Term Mult(Term a, Term b){
        Term c = new Term();
        c.factor = a.factor * b.factor;
        c.power = a.power + b.power;
        return c;
    }
    
    Term integratePower(){
        Term a = this;
        a.factor /= ++a.power;
        return a;
    }
    
}

public class MainClass {
    
    public static float f(float x, float y){
        return x*x+y*y;
    }
    
    public static void fn(int i, float[] x, float[] y){
        if (i == 0) { y[i] = 0; }
        else {
            fn(i-1, x, y);
            y[i] = y[i-1] + (x[i]-x[i-1])*f(x[i-1],y[i-1]);
        }
       
    }
    
    public static void divide (int n, float xn, float[] x){
        float h  = xn/n;
        for (int i = 0; i <= n; i++) { x[i] = h*i; }
    }
    
    

    
    public static String integrate_sum(String s){
        String[] Str = s.split("\\+");
        Term temp = new Term();
        for (int i = 0; i < Str.length; i++) {
            Str[i] = temp.stringToTerm(s).integratePower().termToString();
        }
        String ret = Str[0];
        for (int i = 1; i < Str.length; i++) {
            ret = String.join("+", ret, Str[i]);
        }
        return ret;
    }
    
    public static String string_power(String s){
        String[] Str = s.split("\\+");
        String[] ret = new String[Str.length + 1 + Str.length*(Str.length - 1)/2];
        Term temp = new Term();
        for (int i = 0; i < Str.length; i++){
            temp = temp.stringToTerm(Str[i]);
            temp = temp.Mult(temp, temp);
            ret[i] = temp.termToString();
        }
        Term temp2 = new Term();
        for (int i = 0; i < Str.length-1; i++){
            for (int j = i + 1; j < Str.length; j++){
                temp = temp.stringToTerm(Str[i]);
                temp2 = temp2.stringToTerm(Str[i]);
                temp = temp.Mult(temp, temp2);
                ret[i+j+Str.length] = temp.termToString();
            }
        }
        
        String rets = ret[0];
        for (int i = 1; i < ret.length-1; i++) {
            rets = String.join("+", rets, ret[i]);
        }
        return rets;
    }
    
    public static String PicardMethod(int pow){
        String first = String.format("%.4f", 1.0/3)+"x^3";
        if (pow == 1) { System.out.print("Picard [1]: "+first); return first; }
        else {
            String ret = first + "+" + integrate_sum(string_power(PicardMethod(pow - 1)));
            System.out.printf("Picard [%d]: %s", pow, ret);
            return ret;
        }
    }
    
    public static void main(String[] args) {
        
        Scanner in = new Scanner(System.in);
        
        System.out.println("\t[EULER]");
        
        System.out.print("Enter ammount of divisions: ");
        int n = in.nextInt();
        
        System.out.print("Enter the top x value: ");
        float xn = in.nextFloat();
        
        
        
        float[] x = new float[n+1];
        float[] y = new float[n+1];
        
        divide (n, xn, x);
        fn (n, x, y);
        for (int i = 0; i <= n; i++){
            System.out.println("x"+i+": "+x[i]+"\t\ty"+i+": "+y[i]);
        }
        
        System.out.println("\t[PICARD]");
        
        System.out.print("Enter required power: ");
        n = in.nextInt();
        
        PicardMethod(n);
        
        in.close();
         
         
    }
}
