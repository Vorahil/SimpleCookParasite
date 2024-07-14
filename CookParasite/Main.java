package CookParasite;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    private static final Queue<String> queue = new LinkedList<>();

    public static void main(String[] args) {
        new Thread(() -> add("宫保鸡丁"), "厨师1").start();
        new Thread(() -> add("鱼香肉丝"), "厨师2").start();

        new Thread(Main::eat, "食客1").start();
        new Thread(Main::eat, "食客2").start();
        new Thread(Main::eat, "食客3").start();

    }

    private static void add(String name) {
        while (true) {
            synchronized (queue) {//用queue上锁
                try {
                    queue.offer(name);
                    System.out.println(new Date() +"的时候"+ Thread.currentThread().getName() + "出餐了，菜品是" + name);
                    queue.notifyAll();//唤醒所有带锁的线程
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
        }
    }

    private static void eat() {
        while (true) {
            synchronized (queue) {
                try {
                    while (queue.isEmpty()) queue.wait();//队列空，线程等待
                    String name = queue.poll();
                    System.out.println(new Date()+"的时候"+Thread.currentThread().getName()+"拿到"+name+"了");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
