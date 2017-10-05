package db;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * Created by himandhk on 10/09/2017.
 */
public class GenerateDataInDB implements Callable<GenerateDataInDB> {
    private File file = null;
    private String parentDir= null;
    // private static final String FILENAME = "/Users/himandhk/jsonData/";// Json output folder

    GenerateDataInDB(File file,String parentDir) {
        this.file = file;
        this.parentDir=parentDir;
    }

    public GenerateDataInDB call() throws Exception {
        Process process = null;

        try {
            long startTime = Calendar.getInstance().getTimeInMillis();
            File intputFile = new File(file.getAbsolutePath());
            System.out.println(" File Processing " + intputFile.getAbsoluteFile());

            process= new ProcessBuilder("/Applications/Arelle.app/Contents/MacOS/arelleCmdLine","-f",intputFile.getAbsoluteFile().toString(),
                    "--noCertificateCheck","--plugins","xbrlDB","--store-to-XBRL-DB","localhost,5432,postgres,bah4400,arelle,1000,pgSemantic","--abortOnMajorError",
                    "--logLevel=info").start();

            InputStream is = process.getInputStream();
            handleStream(process, startTime, is,intputFile.getAbsoluteFile().toString());

            return null;
        } catch (Exception e) {
            if(process != null){
                process.destroy();
            }
            e.printStackTrace();

        }
        return null;
    }

    private void handleStream(Process process, long startTime, InputStream is,String filename) throws IOException {

        try (InputStreamReader isr = new InputStreamReader(is);BufferedReader br = new BufferedReader(isr)){
            String line;
            String logStr="";
//            System.out.printf("Output of running %s is:", Arrays.toString(args));

            while ((line = br.readLine()) != null) {
                if (line.contains("Loading terminated")) {
                    if(!Files.isDirectory(Paths.get(parentDir + "/../failed/"))) {
                        Files.createDirectory(Paths.get(parentDir + "/../failed/"));
                    }
                    Files.move(Paths.get(filename), Paths.get(parentDir+ "/../failed/" + file.getName()));
                    System.out.println(filename);
                    logStr="Failed "+filename+"\n";
                }
                System.out.println(line);
                if (Files.notExists(Paths.get(parentDir+ "/../log.txt"))) {
                    Files.createFile(Paths.get(parentDir+ "/../log.txt"));
                }

                logStr=logStr+line+"\n";
                Files.write(Paths.get(parentDir+ "/../log.txt"), (logStr).getBytes(), StandardOpenOption.APPEND);
                logStr="";
            }
            System.out.println(process.isAlive());
            long endTime = Calendar.getInstance().getTimeInMillis();
            System.out.println("Done in " + (endTime - startTime) / 1000 + "secs");
        }
    }


}
