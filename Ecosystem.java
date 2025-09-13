import java.util.Random;

abstract class Animal {
    public abstract String toString();
}

class Bear extends Animal {
    @Override
    public String toString() {
        return "B";
    }
}

class Fish extends Animal {
    @Override
    public String toString() {
        return "F";
    }
}

public class Ecosystem {
    private Animal[] river;
    private Random random;

    public Ecosystem(int riverSize) {
        this.river = new Animal[riverSize];
        this.random = new Random();
        for (int i = 0; i < riverSize; i++) {
            int r = random.nextInt(3);
            if (r == 1) river[i] = new Fish();
            else if (r == 2) river[i] = new Bear();
        }
    }

    public void runStep() {
        Animal[] nextRiver = new Animal[river.length];
        for (int i = 0; i < river.length; i++) {
            if (river[i] == null) continue;
            int move = random.nextInt(3) - 1;
            int newPos = i + move;
            if (newPos < 0) newPos = 0;
            if (newPos >= river.length) newPos = river.length - 1;

            if (nextRiver[newPos] == null) {
                nextRiver[newPos] = river[i];
            } else {
                Animal other = nextRiver[newPos];
                if (other.getClass() == river[i].getClass()) {
                    placeRandom(nextRiver, river[i].getClass());
                    nextRiver[newPos] = river[i];
                } else {
                    if (river[i] instanceof Bear) {
                        nextRiver[newPos] = river[i];
                    }
                }
            }
        }
        river = nextRiver;
    }

    private void placeRandom(Animal[] riverArr, Class<?> cls) {
        int emptyIndex;
        do {
            emptyIndex = random.nextInt(riverArr.length);
        } while (riverArr[emptyIndex] != null);
        try {
            riverArr[emptyIndex] = (Animal) cls.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void visualize() {
        for (Animal animal : river) {
            System.out.print(animal == null ? "-" : animal.toString());
        }
        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
        Ecosystem eco = new Ecosystem(20);
        for (int i = 0; i < 50; i++) {
            eco.visualize();
            eco.runStep();
            Thread.sleep(300);
        }
    }
}