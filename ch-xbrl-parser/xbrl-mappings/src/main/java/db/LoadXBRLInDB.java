package db;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by himandhk on 20/09/2017.
 */
public class LoadXBRLInDB {

    public static void main(String args[]){
        long startTime = System.currentTimeMillis();
        List<Callable<GenerateDataInDB>> tasks = new ArrayList<Callable<GenerateDataInDB>>();
        ExecutorService executorService = Executors.newFixedThreadPool(39);
        try {
            String rootDir=args[0]; // xbrl files folder
            //String rootDir="/xbrl/test/";
            File dir = new File(rootDir);
            findFiles(dir, tasks);
            Stream<List<Callable<GenerateDataInDB>>> batches = batches(tasks, 10);
            batches.forEach(batch -> {
                List<Future<GenerateDataInDB>> futures = null;
                try {
                    futures = executorService.invokeAll(batch);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                futures.forEach(e -> {
                    try {
                        GenerateDataInDB data = e.get();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } catch (ExecutionException e1) {
                        e1.printStackTrace();
                    }
                });

                System.out.println("Batch finished of size "+batch.size());
            });

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            executorService.shutdown();
        }
        System.out.println("Total time taken " + (System.currentTimeMillis() - startTime)/1000+ "secs");
    }



    private static File findFiles(File parentDir,List<Callable<GenerateDataInDB>> tasks) throws Exception {

        File file = null;
        File[] directoryListing = parentDir.listFiles();

        for (File child : directoryListing) {
            if (child.isDirectory()) {
                file= findFiles(child,tasks);
            } else {
                GenerateDataInDB generateData = new GenerateDataInDB(child,parentDir.getAbsolutePath());
                tasks.add(generateData);
            }

        }
        return file;

    }

    public static Stream<List<Callable<GenerateDataInDB>>> batches(List<Callable<GenerateDataInDB>> source, int length) {
        if (length <= 0)
            throw new IllegalArgumentException("length = " + length);
        int size = source.size();
        if (size <= 0)
            return Stream.empty();
        int fullChunks = (size - 1) / length;
        return IntStream.range(0, fullChunks + 1).mapToObj(
                n -> source.subList(n * length, n == fullChunks ? size : (n + 1) * length));
    }
}
