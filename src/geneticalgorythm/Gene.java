package geneticalgorythm;

import java.util.Arrays;
import java.util.Random;

public class Gene {
    // --- variables:

    /**
     * Fitness evaluates to how "close" the current gene is to the
     * optimal solution (i.e. contains only 1s in its chromosome)
     * A gene with higher fitness value from another signifies that
     * it has more 1s in its chromosome, and is thus a better solution
     * While it is common that fitness is a floating point between 0..1
     * this is not necessary: the only constraint is that a better solution
     * must have a strictly higher fitness than a worse solution
     */
    protected float mFitness;
    /**
     * The chromosome contains only integers 0 or 1 (we choose to avoid
     * using a boolean type to make computations easier)
     */
    protected int mChromosome[];

    // --- functions:
    /**
     * Allocates memory for the mChromosome array and initializes any other data, such as fitness
     * We chose to use a constant variable as the chromosome size, but it can also be
     * passed as a variable in the constructor
     */
    Gene() {
        // allocating memory for the chromosome array
        mChromosome = new int[GeneticAlgorithm.CHROMOSOME_SIZE];
        // initializing fitness
        mFitness = 0.f;
    }

    /**
     * Randomizes the numbers on the mChromosome array to values between 0 and GeneticAlgorithm.MAXIMUM_DISTANCE/4
     */
    public void randomizeChromosome(Random randomGen){
        // code for randomization of initial weights goes HERE
        int limit = (int)Math.floor(GeneticAlgorithm.MAXIMUM_DISTANCE/4);
        int counter;
        for(int j = 0; j < GeneticAlgorithm.CHROMOSOME_SIZE/4; j++) {
            counter = 0;
            for (int i = 4*j; i < 4 +(4*j); i++) {
                counter += randomGen.nextInt(limit);
                mChromosome[i] = counter;
            }
        }
    }

    /**
     * Creates a number of offspring by combining (using crossover) the current
     * geneticalgorythm.Gene's chromosome with another geneticalgorythm.Gene's chromosome.
     * Usually two parents will produce an equal amount of offpsring, although
     * in other reproduction strategies the number of offspring produced depends
     * on the fitness of the parents.
     * @param other: the other parent we want to create offpsring from
     * @return Array of geneticalgorythm.Gene offspring (default length of array is 2).
     * These offspring will need to be added to the next generation.
     */
    public Gene[] reproduce(Gene other){
        Gene[] result = new Gene[2];
        // initilization of offspring chromosome goes HERE
        return result;
    }

    /**
     * Mutates a gene using inversion, random mutation or other methods.
     * This function is called after the mutation chance is rolled.
     * Mutation can occur (depending on the designer's wishes) to a parent
     * before reproduction takes place, an offspring at the time it is created,
     * or (more often) on a gene which will not produce any offspring afterwards.
     */
    public void mutate(){
    }
    /**
     * Sets the fitness, after it is evaluated in the geneticalgorythm.GeneticAlgorithm class.
     * @param value: the fitness value to be set
     */
    public void setFitness(float value) { mFitness = value; }
    /**
     * @return the gene's fitness value
     */
    public float getFitness() { return mFitness; }
    /**
     * Returns the element at position <b>index</b> of the mChromosome array
     * @param index: the position on the array of the element we want to access
     * @return the value of the element we want to access (0 or 1)
     */
    public int getChromosomeElement(int index){ return mChromosome[index]; }

    /**
     * Sets a <b>value</b> to the element at position <b>index</b> of the mChromosome array
     * @param index: the position on the array of the element we want to access
     * @param value: the value we want to set at position <b>index</b> of the mChromosome array (0 or 1)
     */
    public void setChromosomeElement(int index, int value){ mChromosome[index]=value; }
    /**
     * Returns the size of the chromosome (as provided in the geneticalgorythm.Gene constructor)
     * @return the size of the mChromosome array
     */
    public int getChromosomeSize() { return mChromosome.length; }
    /**
     * Corresponds the chromosome encoding to the phenotype, which is a representation
     * that can be read, tested and evaluated by the main program.
     * @return a String with a length equal to the chromosome size, composed of A's
     * at the positions where the chromosome is 1 and a's at the posiitons
     * where the chromosme is 0
     */
    public int[][] getPhenotype() {
        int[][] result = new int[GeneticAlgorithm.CHROMOSOME_SIZE/4][4];

        for(int i = 0; i < GeneticAlgorithm.CHROMOSOME_SIZE/4; i ++) {
            result[i] = Arrays.copyOfRange(mChromosome, i*4, i*4 + 4);
        }
        return result;
    }

    public double[][] getDecodedPhenotype() {

        int[][] phenotype = getPhenotype();
        int limit = 4*(int)Math.floor(GeneticAlgorithm.MAXIMUM_DISTANCE/4);
        double [][] result = new double[GeneticAlgorithm.CHROMOSOME_SIZE/4][4];

        for(int j = 0; j < GeneticAlgorithm.CHROMOSOME_SIZE/4; j++) {
            for(int i = 0; i < 4; i++) {
                result[j][i] = (double)phenotype[j][i]/limit;
            }
        }
        return result;
    }

    public int[] getChromosome() {
        return mChromosome;
    }

    public void printPhenotype() {
        System.out.println ("If CLOSEST_GHOST is NEAR, DANGER is HIGH");
        System.out.println ("If CLOSEST_GHOST is MEDIUM, DANGER is MODERATE");
        System.out.println ("If CLOSEST_GHOST is FAR, DANGER is LOW\n");

        System.out.println ("Función de pertenencia CLOSEST_GHOST Near =" + Arrays.toString(Arrays.copyOfRange(mChromosome,0,4)));
        System.out.println ("Función de pertenencia CLOSEST_GHOST Medium =" + Arrays.toString(Arrays.copyOfRange(mChromosome,0,4)));
        System.out.println ("Función de pertenencia CLOSEST_GHOST Far=" + Arrays.toString(Arrays.copyOfRange(mChromosome,8,12)) + "\n");

        System.out.println ("Función de pertenencia Danger Low =" + Arrays.toString(Arrays.copyOfRange(mChromosome,12,16)));
        System.out.println ("Función de pertenencia Danger Moderate =" + Arrays.toString(Arrays.copyOfRange(mChromosome,16,20)));
        System.out.println ("Función de pertenencia Danger High =" + Arrays.toString(Arrays.copyOfRange(mChromosome,20,24)) + "\n");
    }
}
