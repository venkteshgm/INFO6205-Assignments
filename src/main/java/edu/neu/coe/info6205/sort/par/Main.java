package edu.neu.coe.info6205.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */
public class Main {

    public static void main(String[] args) {
        processArgs(args);
        System.out.println("Degree of parallelism: " + ForkJoinPool.getCommonPoolParallelism());
        System.out.println("processor count: " + Runtime.getRuntime().availableProcessors());
//        ForkJoinPool fjp = new ForkJoinPool(2);
        Random random = new Random();

        ArrayList<Long> timeList = new ArrayList<>();
        for(int arrLen = 1000000;arrLen<=4000000;arrLen+=1000000) {
            int[] array = new int[arrLen];
            for (int tCount = 2; tCount <= 32; tCount = tCount * 2) {
                ForkJoinPool fjp = new ForkJoinPool(tCount);
                long meanTime = 0;
                long minTime = 10000000;
                long optimalCutoff = 10000000;

                for (int j = 5; j < 200; j = j + 10) {
                    ParSort.cutoff = 10000 * (j);
                    // for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                    long time;
                    long startTime = System.currentTimeMillis();
                    for (int t = 0; t < 10; t++) {
                        for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                        ParSort.sort(array, 0, array.length, fjp);
                    }
                    long endTime = System.currentTimeMillis();
                    time = (endTime - startTime);
                    timeList.add(time);
                    meanTime += time;
                    if (time < minTime) {
                        minTime = time;
                        optimalCutoff = ParSort.cutoff;
                    }


                    //                System.out.println("cutoff：" + (ParSort.cutoff) + "\t\t10times Time:" + time + "ms");
//                    System.out.println(time);

                }
                meanTime /= 20;
                System.out.println("for arrayLen: "+arrLen+" threadCount: " + tCount + " threads, meanTime: " + meanTime + " minTime: " + minTime + " optimal cutoff: " + optimalCutoff);
            }
        }
        try {
            FileOutputStream fis = new FileOutputStream("./src/result.csv");
            OutputStreamWriter isr = new OutputStreamWriter(fis);
            BufferedWriter bw = new BufferedWriter(isr);
            int j = 0;
            for (long i : timeList) {
                String content = (double) 10000 * (j + 1) / 2000000 + "," + (double) i / 10 + "\n";
                j++;
                bw.write(content);
                bw.flush();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processArgs(String[] args) {
        String[] xs = args;
        while (xs.length > 0)
            if (xs[0].startsWith("-")) xs = processArg(xs);
    }

    private static String[] processArg(String[] xs) {
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("N")) setConfig(x, Integer.parseInt(y));
        else
            // TODO sort this out
            if (x.equalsIgnoreCase("P")) //noinspection ResultOfMethodCallIgnored
                ForkJoinPool.getCommonPoolParallelism();
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();


}
