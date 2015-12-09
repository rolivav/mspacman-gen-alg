package geneticalgorythm;

import pacman.controllers.examples.GeneticPacMan;
import pacman.controllers.examples.RandomGhosts;
import pacman.Executor;
import pacman.controllers.examples.StarterGhosts;

import java.util.ArrayList;     // arrayLists are more versatile than arrays
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


/**
 * Genetic Algorithm sample class <br/>
 * <b>The goal of this GA sample is to maximize the number of capital letters in a String</b> <br/>
 * compile using "javac geneticalgorythm.GeneticAlgorithm.java" <br/>
 * test using "java geneticalgorythm.GeneticAlgorithm" <br/>
 *
 * @author A.Liapis
 */

public class GeneticAlgorithm {
    // --- constants
    static int CHROMOSOME_SIZE=24; //must be multiple of 4
    static int POPULATION_SIZE=300;
    static int MAXIMUM_DISTANCE=150;

    // --- variables:

    /**
     * The population contains an ArrayList of genes (the choice of arrayList over
     * a simple array is due to extra functionalities of the arrayList, such as sorting)
     */
    ArrayList<Gene> mPopulation;

    Gene[] currentBestOffspring;
    int[] currentWorstOffspringPos;
    int generationNumber = 0;
    double bestOffspringScore = 0;
    // --- functions:

    /**
     * Creates the starting population of geneticalgorythm.Gene classes, whose chromosome contents are random
     * @param size: The size of the popultion is passed as an argument from the main class
     */
    public GeneticAlgorithm(int size){
        // initialize the arraylist and each gene's initial weights HERE
        mPopulation = new ArrayList<Gene>();
        Random randomGenerator = new Random();
        for(int i = 0; i < size; i++){
            Gene entry = new Gene();
            entry.randomizeChromosome(randomGenerator);
            mPopulation.add(entry);
        }
    }
    /**
     * For all members of the population, runs a heuristic that evaluates their fitness
     * based on their phenotype. The evaluation of this problem's phenotype is fairly simple,
     * and can be done in a straightforward manner. In other cases, such as agent
     * behavior, the phenotype may need to be used in a full simulation before getting
     * evaluated (e.g based on its performance)
     */
    public void evaluateGeneration(){
        System.out.println("Selecting best genes by their obtained score vs Starter Ghosts...");
        currentBestOffspring = getBestGenesFromPopulation();
    }
    /**
     * With each gene's fitness as a guide, chooses which genes should mate and produce offspring.
     * The offspring are added to the population, replacing the previous generation's Genes either
     * partially or completely. The population size, however, should always remain the same.
     * If you want to use mutation, this function is where any mutation chances are rolled and mutation takes place.
     */
    public void produceNextGeneration(){
        // use one of the offspring techniques suggested in class (also applying any mutations) HERE
        System.out.println("Producing next generation...");
        System.out.println("\nCrossing best candidates...");
        Gene[] children = currentBestOffspring[0].reproduce(currentBestOffspring[1]);
        System.out.println("\nMutating genes...");
        for(Gene gen : mPopulation){
            gen.mutate();
        }
        System.out.println("\nReplacing worst genes with new children...");
        mPopulation.set(currentWorstOffspringPos[0], children[0]);
        mPopulation.set(currentWorstOffspringPos[1], children[1]);

        generationNumber++;
    }

    /**
     * @return the size of the population
     */
    public int size(){ return mPopulation.size(); }
    /**
     * Returns the geneticalgorythm.Gene at position <b>index</b> of the mPopulation arrayList
     * @param index: the position in the population of the geneticalgorythm.Gene we want to retrieve
     * @return the geneticalgorythm.Gene at position <b>index</b> of the mPopulation arrayList
     */
    public Gene getGene(int index){ return mPopulation.get(index); }

    public void printPopulation() { for(Gene gene : mPopulation) System.out.println(Arrays.toString(gene.getPhenotype()));}

    public Gene[] getBestGenesFromPopulation() {
        Gene[] result = new Gene[2];
        Executor exec=new Executor();
        GeneticPacMan geneticPacMan = new GeneticPacMan();
        int[] bestPos = new int [2];
        double[] bestScore =  new double[2];
        currentWorstOffspringPos = new int[2];
        double[] worstScore =  new double[2];
        worstScore[0]  = 10000.0;
        worstScore[1] = 10000.0;
        double currentScore;
        for(int i = 0; i < POPULATION_SIZE; i++) {
            geneticPacMan.initFuzzy(mPopulation.get(i).getDecodedPhenotype());
            currentScore = exec.runExperiment(geneticPacMan,new StarterGhosts(), 1);
            if(bestScore[0] < currentScore){
                bestScore[1] = bestScore[0];
                bestPos[1] = bestPos[0];
                bestScore[0] = currentScore;
                bestPos[0] = i;
            }else if (bestScore[1] < currentScore){
                bestScore[1] = currentScore;
                bestPos[1] = i;
            }

            if(worstScore[0] > currentScore){
                worstScore[1] = worstScore[0];
                worstScore[0] = currentScore;
                currentWorstOffspringPos[1] = currentWorstOffspringPos[0];
                currentWorstOffspringPos[0] = i;
            }else if (worstScore[1] > currentScore){
                worstScore[1] = currentScore;
                currentWorstOffspringPos[1] = i;
            }
        }

        if(bestOffspringScore < bestScore[0]) bestOffspringScore = bestScore[0];

        System.out.println("Best gene was the one at pos " + bestPos[0] + " with an score of " +bestScore[0]);
        mPopulation.get(bestPos[0]).printPhenotype();
        System.out.println("Second best gene was the one at pos " + bestPos[1] + " with an score of " +bestScore[1]);
        mPopulation.get(bestPos[1]).printPhenotype();
        System.out.println("Worst gene was the one at pos " + currentWorstOffspringPos[0] + " with an score of " +worstScore[0]);
        System.out.println("Second worst gene was the one at pos " + currentWorstOffspringPos[1] + " with an score of " +worstScore[1]);
        result[0] = mPopulation.get(bestPos[0]);
        result[1] = mPopulation.get(bestPos[1]);
        return result;
    }

    // Genetic Algorithm maxA testing method
    public static void main( String[] args ){
        // Initializing the population (we chose 500 genes for the population,
        // but you can play with the population size to try different approaches)

        Scanner reader = new Scanner(System.in);

        System.out.println("Creating random population...");
        GeneticAlgorithm population = new GeneticAlgorithm(POPULATION_SIZE);

        while(true) {
            System.out.println("Init 1 to test current population, 2 to keep testing until best gene reaches 6000 score, 3 to exit");
            int input = reader.nextInt();
            if(input == 1) {
                System.out.println("Generation number: " + population.generationNumber);
                System.out.println("Executing experiment on population...\n");
                population.evaluateGeneration();
                population.produceNextGeneration();
            }
            if(input == 2) {
                while(population.bestOffspringScore < 6000) {
                    System.out.println("Generation number: " + population.generationNumber);
                    System.out.println("Executing experiment on population...\n");
                    population.evaluateGeneration();
                    population.produceNextGeneration();
                }
            }
            if (input == 3) break;
        }
    }
}

