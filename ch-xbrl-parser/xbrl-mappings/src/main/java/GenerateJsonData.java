import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.Callable;

/**
 * Created by himandhk on 10/09/2017.
 */
public class GenerateJsonData implements Callable<GenerateJsonData> {
    private File file = null;
    // private static final String FILENAME = "/Users/himandhk/jsonData/";// Json output folder

    GenerateJsonData(File file) {
        this.file = file;
    }


    public GenerateJsonData call() throws Exception {
        BufferedWriter bw = null;
        FileWriter fw = null;
        HttpURLConnection conn = null;
        BufferedReader rd = null;
        try {
            long startTime = Calendar.getInstance().getTimeInMillis();
            File intputFile = new File(file.getAbsolutePath());
            String outputStr=file.getParent() +"Json"+File.separator +file.getName().split("\\.")[0]+".json";
            File outputFile = new File(outputStr);
            System.out.println(" File Processing " + intputFile.getAbsoluteFile());
            String restAPIstr =
                    "http://localhost:3333/rest/xbrl/view?file=" + file + "&media=json&view=facts";

            URL url = new URL(restAPIstr);
            conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() != 200) {
                throw new IOException(conn.getResponseMessage());
            }

            // Buffer the result into a string
            rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
              //  System.out.println(line);
            }

            rd.close();
            conn.disconnect();
            outputFile.getParentFile().mkdirs();
            fw = new FileWriter(outputFile.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(sb.toString());
            bw.flush();


            long endTime = Calendar.getInstance().getTimeInMillis();
            System.out.println("Done in " + (endTime - startTime));

            return null;
        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {

                if (rd != null)
                    rd.close();
                if (conn != null)
                    conn.disconnect();

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (Exception ex) {

                ex.printStackTrace();

            }

        }
        return null;
    }
}
