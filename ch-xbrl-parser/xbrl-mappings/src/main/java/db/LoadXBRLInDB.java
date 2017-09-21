package db;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by himandhk on 20/09/2017.
 */
public class LoadXBRLInDB {

    public static void main(String args[]){
        Collection<Callable<GenerateDataInDB>> tasks = new ArrayList<Callable<GenerateDataInDB>>();
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        try {
            String rootDir=args[0]; // xbrl files folder
           // String rootDir="/Users/himandhk/sampleXBRL/";
            File dir = new File(rootDir);
            findFiles(dir, tasks);
            executorService.invokeAll(tasks);
        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            executorService.shutdown();
        }

    }



    private static File findFiles(File parentDir,Collection<Callable<GenerateDataInDB>> tasks) throws Exception {

        File file = null;
        File[] directoryListing = parentDir.listFiles();

        for (File child : directoryListing) {
            if (child.isDirectory()) {
                file= findFiles(child,tasks);
            } else {
                GenerateDataInDB generateData = new GenerateDataInDB(child);
                tasks.add(generateData);
            }

        }
        return file;

    }
}
