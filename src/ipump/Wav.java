package ipump;

import java.io.IOException;
import javax.annotation.Resources;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * This class is intended for starting sound file
 * 
 */
public class Wav {

    private static SourceDataLine auline = null;
    
    /**
     * Method for starting a sound track.
     * A separate thread is made so that the program works normally at the same time as      *the sound is heard
     * Because it's bad to close the Threads, we shut out the sound file itself as needed *instead of the thread.
     * 
     * @param filename Location of the audio track
     */
    public static void play(String filename) {
        stop();
        //  Missing new sound effects if the speed is higher than 8 times
        if (Clock.getSpeed() > 8)
        {   
            return;
        }
        
        // it is necessary that the filename we send is final
        final String finalFilename = filename;
        
        // The new thread is made

        new Thread(
            new Runnable() {
                public void run() {
                    try {
                        playFile(finalFilename);   
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        ).start();
    }
    
    /**
     * Pauses the previous sound to avoid duplication of sound.
     *Pause only if there is a file previously.
     */
    public static void stop()
    {
        if (auline != null)
        {
            auline.stop();
            auline.close();
        }
    }
    
    /**
     * Sound file is started here.
     * 
     * @param filename Sound file name
     */
    private static void playFile(String filename)
    {
        //Load sound file from the resource folder
        AudioInputStream audioInputStream = null;
        try {
                audioInputStream = AudioSystem.getAudioInputStream(Resources.class.getClass().getResourceAsStream("/resources/wav/" + filename));
        } catch (UnsupportedAudioFileException e1) {
                e1.printStackTrace();
                return;
        } catch (IOException e1) {
                e1.printStackTrace();
                return;
        }

        // Set the sound track
        AudioFormat format = audioInputStream.getFormat();
        auline = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        try {
                auline = (SourceDataLine) AudioSystem.getLine(info);
                auline.open(format);
        } catch (LineUnavailableException e) {
                e.printStackTrace();
                return;
        } catch (Exception e) {
                e.printStackTrace();
                return;
        }

        if (auline.isControlSupported(FloatControl.Type.PAN)) {
                FloatControl pan = (FloatControl) auline
                                .getControl(FloatControl.Type.PAN);
        }

        // Launch the sound
        auline.start();
        int nBytesRead = 0;
        byte[] abData = new byte[524288];

        try {
                while (nBytesRead != -1) {
                        nBytesRead = audioInputStream.read(abData, 0, abData.length);
                        if (nBytesRead >= 0)
                                auline.write(abData, 0, nBytesRead);
                }
        } catch (IOException e) {
                e.printStackTrace();
                return;
        } finally {
                auline.drain();
                auline.close();
        }
    }
}
