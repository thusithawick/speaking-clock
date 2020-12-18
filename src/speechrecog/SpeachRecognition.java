package speechrecog;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Port;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import java.text.SimpleDateFormat;
import java.util.Date;
import speak.Speeker;

public class SpeachRecognition {

    // Logger
    private Logger logger = Logger.getLogger(getClass().getName());
    public static boolean listenOn = true;

    // Variables
    private String result;

    // Threads
    Thread speechThread;
    Thread resourcesThread;

    // LiveRecognizer
    private LiveSpeechRecognizer recognizer;

    /**
     * Constructor
     */
    public SpeachRecognition() {

        // Loading Message
        logger.log(Level.INFO, "Loading..\n");

        // Configuration
        Configuration configuration = new Configuration();

        // Load model from the jar
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");

        // if you want to use LanguageModelPath disable the 3 lines after which
        // are setting a custom grammar->
        // configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin")
        // Grammar
        configuration.setGrammarPath("resource:/grammars");
        configuration.setGrammarName("grammars");
        configuration.setUseGrammar(true);

        try {
            recognizer = new LiveSpeechRecognizer(configuration);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        // Start recognition process pruning previously cached data.
        recognizer.startRecognition(true);

        // Start the Thread
        startSpeechThread();
        startResourcesThread();
    }

    /**
     * Starting the main Thread of speech recognition
     */
    protected void startSpeechThread() {

        // alive?
        if (speechThread != null && speechThread.isAlive()) {
            return;
        }

        // initialise
        speechThread = new Thread(new Runnable() {

            @Override
            public void run() {
                logger.log(Level.INFO, "You can start to speak...\n");
                try {
                    while (true) {
                        if (listenOn) {
                            /*
                             * This method will return when the end of speech is
                             * reached. Note that the end pointer will determine the end
                             * of speech.
                             */
                            SpeechResult speechResult = recognizer.getResult();
                            if (speechResult != null) {

                                result = speechResult.getHypothesis();
                                System.out.println("You said: [" + result + "]\n");
                                makeDecision(result);
                                // logger.log(Level.INFO, "You said: " + result + "\n")

                            } else {
                                logger.log(Level.INFO, "I can't understand what you said.\n");
                            }
                        }

                    }
                } catch (Exception ex) {
                    logger.log(Level.WARNING, null, ex);
                }

                logger.log(Level.INFO, "SpeechThread has exited...");
            }
        });

        // Start
        speechThread.start();

    }

    /**
     * Starting a Thread that checks if the resources needed to the
     * SpeechRecognition library are available
     */
    protected void startResourcesThread() {

        // alive?
        if (resourcesThread != null && resourcesThread.isAlive()) {
            return;
        }

        resourcesThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    // Detect if the microphone is available
                    while (true) {
                        if (AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
                            // logger.log(Level.INFO, "Microphone is available.\n")
                        } else {
                        // logger.log(Level.INFO, "Microphone is not
                            // available.\n")

                        }

                        // Sleep some period
                        Thread.sleep(350);
                    }

                } catch (InterruptedException ex) {
                    logger.log(Level.WARNING, null, ex);
                    resourcesThread.interrupt();
                }
            }
        });

        // Start
        resourcesThread.start();
    }

    /**
     * Takes a decision based on the given result
     */
    public void makeDecision(String speech) {
        if (listenOn) {
            switch (speech) {
                case "what is the time":
                case "speak time":
                case "current time":
                    Speeker.speek(new SimpleDateFormat("hh:mm a").format(new Date()));
                    break;
                case "what is the date":
                case "speak date":
                case "current date":
                    Speeker.speek(new SimpleDateFormat("YYYY, MMMM, dd").format(new Date()));
                    break;
                case "nice":
                case "thank you":
                case "good":
                    Speeker.speek("You are Welcome");
                    break;
                default:
                    break;
            }

        }
        //MainMenu.openAnonymousApp(MainMenu.jPanel1, App.PurchaseOrders);
    }

    /**
     * Java Main Application Method
     *
     * @param args
     */
    public static void main(String[] args) {

        // // Be sure that the user can't start this application by not giving
        // the
        // // correct entry string
        // if (args.length == 1 && "SPEECH".equalsIgnoreCase(args[0]))
        listenOn = true;
        new SpeachRecognition();
        // else
        // Logger.getLogger(Main.class.getName()).log(Level.WARNING, "Give me
        // the correct entry string..");

    }

}
