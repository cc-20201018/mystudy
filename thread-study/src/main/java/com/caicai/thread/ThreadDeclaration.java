package com.caicai.thread;

/**
 * 线程声明
 *
 * @author caizhengwei
 * @since 2021/1/14 16:03
 */
public class ThreadDeclaration {
    private static final Object lock = new Object();

    /*
     * 并发和并行的概念
     * 1.cpu的发展趋势：核心数目依旧会越来越多，根据摩尔定律，由于单个核心性能提升有严重的瓶颈问题，所以就加大核心数目来提升整体性能
     * 2.并发和并行的区别：所有的并发处理都有排队等候，唤醒和执行这三个步骤，所以并发是宏观概念，在微观上他们都是序列被处理的，只不过资源不会
     * 在某一个上被阻塞（一般是通过时间片轮转），所以在宏观上多个几乎同时到达的请求同时在被处理。如果是同一时刻到达的请求也会根据优先级的不同，
     * 先后进入队列排队等待执行。
     * 并发和并行是两个既相似但是却不同的概念：
     * 并发性：又称共行性，是指处理多个同时性活动的能力。
     * 并行：指同时发生两个并发事件，具有并发的含义。并发不一定并行，也可以说并发事件之间不一定要同一时刻发生。
     * 并发的实质是一个物理CPU（也可以是多个物理CPU）在若干个程序之间多路复用，并发性是对有限物理资源强制行使多用户共享以提高效率。
     *
     * 并行指两个或两个以上事件或活动在同一时刻发生，在多道程序环境下，并行使多个程序<同一时刻>可在不同CPU上<同时>执行。
     * 并发是在同一个cpu上同时（不是真正的同时，而是看起来同时，因为CPU在多个程序间来回的切换）运行多个程序。
     *
     * 并行是每一个CPU运行一个程序。
     * 并发是指一个处理器同时处理多个任务。
     *
     * 并行是指多个处理器或者多核的处理器同时处理多个不同的任务。
     * 并发是逻辑上的同时发生（simultaneous），而并行是物理上的同时发生。
     *
     * 那么并发，并行和多线程的关系呢？
     * 并行需要两个或两个以上的线程跑在不同的处理器上，并发可以跑在一个处理器上通过时间片进行切换。
     * 所以并发编程的目标是充分的利用处理器的每一个核，以达到最高的处理性能。并行包含并发，但并发小于并行。
     */
    public static void main(String[] args) {
        //声明线程的基础方式一，继承Thread类
        Thread thread1 = new Thread() {
            @Override
            public void run() {

                try {
                    synchronized (lock) {
                        //等待状态，释放锁
                        System.out.println("啊，我被挂起了，需要人来唤醒我，我那锁给你，你拿到了锁了来唤醒我");
                        lock.wait();
                        System.out.println("谢谢你，我被唤醒了！");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("继承Thread类，重写run方法");
            }
        };

        Thread threadNotify = new Thread(()->{
            synchronized (lock) {
                System.out.println("我来唤醒你啦");
                lock.notify();
                System.out.println("好啦，唤醒了一个被等待的线程");
            }
        });

        threadNotify.start();

        //声明线程的基础方式二，实现Runnable接口
        Thread thread2 = new Thread(() -> System.out.println("实现Runnable接口，重写run方法"));

        //线程优先级，线程优先级有10级，默认是5。
        int priority = thread1.getPriority();
        System.out.println(priority);

        /*
         *  线程状态，在Thread类中定义了一个枚举类来声明线程有哪些状态。
         *  共有六种，
         *  NEW，表示线程还未启动状态，也就是还没有调用start方法
         *  RUNNABLE，表示线程可运行状态，有两种情况一种是在运行，另外一种是在等待cpu资源。
         *  BLOCKED，表示线程阻塞状态，比如等待一个同步锁。
         *  WAITING，表示线程等待状态，比如调用wait方法，join方法或者LockSupport的park方法。
         *      举个例子，在一个对象中一个线程调用了它的Object#wait方法,那么这个线程就处于等待状态，它要等待
         *      另一个线程调用这个对象的notify方法或者notifyAll方法，在使用wait，notify方法时必须在一个同步块里面使用，
         *      这个同步锁锁定的对象就是调用wait，notify方法的对象,否则会报一个无效监视状态的运行时异常。
         *      在或者调用了join方法，那么将会等待这个指定的线程
         *      执行完毕，也就是这个线程状态为TERMINATED。
         *  TIMED_WAITING，表示线程处于一个指定了等待时间的等待状态，
         *      比如调用了sleep(long),Object#wait(long),join(long),LockSupport#parkNanos,LockSupport#parkUntil
         *  TERMINATED 表示线程结束状态，线程执行完毕了。
         */
        Thread.State state = thread1.getState();
        System.out.println(state);
        //线程启动
        thread1.start();
        thread2.start();
        try {
            thread1.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(thread1.getState());

    }
}
