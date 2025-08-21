package com.mav.ex3;

import java.util.LinkedList;
import java.util.List;

public class MyThreadPool extends Thread {
    private final LinkedList<Runnable> tasks = new LinkedList<>();
    private final List<Thread> workers = new LinkedList<>();
    private volatile boolean isShutdown = false;

    public MyThreadPool(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            Thread worker = new Thread(this::workerLoop);
            worker.start();
            workers.add(worker);
        }
    }

    public synchronized void execute(Runnable task) {
        if (isShutdown) throw new IllegalStateException("Pool is shutting down");
        tasks.add(task);
        notify();
    }

    public synchronized void shutdown() {
        isShutdown = true;
        notifyAll();
    }

    public void awaitTermination() throws InterruptedException {
        for (Thread worker : workers) {
            worker.join();
        }
    }

    private void workerLoop() {
        while (true) {
            Runnable task;
            synchronized (this) {
                while (tasks.isEmpty() && !isShutdown) {
                    try {
                        wait();
                    } catch (InterruptedException ignored) {}
                }
                if (tasks.isEmpty() && isShutdown) {
                    return;
                }
                task = tasks.removeFirst();
            }
            try {
                task.run();
            } catch (Exception e) {
                System.err.println("Task failed: " + e.getMessage());
            }
        }
    }
}
