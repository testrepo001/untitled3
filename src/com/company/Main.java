package com.company;

class Q {
    int value;
    boolean ValueSet = false;

    synchronized int get() {
        while (!ValueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
        System.out.println("Received " + value);
        ValueSet = false;
        notify();
        return value;
    }

    synchronized void set(int val) {
        while (ValueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
        value = val;
        ValueSet = true;
        System.out.println("Transmitted " + val);
        notify();
    }
}

class Producer implements Runnable {
    Q q;

    Producer(Q q) {
        this.q = q;
        Thread t = new Thread(this, "Producer");
        t.start();
    }

    @Override
    public void run() {
        int i = 1;
        while (i<20) {
            q.set(i++);
        }
    }
}

class Consumer implements Runnable {
    Q q;

    Consumer(Q q) {
        this.q = q;
        new Thread(this, "Consumer").start();
    }

    @Override
    public void run() {
        while(true){
            q.get();
        }
    }
}

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello");
        System.out.println("World");
        Q q = new Q();
        new Producer(q);
        new Consumer(q);
    }
}
