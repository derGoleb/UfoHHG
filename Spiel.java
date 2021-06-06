import GLOOP.*;
import java.lang.Math.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;  
import java.util.Scanner;
public class Spiel {
    // Das Spiel verwendet die folgenden Objekte:
    // Assoziationen/Beziehungen
    private GLKamera kamera;
    private GLLicht licht;
    private GLTastatur tastatur;
    private GLHimmel himmel;
    private GLMaus maus;
    private GLTafel startTafel;
    private GLTafel levelTafel;
    private GLTafel lebenTafel;
    private GLTafel PunkteTafel;

    private Ufo dasUfo;
    private Asteroid[] derAsteroid;
    private Boolean restart;
    // Das Spiel verfügt über die folgenden Variablen, um sicZustände zu merken:
    // Attribute
    private int alteMausX;
    private int alteMausY;
    boolean kollision = false;
    private int a = 150;
    private int level = 1;
    private int leben = 3;
    private int speed = 1;
    private int points = 0;
    private int highscore = 0;
    private int hs_cache;

    // Konstruktor (Wird aufgerufen, wenn das Spiel gestartet/erzeugt wird.)
    public Spiel() {
        // zunächst werden alle verwendeten Objekte erzeugt
        kamera = new GLKamera();  
        // die Kamera wird so positioniert, dass man in y-Richtung guckt 
        kamera.setzePosition(0,-600,200);
        kamera.setzeBlickpunkt(0,0,200);
        kamera.setzeScheitelrichtung(0,0,1);    // nach oben ist jetzt z-Richtung
        // ----------------------------------------
        licht  = new GLLicht(-5000,-10000,0);
        himmel = new GLHimmel("Sternenhimmel1.jpg");

        tastatur = new GLTastatur();
        maus = new GLMaus();

        startTafel = new GLTafel(0,100,200,10,10);
        startTafel.setzeAutodrehung(true);  // die Tafel dreht sich automatisch zum Beobachter

        levelTafel = new GLTafel(480,100,510,10,10);
        levelTafel.setzeAutodrehung(true);

        lebenTafel = new GLTafel(-480,100,510,10,10);
        lebenTafel.setzeAutodrehung(true);

        PunkteTafel = new GLTafel(0,100,510,10,10);
        PunkteTafel.setzeAutodrehung(true);

        startTafel.setzeKamerafixierung(true);
        lebenTafel.setzeKamerafixierung(true);
        levelTafel.setzeKamerafixierung(true);
        PunkteTafel.setzeKamerafixierung(true);

        GLTextur textur = new GLTextur("tMeteorit1.jpg");
        dasUfo = new Ufo();
        derAsteroid = new Asteroid[1000];
        for (int i = 0; i < a; i++) {
            derAsteroid[i] = new Asteroid(textur);
            derAsteroid[i].setzeUfo(dasUfo);
        }            

        //Starten des Spiels
        fuehreAus();       
    }

    public void fuehreAus() {

        while (true) {
            //Vorbereiten für eine neue Runde
            vorbereiten();
            Runnable controls = () -> 
                {

                    while(leben > 0) {

                        if (tastatur.links() /*&& !(dasUfo.gibX() <= -560)*/){
                            dasUfo.bewegeLinks();
                        }
                        if (tastatur.rechts() /* && !(dasUfo.gibX() >= 560)*/){
                            dasUfo.bewegeRechts();
                        }
                        if (tastatur.unten()   /*&& !(dasUfo.gibZ() <= -510)*/){
                            dasUfo.bewegeUnten();
                        }
                        if (tastatur.oben()   /*&& !(dasUfo.gibZ() >= 510)*/){
                            dasUfo.bewegeOben();
                        }
                        if (tastatur.shift()   /*&& !(dasUfo.gibZ() >= 510)*/){
                            laser();
                        }
                        kamera.setzePosition(dasUfo.gibX(),-600,dasUfo.gibZ()+100);
                        kamera.setzeBlickpunkt(dasUfo.gibX(),0,dasUfo.gibZ());

                        points = points + 1*level;
                        PunkteTafel.setzeText(points+"/"+highscore, 60);

                        kollision_test();

                        test_highscore();
                        Sys.warte();
                    }
                    speed = 1;

                };

            Runnable set_level = () -> 
                {
                    while (level <= 10 && leben > 0){
                        Sys.warte(10000);
                        speed = speed + 1;
                        level = level + 1;
                        levelTafel.setzeText("Level: "+level, 60);
                    }

                };

            // das Spiel beginnt
            new Thread(set_level).start();
            new Thread(controls).start();

            while(leben > 0) {

                for (int i = 0; i < a-1; i++) {
                    derAsteroid[i].bewegeDich(speed); 
                    Sys.warte(0);
                    

                } 
            }
            //kill task1
            Sys.warte(1000);
        }
    }

    private void vorbereiten() {
        highscore = get_highscore();

        kollision = false;
        leben = 3;
        level = 1;
        points = 0;
        PunkteTafel.setzeText(points+"/"+highscore, 60);
        dasUfo.reset();

        for (int i = 0; i < a; i++) {
            derAsteroid[i].setzeUfo(dasUfo);
            derAsteroid[i].bewegeDich(100);
        } 
        kamera.setzePosition(0,-600,200);
        kamera.setzeBlickpunkt(0,0,200);
        kamera.setzeScheitelrichtung(0,0,1);  

        startTafel.setzeText("Zum Starten beliebige Taste drücken! Highscore: "+highscore, 30);
        startTafel.setzeSichtbarkeit(true); // Tafel einblenden

        lebenTafel.setzeText("Leben: "+leben, 60);
        levelTafel.setzeText("Level: "+level, 60);

        while(!tastatur.istGedrueckt()) { }  // warten bis Taste gedrückt

        startTafel.setzeSichtbarkeit(false);   // Tafel ausblenden

    }

    private void kollision_test() {
        for (int i = 0; i < a; i++) {

            if(derAsteroid[i].kollision()){
                leben = leben - 1;
                lebenTafel.setzeText("Leben: "+leben, 60);
                Sys.warte(500);
            }
            if (leben < 1) {
                startTafel.setzeText("Game Over!", 60);
            }
        }   
    }

    private int get_highscore() {
        File hs_file = new File("highscore.txt");
        try {
            Scanner sc = new Scanner(hs_file);
            while (sc.hasNextLine()) {
                hs_cache = Integer.parseInt(sc.nextLine());
            }
            sc.close();
        } catch (FileNotFoundException none) {
        }
        return hs_cache;
    }

    private void test_highscore() {
        if (points > highscore) {
            try {
                FileWriter wr = new FileWriter("highscore.txt");
                wr.write(""+points);
                wr.close();
            } catch (IOException none) {
            }
        }
    }

    private void laser() {

        dasUfo.laser.setzeSichtbarkeit(true);
        for (int i = 0; i < a; i++) {
            if(derAsteroid[i].laser_hit()){
                derAsteroid[i].zuruecksetzen();
                System.out.println("Hit");
            }
            else {
            }
        }
        Sys.warte(100);
        dasUfo.laser.setzeSichtbarkeit(false);

    }
}