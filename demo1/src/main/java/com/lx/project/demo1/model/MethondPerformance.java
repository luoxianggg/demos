package com.lx.project.demo1.model;

public class MethondPerformance {
    private Long begin;
    private  Long end;

    public  MethondPerformance(){
        this.begin = System.currentTimeMillis();
    }
    public Long printPerformance(){
        this.end = System.currentTimeMillis();
        long elapse = this.end - this.begin;
        return elapse;
    }
}
