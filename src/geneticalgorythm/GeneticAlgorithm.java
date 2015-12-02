package geneticalgorythm;

import pacman.controllers.examples.GeneticPacMan;
import pacman.controllers.examples.RandomGhosts;
import pacman.Executor;
import java.util.ArrayList;     // arrayLists are more versatile than arrays
import java.util.Arrays;
import java.util.Random;


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
    static int POPULATION_SIZE=500;
    static int MAXIMUM_DISTANCE=150;

    // --- variables:

    /**
     * The population contains an ArrayList of genes (the choice of arrayList over
     * a simple array is due to extra functionalities of the arrayList, such as sorting)
     */
    ArrayList<Gene> mPopulation;

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
        for(int i = 0; i < mPopulation.size(); i++){
            // evaluation of the fitness function for each gene in the population goes HERE
        }
    }
    /**
     * With each gene's fitness as a guide, chooses which genes should mate and produce offspring.
     * The offspring are added to the population, replacing the previous generation's Genes either
     * partially or completely. The population size, however, should always remain the same.
     * If you want to use mutation, this function is where any mutation chances are rolled and mutation takes place.
     */
    public void produceNextGeneration(){
        // use one of the offspring techniques suggested in class (also applying any mutations) HERE
    	
    	
    }

    //WE NEED ANOTHER METHOD which decides who are reinserted

    // accessors
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

    // Genetic Algorithm maxA testing method
    public static void main( String[] args ){
        // Initializing the population (we chose 500 genes for the population,
        // but you can play with the population size to try different approaches)
        GeneticAlgorithm population = new GeneticAlgorithm(POPULATION_SIZE);
        population.getGene(0).printPhenotype(); //this is ugly. take out of gene in next iteration.
//        int[][] phenotype = population.getGene(0).getPhenotype();
//        for(int[] function : phenotype){
//            System.out.print(Arrays.toString(function));
//        }
//        System.out.println();
//        double[][] decodedPhenotype = population.getGene(0).getDecodedPhenotype();
//        for(double[] function : decodedPhenotype){
//            System.out.print(Arrays.toString(function));
//        }

        Executor exec=new Executor();

        //run multiple games in batch mode - good for testing.
        int numTrials=1;

        GeneticPacMan geneticPacMan = new GeneticPacMan();
        geneticPacMan.initFuzzy(population.getGene(0).getDecodedPhenotype());

        exec.runExperiment(geneticPacMan,new RandomGhosts(),numTrials);


//        population.printPopulation();

//        int generationCount = 0;
//        // For the sake of this sample, evolution goes on forever.
//        // If you wish the evolution to halt (for instance, after a number of
//        //   generations is reached or the maximum fitness has been achieved),
//        //   this is the place to make any such checks
////        while(true){
//            // --- evaluate current generation:
//            population.evaluateGeneration();
//            // --- print results here:
//            // we choose to print the average fitness,
//            // as well as the maximum and minimum fitness
//            // as part of our progress monitoring
//            float avgFitness=0.f;
//            float minFitness=Float.POSITIVE_INFINITY;
//            float maxFitness=Float.NEGATIVE_INFINITY;
//            String bestIndividual="";
//		String worstIndividual="";
//            for(int i = 0; i < population.size(); i++){
//                float currFitness = population.getGene(i).getFitness();
//                avgFitness += currFitness;
//                if(currFitness < minFitness){
//                    minFitness = currFitness;
//                    worstIndividual = population.getGene(i).getPhenotype();
//                }
//                if(currFitness > maxFitness){
//                    maxFitness = currFitness;
//                    bestIndividual = population.getGene(i).getPhenotype();
//                }
//            }
//            if(population.size()>0){ avgFitness = avgFitness/population.size(); }
//            String output = "Generation: " + generationCount;
//            output += "\t AvgFitness: " + avgFitness;
//            output += "\t MinFitness: " + minFitness + " (" + worstIndividual +")";
//            output += "\t MaxFitness: " + maxFitness + " (" + bestIndividual +")";
//            System.out.println(output);
//            // produce next generation:
//            population.produceNextGeneration();
//            generationCount++;
//        }
    }
};

