package shenkar.finalProject.googleDocs.MVVC;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

    /**
     * to make Logger accessible from anywhere in the program
     */
public class MyLogger {

    public static Logger Log = LogManager.getLogger(StartProgram.class.getName());
}
