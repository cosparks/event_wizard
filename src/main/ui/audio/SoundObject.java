package ui.audio;

import sun.audio.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SoundObject {
    private AudioStream audioStream;
    private boolean firstTime;

    private Clip clip;
    private AudioInputStream audioInputStream;
    private String filePath;


    public SoundObject() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        filePath = "./data/audio/Moist_Crash.aiff";
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        firstTime = true;
    }

    public SoundObject(String fileName) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        filePath = "./data/audio/" + fileName;
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        firstTime = true;
    }

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

    public void restart() throws IOException, LineUnavailableException,
            UnsupportedAudioFileException {
        clip.stop();
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(0);
        clip.start();
    }

    public void resetAudioStream() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip.open(audioInputStream);
    }
}


