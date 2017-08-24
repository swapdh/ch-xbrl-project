/**
 * Created by himandhk on 24/08/2017.
 */


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadXBRLFile {

    private static final String FILENAME = "/Users/himandhk/JsonData/";


    public static void main(String[] args) {

        BufferedWriter bw = null;
        FileWriter fw = null;
        HttpURLConnection conn = null;
        BufferedReader rd = null;
        try {

            File dir = new File("/Users/himandhk/Accounts_Month_Aug2016/");
            File[] directoryListing = dir.listFiles();
            int i = 1;
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    File file = new File(FILENAME + "report" + i + ".txt");
                    String restAPIstr =
                            "http://localhost:3333/rest/xbrl/view?file=" + child + "&media=json&view=facts";

                    URL url = new URL(restAPIstr);
                    conn = (HttpURLConnection) url.openConnection();
                    if (conn.getResponseCode() != 200) {
                        throw new IOException(conn.getResponseMessage());
                    }

                    // Buffer the result into a string
                    rd = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String line = rd.readLine();


                    fw = new FileWriter(file.getAbsoluteFile());
                    bw = new BufferedWriter(fw);
                    bw.write(line);

                    System.out.println("Done");
                    i++;
                }
            }

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

    }

}
