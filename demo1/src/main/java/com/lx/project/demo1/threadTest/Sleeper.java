package com.lx.project.demo1.threadTest;

public class Sleeper extends Thread {
    private int duration;

    public Sleeper(){}
    public  Sleeper(String name,int sleepTime){
        super(name);
        duration = sleepTime;
        start();
    }
    @Override
    public void run(){
        try{
            sleep(duration);
        }catch (InterruptedException e){
            System.err.println(getName() + "was interrupted ." + "isInterrupt():"+ isInterrupted());
        return;
        }
        System.out.println(getName()+"has awakened");
    }
}
