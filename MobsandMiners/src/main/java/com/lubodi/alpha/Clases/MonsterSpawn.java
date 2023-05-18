package com.lubodi.alpha.Clases;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class MonsterSpawn {

    private static final List<MonsterProbability> MONSTER_PROBABILITIES = new ArrayList<>();
    static {
        MONSTER_PROBABILITIES.add(new MonsterProbability(EntityType.ZOMBIE, 40));
        MONSTER_PROBABILITIES.add(new MonsterProbability(EntityType.SKELETON, 25));
        MONSTER_PROBABILITIES.add(new MonsterProbability(EntityType.SPIDER, 20));
        MONSTER_PROBABILITIES.add(new MonsterProbability(EntityType.ENDERMAN, 5,2));
        MONSTER_PROBABILITIES.add(new MonsterProbability(EntityType.CREEPER, 15));
        MONSTER_PROBABILITIES.add(new MonsterProbability(EntityType.VEX, 0.05,1));
        MONSTER_PROBABILITIES.add(new MonsterProbability(EntityType.BLAZE, 5, 2));
        MONSTER_PROBABILITIES.add(new MonsterProbability(EntityType.WITCH, 10, 2));
        MONSTER_PROBABILITIES.add(new MonsterProbability(EntityType.MAGMA_CUBE,10, 2));
        MONSTER_PROBABILITIES.add(new MonsterProbability(EntityType.SLIME,10, 2));
        MONSTER_PROBABILITIES.add(new MonsterProbability(EntityType.VINDICATOR, 10, 2));
        MONSTER_PROBABILITIES.add(new MonsterProbability(EntityType.PIGLIN_BRUTE, 20,3));
        MONSTER_PROBABILITIES.add(new MonsterProbability(EntityType.PILLAGER, 10,3));
        MONSTER_PROBABILITIES.add(new MonsterProbability(EntityType.EVOKER, 0.09, 3));
        MONSTER_PROBABILITIES.add(new MonsterProbability(EntityType.WITHER_SKELETON, 15, 3));
        MONSTER_PROBABILITIES.add(new MonsterProbability(EntityType.WITHER, 0.5, 3));
    }

    private static final Random random = new Random();

    public static void spawnRandomMonsters(Location location, int level) {
        int numMonsters = (int) (Math.random() * 20) + 1; // Generar un número aleatorio de 1 a 10 monstruos
        for (int i = 0; i < numMonsters; i++) {
            EntityType monsterType = getRandomMonster(level);
            Location monsterLocation;
            do {
                double x = location.getX() + Math.random() * 400 - 300;
                double z = location.getZ() + Math.random() * 400 - 300;
                monsterLocation = new Location(location.getWorld(), x, location.getY(), z);
            } while (Math.abs(monsterLocation.distance(location)) > 200); // Generar una ubicación cercana al aldeano
            location.getWorld().spawnEntity(monsterLocation, monsterType); // Invocar al monstruo en la ubicación generada
        }
    }

    private static EntityType getRandomMonster(int level) {
        int totalProbabilities = 0;
        for (MonsterProbability mp : MONSTER_PROBABILITIES) {
            if (mp.getLevel() <= level) {
                totalProbabilities += mp.getProbability();
            }
        }
        int randomProbability = random.nextInt(totalProbabilities);
        int cumulativeProbability = 0;
        for (MonsterProbability mp : MONSTER_PROBABILITIES) {
            if (mp.getLevel() <= level) {
                cumulativeProbability += mp.getProbability();
                if (randomProbability < cumulativeProbability) {
                    return mp.getEntityType();
                }
            }
        }
        return EntityType.ZOMBIE; // En caso de que algo falle, devolver un zombie
    }

    private static class MonsterProbability {
        private EntityType entityType;
        private double probability;
        private int level;

        public MonsterProbability(EntityType entityType, int probability) {
            this(entityType, probability, 1);
        }

        public MonsterProbability(EntityType entityType, double probability, int level) {
            this.entityType = entityType;
            this.probability = probability;
            this.level = level;
        }

        public EntityType getEntityType() {
            return entityType;
        }

        public double getProbability() {
            return probability;
        }

        public int getLevel() {
            return level;
        }
    }

}
