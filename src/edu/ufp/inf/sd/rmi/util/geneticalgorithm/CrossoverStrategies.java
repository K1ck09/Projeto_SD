package edu.ufp.inf.sd.rmi.util.geneticalgorithm;

public enum CrossoverStrategies {
    ONE(1),     // Two parents generate two children that will substitute them in the population.
    TWO(2),     // Two parents generate two children that will substitute two random individuals
                        //   in the population. The most fit individual survives (elitism).
    THREE(3);   // Two parents generate a child that will substitute the worst individual of the population.

    public final Integer strategy;

    CrossoverStrategies(Integer strategy) {
        this.strategy = strategy;
    }
}
