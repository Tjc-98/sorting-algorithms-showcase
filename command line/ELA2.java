import java.util.Random;

class Main {
    public static void main(String[] args) {
        testSortAlgorithms(Integer.parseInt(args[0]),Long.parseLong(args[1]), Long.parseLong(args[2]));
        // testSortAlgorithms(10000000, 4000000000L, 30);
    }

    static void testSortAlgorithms(int integerCount, long upperLimit, long seed) {
        int tests = 5;
        int cutoffCount = 31;
        long startTime;
        long executionTime;
        long averageTimeMerge = 0;
        long averageTimeQuick = 0;
        long[] mergeTime = new long[cutoffCount];
        long[] quickTime = new long[cutoffCount];

        System.out.println("-----Average over " + tests + " tests-----");
        long[] randomArray = generateRandomInputs(integerCount, upperLimit, seed);
        for(int cutoff = 0; cutoff < cutoffCount; cutoff++) {
            long[] copyArray = cloneArray(randomArray);

            for(int i = 0; i < tests; i++) {
                copyArray = cloneArray(randomArray);
                startTime = System.nanoTime();
                MergeSort.sort(copyArray, cutoff);
                executionTime = System.nanoTime() - startTime;
                averageTimeMerge += executionTime;

                copyArray = cloneArray(randomArray);
                startTime = System.nanoTime();
                QuickSort.sort(copyArray, cutoff);
                executionTime = System.nanoTime() - startTime;
                averageTimeQuick += executionTime;
            }
            averageTimeMerge = averageTimeMerge / tests;
            averageTimeQuick = averageTimeQuick / tests;
            mergeTime[cutoff] = averageTimeMerge / 1000000;
            quickTime[cutoff] = averageTimeQuick / 1000000;
            System.out.println("*Number of cutoff: " + cutoff + "*");
            System.out.println("The average time for Mergesort is: " + (averageTimeMerge/1000000) + " ms");
            System.out.println("The average time for Quicksort is: " + (averageTimeQuick/1000000) + " ms");
        }

        System.out.println("---Merge Sort graph data---");
        printArray(mergeTime);
        System.out.println("---Quick Sort graph data---");
        printArray(quickTime);
        System.out.println("");
    }

    static long[] generateRandomInputs(int integerCount, long upperLimit, long seed) {
        Random rand = new Random(seed);
        long[] generatedArray = new long[integerCount];
        for(int i = 0; i < integerCount; i++) {
            generatedArray[i] = rand.nextInt();
        }
        return generatedArray;
    }

    static long[] cloneArray(long[] originalArray) {
        long[] copyArray = new long[originalArray.length];
        for(int i = 0; i < originalArray.length; i++) {
            copyArray[i] = originalArray[i];
        }
        return copyArray;
    }

    static void printArray(long[] array) {
        for(int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }
}


class MergeSort {
    private static int CUTOFF = 0;  // cutoff to insertion sort

    // This class should not be instantiated.
    private MergeSort() {}

    private static void merge(long[] src, long[] dst, int lo, int mid, int hi) {

        // precondition: src[lo .. mid] and src[mid+1 .. hi] are sorted subarrays
        assert isSorted(src, lo, mid);
        assert isSorted(src, mid + 1, hi);

        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) dst[k] = src[j++];
            else if (j > hi) dst[k] = src[i++];
            else if (less(src[j], src[i])) dst[k] = src[j++];   // to ensure stability
            else dst[k] = src[i++];
        }

