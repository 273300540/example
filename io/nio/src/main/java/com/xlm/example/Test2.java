package com.xlm.example;

public class Test2 implements Runnable{
    public static Integer i = new Integer(0);

    @Override
    public void run() {
        while (true){
            synchronized (i){
                if(i<100){
                    i++;
                    System.out.println("i="+i);
                }else {
                    break;
                }
            }
        }
    }
    public static  void main(String[] args){
        Thread one =  new Thread(new Test2());
        Thread two =  new Thread(new Test2());
        one.start();
        two.start();

    }
}
