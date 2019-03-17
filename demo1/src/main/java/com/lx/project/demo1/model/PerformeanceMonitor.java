package com.lx.project.demo1.model;

public class PerformeanceMonitor {
    private static ThreadLocal<MethondPerformance> t = new ThreadLocal<>();
    public   static void begin(){
        MethondPerformance mp = new MethondPerformance();
        t.set(mp);
    }
    public static Long end(){
        MethondPerformance mp = t.get();
       return mp.printPerformance();
    }
}
