import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by himandhk on 10/09/2017.
 */
public class GenerateJsonFromXBRLFiles {


    public static void main(String args[]){

        ExecutorService executorService = Executors.newFixedThreadPool(6);
        Collection<Callable<GenerateJsonData>> tasks = new ArrayList<Callable<GenerateJsonData>>();

        try {
        File dir = new File("/Users/himandhk/sampleXBRL/");// xbrl files folder
        File[] directoryListing = dir.listFiles();

        if (directoryListing != null) {
            for (File child : directoryListing) {

                GenerateJsonData generateJsonData = new GenerateJsonData(child);

                tasks.add(generateJsonData);
            }

                executorService.invokeAll(tasks);


        }
        } catch (Exception e) {

            e.printStackTrace();

        }finally {
            executorService.shutdown();
        }

        }
}
