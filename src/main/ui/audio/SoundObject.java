package ui.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;
import java.io.IOException;

// represents a sound object which selects and plays different audio files
public class SoundObject {
    private boolean firstTime;
    private String filePath;

    private Clip clip;
    private AudioInputStream audioInputStream;

    // EFFECTS: constructs a new sound object with default file path; throws exception if audio file type is
    //          supported, if filePath is invalid, or if line is unable to be opened
    public SoundObject() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        filePath = "./data/audio/Moist_Crash.aiff";
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        firstTime = true;
    }

    // EFFECTS: constructs a new sound object with specified file path; throws exception if audio file type is
    //          supported, if filePath is invalid, or if line is unable to be opened
    public SoundObject(String fileName) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        filePath = "./data/audio/" + fileName;
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        firstTime = true;
    }

    // MODIFIES: this
    // EFFECTS: finds and sets audio file to that specified by filename; prints error message to console if any
    //          exceptions are caught
    public void play(String fileName) {
        try {
            filePath = "./data/audio/" + fileName;
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            firstTime = false;
        } catch (IOException e) {
            System.out.println("IO exception has occurred in sound object");
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Caught UnsupportedAudioFileException in sound object");
        } catch (LineUnavailableException e) {
            System.out.println("Caught LineUnavailableException in sound object");
        }
    }

    // MODIFIES: this
    // EFFECTS: plays currently selected audio clip--restarts clip if it has already been played;
    //          prints error message to console if any exceptions are caught
    public void play() {
        if (firstTime) {
            clip.start();
            firstTime = false;
        } else {
            try {
                restart();
            } catch (IOException e) {
                System.out.println("IO exception has occurred");
            } catch (UnsupportedAudioFileException e) {
                System.out.println("Caught UnsupportedAudioFileException");
            } catch (LineUnavailableException e) {
                System.out.println("Caught LineUnavailableException");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: resets and plays currently selected audio clip; throws exception if there is an IO error, a line
    //          is unavailable, or audio file type is unsupported
    public void restart() throws IOException, LineUnavailableException,
            UnsupportedAudioFileException {
        clip.stop();
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(0);
        clip.start();
    }

    // MODIFIES: this
    // EFFECTS: resets audio stream; throws UnsupportedAudioFileException if audio file in path is of unsupported type
    public void resetAudioStream() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip.open(audioInputStream);
    }
}


