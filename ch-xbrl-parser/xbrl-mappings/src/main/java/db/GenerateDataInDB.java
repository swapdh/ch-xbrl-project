package db;

import java.io.*;
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
        Process process;

        try {
            long startTime = Calendar.getInstance().getTimeInMillis();
            File intputFile = new File(file.getAbsolutePath());
            System.out.println(" File Processing " + intputFile.getAbsoluteFile());

            process= new ProcessBuilder("/Applications/Arelle.app/Contents/MacOS/arelleCmdLine","-f",intputFile.getAbsoluteFile().toString(),
                    "--plugins","xbrlDB","--store-to-XBRL-DB","localhost,5432,postgres,12345,test_db,90,postgres").start();

            long endTime = Calendar.getInstance().getTimeInMillis();
            System.out.println("Done in " + (endTime - startTime));

            return null;
        } catch (Exception e) {

            e.printStackTrace();

        }
        return null;
    }




}
