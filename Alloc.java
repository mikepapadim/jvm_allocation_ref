
import java.io.FileWriter;
import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.ArrayList;


import java.util.List;

public class Alloc {

    private static final int[] ARRAY_SIZE_VALUES = {1, 5, 50, 500, 1000};
    private static final int[] ITERATIONS = {10, 50, 100, 200};

    public static void main(String[] args) {

            // Create a StringBuilder to build the CSV content
            StringBuilder csvContent = new StringBuilder();
            csvContent.append(
                    "Array Size (MB),Number of Iterations,Panama - Average time,Panama - Minimum time,Panama - Maximum time,Panama - Standard deviation,Primitive - Average time,Primitive - Minimum time,Primitive - Maximum time,Primitive - Standard deviation\n");

            for (int arraySize : ARRAY_SIZE_VALUES) {
                for (int numIterations : ITERATIONS) {
                    ArrayList<Long> allocationTimesPanama = allocateAndMeasureTimesPanama(arraySize, numIterations);
                    ArrayList<Long> allocationTimes = allocateAndMeasureTimesPrimitive(arraySize, numIterations);

                    if (!allocationTimes.isEmpty()) {
                        String csvRow = buildCSVRow(arraySize, numIterations, allocationTimesPanama, allocationTimes);
                        csvContent.append(csvRow);
                    }
                }
            }

            // Write the CSV content to a file
            try (FileWriter file = new FileWriter("allocation_log.csv")) {
                file.write(csvContent.toString());
                System.out.println("Log data written to allocation_log.csv");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static String buildCSVRow ( int arraySize, int numIterations, ArrayList<Long > allocationTimesPanama, ArrayList < Long > allocationTimes){
            List<Long> panamaTimer = printTimingResults(allocationTimesPanama);
            long panamaAverage = panamaTimer.get(0);
            long panamaMinimum =panamaTimer.get(1);
            long panamaMaximum = panamaTimer.get(2);
            long panamaStdDev = panamaTimer.get(3);

            List<Long> primitiveTimer = printTimingResults(allocationTimes);
            long primitiveAverage = primitiveTimer.get(0);
            long primitiveMinimum =primitiveTimer.get(1);
            long primitiveMaximum = primitiveTimer.get(2);
            long primitiveStdDev = primitiveTimer.get(3);


            return String.format("%d,%d,%d,%d,%d,%d,%d,%d,%d,%d\n", arraySize, numIterations, panamaAverage, panamaMinimum, panamaMaximum, panamaStdDev, primitiveAverage, primitiveMinimum,
                    primitiveMaximum, primitiveStdDev);
        }

        public static ArrayList<Long> allocateAndMeasureTimesPanama ( int arraySizeMB, int numIterations){
            int arraySizeBytes = arraySizeMB * 1024 * 1024;
            ArrayList<Long> allocationTimes = new ArrayList<>();

            for (int i = 0; i < numIterations; i++) {
                float[] largeArray = null;

                try {
                    long startTime = System.nanoTime();
                    largeArray = new float[arraySizeBytes / 4];
                    long endTime = System.nanoTime();

                    long allocationTime = endTime - startTime;
                    allocationTimes.add(allocationTime);

                    // Ensure that the array is released and eligible for garbage collection
                    largeArray = null;
                } catch (OutOfMemoryError e) {
                    System.err.println("Out of memory: Unable to allocate the array.");
                }
            }
            return allocationTimes;
        }

    public static ArrayList<Long> allocateAndMeasureTimesPrimitive(int arraySizeMB, int numIterations) {
        int arraySizeBytes = arraySizeMB * 1024 * 1024;
        ArrayList<Long> allocationTimes = new ArrayList<>();

        for (int i = 0; i < numIterations; i++) {
            MemorySegment largeArray = null;
            long segmentByteSize = arraySizeBytes;

            try {
                long startTime = System.nanoTime();
                largeArray = Arena.ofAuto().allocate(segmentByteSize, 1);
                long endTime = System.nanoTime();

                long allocationTime = endTime - startTime;
                allocationTimes.add(allocationTime);

                // Ensure that the array is released and eligible for garbage collection
                largeArray=null;
            } catch (OutOfMemoryError e) {
                System.err.println("Out of memory: Unable to allocate the array.");
            }
        }
        return allocationTimes;
    }

    public static List<Long> printTimingResults(ArrayList<Long> allocationTimes) {


        List<Long> timer = new ArrayList<>();
        // Calculate average, minimum, maximum, and standard deviation
        long sum = 0;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;

        for (Long time : allocationTimes) {
            sum += time;
            min = Math.min(min, time);
            max = Math.max(max, time);
        }

        Long average = (long) ((double) sum / allocationTimes.size());

        // Calculate standard deviation
        double variance = 0;
        for (Long time : allocationTimes) {
            double diff = time - average;
            variance += diff * diff;
        }
        Long stdDev = (long) Math.sqrt(variance / allocationTimes.size());

        timer.add(average);
        timer.add(min);
        timer.add(max);
        timer.add(stdDev);

        return timer;

    }
}
