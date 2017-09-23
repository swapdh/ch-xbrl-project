package db;

import java.io.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * Created by himandhk on 10/09/2017.
 */
public class GenerateDataInDB implements Callable<GenerateDataInDB> {
    private File file = null;
    // private static final String FILENAME = "/Users/himandhk/jsonData/";// Json output folder

    GenerateDataInDB(File file) {
        this.file = file;
    }

    public GenerateDataInDB call() throws Exception {
        Process process = null;

        try {
            long startTime = Calendar.getInstance().getTimeInMillis();
            File intputFile = new File(file.getAbsolutePath());
            System.out.println(" File Processing " + intputFile.getAbsoluteFile());

            process= new ProcessBuilder("/Applications/Arelle.app/Contents/MacOS/arelleCmdLine","-f",intputFile.getAbsoluteFile().toString(),
                    "--noCertificateCheck","--plugins","xbrlDB","--store-to-XBRL-DB","localhost,5432,postgres,12345,arelle_db,1000,pgSemantic").start();

            InputStream is = process.getInputStream();
            handleStream(process, startTime, is);

            return null;
        } catch (Exception e) {
            if(process != null){
                process.destroy();
            }
            e.printStackTrace();

        }
        return null;
    }

    private void handleStream(Process process, long startTime, InputStream is) throws IOException {

        try (InputStreamReader isr = new InputStreamReader(is);BufferedReader br = new BufferedReader(isr)){
            String line;

//            System.out.printf("Output of running %s is:", Arrays.toString(args));

            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println(process.isAlive());
            long endTime = Calendar.getInstance().getTimeInMillis();
            System.out.println("Done in " + (endTime - startTime) / 1000 + "secs");
        }
    }


}
