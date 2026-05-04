/**
 * Program to test the sorting algorithms.
 */

package se.kth;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        testSortAlgorithms(Integer.parseInt(args[0]),Long.parseLong(args[1]), Long.parseLong(args[2]));
        testSortAlgorithms(10000000, 4000000000L, 30);
    }

    /**
     * Test the merge and quick sort with specific parameters.
     * @param integerCount how many integers the data will contain.
     * @param upperLimit the limit of the integer size.
     * @param seed what seed the random will be generated upon.
     */
    static void testSortAlgorithms(int integerCount, long upperLimit, long seed) {
        // Preparation
        int tests = 5;
        int cutoffCount = 31;
        long startTime;
        long executionTime;
        long averageTimeMerge = 0;
        long averageTimeQuick = 0;
        long[] mergeTime = new long[cutoffCount];
        long[] quickTime = new long[cutoffCount];

        // Tests
        System.out.println("-----Average over " + tests + " tests-----");
        long[] randomArray = generateRandomInputs(integerCount, upperLimit, seed);
        for(int cutoff = 0; cutoff < cutoffCount; cutoff++) {
            long[] copyArray = cloneArray(randomArray);

            for(int i = 0; i < tests; i++) {
                // Merge
                copyArray = cloneArray(randomArray);
                startTime = System.nanoTime();
                MergeSort.sort(copyArray, cutoff);
                executionTime = System.nanoTime() - startTime;
                averageTimeMerge += executionTime;

                // Quick
                copyArray = cloneArray(randomArray);
                startTime = System.nanoTime();
                QuickSort.sort(copyArray, cutoff);
                executionTime = System.nanoTime() - startTime;
                averageTimeQuick += executionTime;
            }

            // Output the results
            averageTimeMerge = averageTimeMerge / tests;
            averageTimeQuick = averageTimeQuick / tests;
            mergeTime[cutoff] = averageTimeMerge / 1000000;
            quickTime[cutoff] = averageTimeQuick / 1000000;
            System.out.println("*Number of cutoff: " + cutoff + "*");
            System.out.println("The average time for Mergesort is: " + (averageTimeMerge/1000000) + " ms");
            System.out.println("The average time for Quicksort is: " + (averageTimeQuick/1000000) + " ms");
        }

        // Data output for spreed sheet sake
        System.out.println("---Merge Sort graph data---");
        printArray(mergeTime);
        System.out.println("---Quick Sort graph data---");
        printArray(quickTime);
        System.out.println("");
    }

    /**
     * Generate random array with specific count of integers and the size of them.
     * @param integerCount how many integers the data will contain.
     * @param upperLimit the limit of the integer size.
     * @param seed what seed the random will be generated upon.
     * @return the random generated array.
     */
    static long[] generateRandomInputs(int integerCount, long upperLimit, long seed) {
        Random rand = new Random(seed);
        long[] generatedArray = new long[integerCount];
        for(int i = 0; i < integerCount; i++) {
            generatedArray[i] = rand.nextInt((int)upperLimit);
        }
        return generatedArray;
    }

    /**
     * Make a copy of the passed array.
     * @param originalArray the array that wanted to be copied.
     * @return the copied array of the original.
     */
    static long[] cloneArray(long[] originalArray) {
        long[] copyArray = new long[originalArray.length];
        for(int i = 0; i < originalArray.length; i++) {
            copyArray[i] = originalArray[i];
        }
        return copyArray;
    }

    /**
     * Show the elements of the array in the console.
     * @param array the array that wanted to be printed.
     */
    static void printArray(long[] array) {
        for(int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }
}
