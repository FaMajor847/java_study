package com.mav.ex3;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MyThreadPool pool = new MyThreadPool(5);

        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() +
                        " выполняю задачу " + taskId);
                try {
                    Thread.sleep(1000); // Имитация работы
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination();
        System.out.println("Все задачи выполнены");
    }
}
