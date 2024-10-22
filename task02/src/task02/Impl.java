package task02;

public class Impl implements Runnable {
    
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("thread: " + Thread.currentThread().getName() + " " + i);
        }
    }
}
