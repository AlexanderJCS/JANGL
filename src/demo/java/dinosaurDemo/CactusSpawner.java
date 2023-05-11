package dinosaurDemo;

import jglt.time.Clock;

import java.util.ArrayList;
import java.util.List;

public class CactusSpawner {
    private final List<Cactus> cacti;
    private double cactusTimer;
    private final double spawnFrequency;
    private final float floorHeight;

    public CactusSpawner(double spawnFrequency, float floorHeight) {
        this.cacti = new ArrayList<>();

        this.cactusTimer = 0;
        this.spawnFrequency = spawnFrequency;
        this.floorHeight = floorHeight;
    }

    public void update(Player dinosaur) {
        this.cactusTimer += Clock.getTimeDelta();

        // Spawn new cacti
        if (cactusTimer > this.spawnFrequency) {
            this.cactusTimer = 0;
            this.cacti.add(new Cactus(this.floorHeight, 0.8f));
        }

        // Clear any offscreen cacti to prevent a leak
        for (int i = this.cacti.size() - 1; i >= 0; i--) {
            if (this.cacti.get(i).getRect().x2 < -1) {
                this.cacti.get(i).close();
                this.cacti.remove(i);
            }
        }

        // Update all the cacti
        for (Cactus cactus : cacti) {
            cactus.update();

            if (dinosaur.getRect().collidesWith(cactus.getRect())) {
                System.exit(0);
            }
        }
    }

    public void draw() {
        for (Cactus cactus : cacti) {
            cactus.draw();
        }
    }
}
