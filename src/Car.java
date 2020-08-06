import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class Car implements Runnable {




      AtomicInteger fin = new AtomicInteger(0);

    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;

    }


    CyclicBarrier prepToStart;
    private Race race;
    private int speed;
    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed,CyclicBarrier prepToStart) {
        this.race = race;
        this.speed = speed;
        this.prepToStart = prepToStart;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            prepToStart.await();
            System.out.println(this.name + " готов");
            prepToStart.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

        if(fin.incrementAndGet() == 1){
            System.out.println(name + " Финишировал!");

        }

        try {
            prepToStart.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}


abstract class Stage {
    protected int length;
    protected String description;
    public String getDescription() {

        return description;
    }
    public abstract void go(Car c);
}
