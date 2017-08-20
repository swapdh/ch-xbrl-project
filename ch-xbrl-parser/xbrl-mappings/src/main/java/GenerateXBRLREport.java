

import com.uk.ch.xbrl.Xbrl;

import javax.naming.*;
import javax.naming.Context;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Hashtable;

/**
 * Created by himandhk on 20/08/2017.
 */
public class GenerateXBRLREport {
    public static void main(String args[]){
        System.out.print("hi");
        try {

            File file = new File("/Users/himandhk/tempData/report.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Xbrl.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Xbrl xbrl = (Xbrl) jaxbUnmarshaller.unmarshal(file);
            System.out.println(xbrl.);

        } catch (JAXBException e) {
            e.printStackTrace();
        }


    }
}
