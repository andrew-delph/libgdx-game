package main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Shared {
    public int count;

    public Shared() {
    }

    public void increase() {
        this.count += 1;
    }
}

// Task class to be executed (Step 1)
class Task implements Callable<Object> {
    private final String name;
    Shared shared;

    public Task(String s, Shared shared) {
        name = s;
        this.shared = shared;
    }

    // Prints task name and sleeps for 1s
    // This Whole process is repeated 5 times
    public Object call() throws Exception {
        this.shared.increase();
        System.out.println("SHARED!" + this.shared.count);
        //    {
        //      try {
        //        for (int i = 0; i <= 5; i++) {
        //          if (i == 0) {
        //            Date d = new Date();
        //            SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
        //            System.out.println(
        //                "Initialization Time for" + " task name - " + name + " = " + ft.format(d));
        //            // prints the initialization time for every task
        //          } else {
        //            Date d = new Date();
        //            SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
        //            System.out.println("Executing Time for task name - " + name + " = " +
        // ft.format(d));
        //            // prints the execution time for every task
        //          }
        //          Thread.sleep(1000);
        //        }
        //        System.out.println(name + " complete");
        //      } catch (InterruptedException e) {
        //        e.printStackTrace();
        //      }
        //    }
        return null;
    }
}

public class ThreadTest {
    // Maximum number of threads in thread pool
    static final int MAX_T = 3;

    public static void main(String[] args) throws InterruptedException {
        Shared shared = new Shared();
        // creates five tasks
        Task r1 = new Task("task 1", shared);
        Task r2 = new Task("task 2", shared);
        Task r3 = new Task("task 3", shared);
        Task r4 = new Task("task 4", shared);
        Task r5 = new Task("task 5", shared);

        // creates a thread pool with MAX_T no. of
        // threads as the fixed pool size(Step 2)
        ExecutorService pool = Executors.newFixedThreadPool(3);

        // passes the Task objects to the pool to execute (Step 3)
        //        pool.execute(r1);
        //        pool.execute(r2);
        //        pool.execute(r3);
        //        pool.execute(r4);
        //        pool.execute(r5);

        List<Task> list = new ArrayList<>();

        list.add(r1);
        list.add(r2);
        list.add(r3);
        list.add(r4);
        list.add(r5);

        pool.invokeAll(list);

        System.out.println("done!!");
        // pool shutdown ( Step 4)
        pool.shutdown();
        //        pool.awaitTermination(500, TimeUnit.SECONDS);
        //        System.out.println("notifyAll!!");
    }
}
