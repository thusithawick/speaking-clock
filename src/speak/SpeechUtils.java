package speak;

import java.beans.PropertyVetoException;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineStateError;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;

public class SpeechUtils {

    SynthesizerModeDesc desc;
    Synthesizer synthesizer;
    Voice voice;

    public SpeechUtils() {
        try {
            init("kevin16");
        } catch (EngineException ex) {
            Logger.getLogger(SpeechUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AudioException ex) {
            Logger.getLogger(SpeechUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EngineStateError ex) {
            Logger.getLogger(SpeechUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(SpeechUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void speek(String text){
        try {
            doSpeak(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(String voiceName) throws EngineException, AudioException, EngineStateError,
            PropertyVetoException {
        if (desc == null) {

            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_time_awb.AlanVoiceDirectory");
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

            desc = new SynthesizerModeDesc(Locale.US);
            Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
            synthesizer = Central.createSynthesizer(desc);
            synthesizer.allocate();
            synthesizer.resume();
            SynthesizerModeDesc smd
                    = (SynthesizerModeDesc) synthesizer.getEngineModeDesc();
            Voice[] voices = smd.getVoices();
            Voice voice = null;
            for (int i = 0; i < voices.length; i++) {
                System.out.println(voices[i].getName());
                if (voices[i].getName().equals(voiceName)) {
                    voice = voices[i];
                    voice.setAge(Voice.AGE_OLDER_ADULT);
                    voice.setGender(Voice.GENDER_FEMALE);
                }
            }
            voice.setAge(Voice.AGE_OLDER_ADULT);
            voice.setGender(Voice.GENDER_FEMALE);
            synthesizer.getSynthesizerProperties().setVoice(voice);
        }

    }

    public void terminate() throws EngineException, EngineStateError {
        synthesizer.deallocate();
    }

    public void doSpeak(String speakText) throws EngineException, AudioException, IllegalArgumentException, InterruptedException {
        synthesizer.speakPlainText(speakText, null);
        synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
        synthesizer.cancelAll();
    }

    public static void main(String[] args) throws Exception {
        SpeechUtils su = new SpeechUtils();

        /*
         # This file lists the voice directories avialable to FreeTTs.
         # To get the name of a voice directory from a voice jar file,
         # type "java -jar voicefile.jar".
         #
         # Entries should look like:
         #com.sun.speech.freetts.en.us.cmu_time_awb.AlanVoiceDirectory
         #com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory
         com.sun.speech.freetts.en.us.cmu_time_awb.AlanVoiceDirectory
         com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory
         # Uncomment to allow MBROLA voices:
         #de.dfki.lt.freetts.en.us.MbrolaVoiceDirectory
        */
        
        su.init("kevin16");
        // high quality
        for (int i = 0; i < 2; i++) {
            su.doSpeak("Hi");
        }
        LinkedBlockingDeque<String> q = new LinkedBlockingDeque<>();
        
        for(;true;) {
            if (q.size()>0) {
                
            }
        }
            
    }
}