        // postcondition: dst[lo .. hi] is sorted subarray
        assert isSorted(dst, lo, hi);
    }

    private static void sort(long[] src, long[] dst, int lo, int hi) {
        // if (hi <= lo) return;
        if (hi <= lo + CUTOFF) {
            insertionSort(dst, lo, hi);
            return;
        }
        int mid = lo + (hi - lo) / 2;
        sort(dst, src, lo, mid);
        sort(dst, src, mid + 1, hi);

        // if (!less(src[mid+1], src[mid])) {
        //    for (int i = lo; i <= hi; i++) dst[i] = src[i];
        //    return;
        // }

        // using System.arraycopy() is a bit faster than the above loop
        if (!less(src[mid + 1], src[mid])) {
            System.arraycopy(src, lo, dst, lo, hi - lo + 1);
            return;
        }

        merge(src, dst, lo, mid, hi);
    }

    /**
     * Rearranges the array in ascending order, using the natural order.
     *
     * @param a the array to be sorted
     */
    public static void sort(long[] a, int cutOffCount) {
        long[] aux = a.clone();
        CUTOFF = cutOffCount;
        sort(aux, a, 0, a.length - 1);
        assert isSorted(a);
    }

    // sort from a[lo] to a[hi] using insertion sort
    private static void insertionSort(long[] a, int lo, int hi) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j - 1]); j--)
                exch(a, j, j - 1);
    }


    /*******************************************************************
     *  Utility methods.
     *******************************************************************/

    // exchange a[i] and a[j]
    private static void exch(long[] a, int i, int j) {
        long swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    // is a[i] < a[j]?
    private static boolean less(long a, long b) {
        return a < b;
    }


    /***************************************************************************
     *  Check if array is sorted - useful for debugging.
     ***************************************************************************/
    private static boolean isSorted(long[] a) {
        return isSorted(a, 0, a.length - 1);
    }

    private static boolean isSorted(long[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++)
            if (less(a[i], a[i - 1])) return false;
        return true;
    }

    // print array to standard output
    private static void show(Object[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }
}

class QuickSort {

    // cutoff to insertion sort, must be >= 1
    private static int CUTOFF = 0;

    // This class should not be instantiated.
    private QuickSort() { }

    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    public static void sort(long[] a, int cutOffCount) {
        // StdRandom.shuffle(a);
        CUTOFF = cutOffCount;
        sort(a, 0, a.length - 1);
        assert isSorted(a);
    }

    // quicksort the subarray from a[lo] to a[hi]
    private static void sort(long[] a, int lo, int hi) {
        if (hi <= lo) return;

        // cutoff to insertion sort (Insertion.sort() uses half-open intervals)
        int n = hi - lo + 1;
        if (n <= CUTOFF) {
            Insertion.sort(a, lo, hi + 1);
            return;
        }

        int j = partition(a, lo, hi);
        sort(a, lo, j-1);
        sort(a, j+1, hi);
    }

    // partition the subarray a[lo..hi] so that a[lo..j-1] <= a[j] <= a[j+1..hi]
    // and return the index j.
    private static int partition(long[] a, int lo, int hi) {
        int n = hi - lo + 1;
        int m = median3(a, lo, lo + n/2, hi);
        exch(a, m, lo);

        int i = lo;
        int j = hi + 1;
        long v = a[lo];

        // a[lo] is unique largest element
        while (less(a[++i], v)) {
            if (i == hi) { exch(a, lo, hi); return hi; }
        }

        // a[lo] is unique smallest element
        while (less(v, a[--j])) {
            if (j == lo + 1) return lo;
        }

        // the main loop
        while (i < j) {
            exch(a, i, j);
            while (less(a[++i], v)) ;
            while (less(v, a[--j])) ;
        }

        // put partitioning item v at a[j]
        exch(a, lo, j);

        // now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
        return j;
    }

    // return the index of the median element among a[i], a[j], and a[k]
    private static int median3(long[] a, int i, int j, int k) {
        return (less(a[i], a[j]) ?
                (less(a[j], a[k]) ? j : less(a[i], a[k]) ? k : i) :
                (less(a[k], a[j]) ? j : less(a[k], a[i]) ? k : i));
    }

    /***************************************************************************
     *  Helper sorting functions.
     ***************************************************************************/

    // is v < w ?
    private static boolean less(long v, long w) {
        return v < w;
    }

    // exchange a[i] and a[j]
    private static void exch(long[] a, int i, int j) {
        long swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }


    /***************************************************************************
     *  Check if array is sorted - useful for debugging.
     ***************************************************************************/
    private static boolean isSorted(long[] a) {
        for (int i = 1; i < a.length; i++)
            if (less(a[i], a[i-1])) return false;
        return true;
    }

    // print array to standard output
    private static void show(long[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }
}

class Insertion {

    // This class should not be instantiated.
    private Insertion() {}

    /**
     * Rearranges the subarray a[lo..hi) in ascending order, using the natural order.
     *
     * @param a  the array to be sorted
     * @param lo left endpoint (inclusive)
     * @param hi right endpoint (exclusive)
     */
    public static void sort(long[] a, int lo, int hi) {
        for (int i = lo + 1; i < hi; i++) {
            for (int j = i; j > lo && less(a[j], a[j - 1]); j--) {
                exch(a, j, j - 1);
            }
        }
        assert isSorted(a, lo, hi);
    }

    /***************************************************************************
     *  Helper sorting functions.
     ***************************************************************************/

    // is v < w ?
    private static boolean less(long v, long w) {
        return v < w;
    }

    // exchange a[i] and a[j]  (for indirect sort)
    private static void exch(long[] a, int i, int j) {
        long swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    /***************************************************************************
     *  Check if array is sorted - useful for debugging.
     ***************************************************************************/
    private static boolean isSorted(long[] a) {
        return isSorted(a, 0, a.length);
    }

    // is the array a[lo..hi) sorted
    private static boolean isSorted(long[] a, int lo, int hi) {
        for (int i = lo + 1; i < hi; i++)
            if (less(a[i], a[i - 1])) return false;
        return true;
    }

    // print array to standard output
    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }
}