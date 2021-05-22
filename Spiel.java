import GLOOP.*;
import java.lang.Math.*;
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

        startTafel.setzeKamerafixierung(true);
        lebenTafel.setzeKamerafixierung(true);
        levelTafel.setzeKamerafixierung(true);
        
        GLTextur textur = new GLTextur("tMeteorit1.jpg");
        dasUfo = new Ufo();
        derAsteroid = new Asteroid[1000];
        for (int i = 0; i < a; i++) {
            derAsteroid[i] = new Asteroid(textur);
            derAsteroid[i].setzeUfo(dasUfo);
        }            

        fuehreAus();       // starten der Methode fuehreAus
    }

    public void fuehreAus() {
        
        while (true) {
            vorbereiten();
            Runnable controls = () -> 
                { // Vorbereitung des Spiels

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
                        kamera.setzePosition(dasUfo.gibX(),-600,dasUfo.gibZ()+100);
                        kamera.setzeBlickpunkt(dasUfo.gibX(),0,dasUfo.gibZ());

                        kollision_test();
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
                System.out.println("A: "+a);
                for (int i = 0; i < a-1; i++) {
                    derAsteroid[i].bewegeDich(speed); 
                    Sys.warte(0);
                    System.out.println(i);

                } 
            }
            //kill task1
            Sys.warte(1000);
        }
    }

    private void vorbereiten() {
        kollision = false;
        leben = 3;
        level = 1;
        dasUfo.reset();
        for (int i = 0; i < a; i++) {
            derAsteroid[i].setzeUfo(dasUfo);
            derAsteroid[i].bewegeDich(100);
        } 
        kamera.setzePosition(0,-600,200);
        kamera.setzeBlickpunkt(0,0,200);
        kamera.setzeScheitelrichtung(0,0,1);  
        
        
        startTafel.setzeText("Bewege die Maus auf das Ufo. Klicken. Taste drücken.", 20);
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

}