package com.lx.project.demo1.threadTest;

public class Jonier extends Thread {
    private Sleeper sleeper;
    public Jonier(){}
    public Jonier(String name,Sleeper sleeper){
        super(name);
        this.sleeper = sleeper;
        start();
    }
    @Override
    public void run(){
        try{
            sleeper.join();
        }catch (InterruptedException e){
            System.err.println("Interrupted");
        }
        System.out.println(getName()+ " join completed");
    }
}
