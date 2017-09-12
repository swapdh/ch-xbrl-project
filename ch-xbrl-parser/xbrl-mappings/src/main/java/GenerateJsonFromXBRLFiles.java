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

    public static void main(String args[]) {

        Collection<Callable<GenerateJsonData>> tasks = new ArrayList<Callable<GenerateJsonData>>();
        ExecutorService executorService = Executors.newFixedThreadPool(6);

        try {
            String rootDir=args[0]; // xbrl files folder
            File dir = new File(rootDir);
            findFiles(dir, tasks);
            executorService.invokeAll(tasks);
        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            executorService.shutdown();
        }

    }

    @SuppressWarnings("unchecked")
    private static File findFiles(File parentDir,Collection<Callable<GenerateJsonData>> tasks) throws Exception {

        File file = null;
        File[] directoryListing = parentDir.listFiles();

        for (File child : directoryListing) {
            if (child.isDirectory()) {
                file= findFiles(child,tasks);
            } else {
                GenerateJsonData generateJsonData = new GenerateJsonData(child);
                tasks.add(generateJsonData);
            }

        }
        return file;

    }
}
