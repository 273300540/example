package com.xlm.example;

import java.util.concurrent.CountDownLatch;

public class CountDownLanchTest {
    public static  void main(String[] argv) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread thread = new Thread(){
            public void run(){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            }
        };
        thread.start();
        System.out.println("=============等待============");
        countDownLatch.await();
    }
}
