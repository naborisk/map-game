import java.io.File;
import java.net.MalformedURLException;

import javafx.scene.media.AudioClip;

public class MusicController {
    private static int a = 1;
    public static void BGM() {
        try {
            AudioClip ac = new AudioClip(new File("tammb14.mp3").toURI().toString());
            ac.setCycleCount(AudioClip.INDEFINITE);
            ac.play();
            ac.setVolume(3);
            ac.setRate(1);
            if (a == 0) {
                ac.stop();
            }
        } catch (Exception e) {
            System.err.print(e);
        }
    }
    public static void setA(int x) {
        a = x;
    }
    public static void effectSound1() {
        try {
            System.out.println("effectSound");
            AudioClip ac2 = new AudioClip(new File("kira.mp3").toURI().toString());
            ac2.play();
            ac2.setVolume(3);
            ac2.setRate(0.1);
        } catch (Exception e) {
            System.err.print(e);
        }
    }

    public static void effectSound2() {
        try {
            System.out.println("effectSound");
            AudioClip ac2 = new AudioClip(new File("kira2.mp3").toURI().toString());
            ac2.play();
            ac2.setVolume(3);
            ac2.setRate(1);
        } catch (Exception e) {
            System.err.print(e);
        }
    }

    public static void effectSound3() {
        try {
            System.out.println("effectSound");
            AudioClip ac2 = new AudioClip(new File("kansei.mp3").toURI().toString());
            ac2.play();
            ac2.setVolume(3);
            ac2.setRate(1);
        } catch (Exception e) {
            System.err.print(e);
        }
    }
}
