package mapgame.controller;

import java.io.File;
import java.net.MalformedURLException;

import javafx.scene.media.AudioClip;

public class MusicController {
    //エラーが出たらまずパスを変えてみて下さい。
    public static final String BGM_PATH = "mapgame/assets/bgm/";
    public static final String SFX_PATH = "mapgame/assets/sfx/";

    AudioClip bgm;

    public MusicController() {
        try {
            bgm = new AudioClip(new File(BGM_PATH + "tammb14.mp3").toURI().toString());
            bgm.setCycleCount(AudioClip.INDEFINITE);
            bgm.setVolume(2);
            bgm.setRate(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playBgm() {
        try {
            bgm.play();
        } catch (Exception e) {
            System.out.println("Music not found or can't be played");
        }
    }

    public void stopBgm() {
       if(bgm.isPlaying()) bgm.stop();
    }

    public void play(String path) {
        try {
            System.out.println("effectSound");
            AudioClip ac = new AudioClip(new File(path).toURI().toString());
            ac.setVolume(2);
            ac.setRate(1);
            ac.play();
        } catch (Exception e) {
            System.err.print(e);
        }
    }

    public void playSfx1() {
        try {
            System.out.println("effectSound");
            AudioClip ac = new AudioClip(new File(SFX_PATH + "kira2.mp3").toURI().toString());
            ac.setVolume(3);
            ac.setRate(1);
            ac.play();
        } catch (Exception e) {
            System.err.print(e);
        }
    }

    public static void effectSound1() {
        try {
            System.out.println("effectSound");
            AudioClip ac2 = new AudioClip(new File(SFX_PATH + "kira.mp3").toURI().toString());
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
            AudioClip ac2 = new AudioClip(new File(SFX_PATH + "kira2.mp3").toURI().toString());
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
            AudioClip ac2 = new AudioClip(new File(SFX_PATH + "kansei.mp3").toURI().toString());
            ac2.play();
            ac2.setVolume(3);
            ac2.setRate(1);
        } catch (Exception e) {
            System.err.print(e);
        }
    }
}
