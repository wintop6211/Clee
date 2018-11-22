package test.java;

class A {
    int a = 4;
    int b = 3;
}

public class Playground {

    private Integer counter = 0;
    private final Integer tmp = 0;
    private final A a = new A();

    private void update() throws Exception {
        counter += 1;
        System.out.println(counter);
        a.a = 4;
        a.b = 5;
    }

    private void helper() {
        try {
            while (true) {
                Thread.sleep(1000);
                synchronized (tmp) {
                    update();
                }
            }
        } catch (Exception ignored) {

        }
    }

    public void startThreads() throws Exception {
        Thread thread1 = new Thread(this::helper);
        Thread thread2 = new Thread(this::helper);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    public static void main(String[] args) {
        try {
            Playground playground = new Playground();
            playground.startThreads();
        } catch (Exception ignored) {

        }
    }
}
