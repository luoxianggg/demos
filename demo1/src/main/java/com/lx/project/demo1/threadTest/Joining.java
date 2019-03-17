package com.lx.project.demo1.threadTest;

public class Joining {
    public static void main(String[] args){
        Sleeper
                sleeper = new Sleeper("Sleepy",1500),
                grumpy = new Sleeper("Grumpy",1500);
        Jonier
                dopey = new Jonier("Dopey",sleeper),
                doc = new Jonier("Doc",grumpy);
        grumpy.interrupt();
    }
}
