import GLOOP.*;
public class Spiel {
    // Das Spiel verwendet die folgenden Objekte:
    // Assoziationen/Beziehungen
    private GLKamera kamera;
    private GLLicht licht;
    private GLTastatur tastatur;
    private GLHimmel himmel;
    private GLMaus maus;
    private GLTafel eineTafel;

    private Ufo dasUfo;
    private Asteroid[] derAsteroid;
    
    // Das Spiel verfügt über die folgenden Variablen, um sich Zustände zu merken:
    // Attribute
    private int alteMausX;
    private int alteMausY;
    
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
        
        eineTafel = new GLTafel(0,100,200,10,10);
        eineTafel.setzeAutodrehung(true);  // die Tafel dreht sich automatisch zum Beobachter

        GLTextur textur = new GLTextur("tMeteorit1.jpg");
        
        derAsteroid = new Asteroid[10];
        for (int i = 0; i < 10; i++) {
                derAsteroid[i] = new Asteroid(textur);
            }            
        dasUfo = new Ufo();

        fuehreAus();       // starten der Methode fuehreAus
    }

    public void fuehreAus() {
        // Vorbereitung des Spiels
        eineTafel.setzeText("Bewege die Maus auf das Ufo. Klicken. Taste drücken.", 20);
        eineTafel.setzeSichtbarkeit(true);   // Tafel einblenden
        
        while(!tastatur.istGedrueckt()) { }  // warten bis Taste gedrückt
        
        
        eineTafel.setzeSichtbarkeit(false);   // Tafel ausblenden
        
        // das Spiel beginnt
        while(!tastatur.esc()) {
            if (tastatur.links() /*&& !(dasUfo.gibX() >= -500)*/){
                dasUfo.bewegeLinks();
            }
            if (tastatur.rechts() /*&& !(dasUfo.gibX() >= 500)*/){
                dasUfo.bewegeRechts();
            }
            if (tastatur.unten() /*&& !(dasUfo.gibY() >= -170)*/){
                dasUfo.bewegeUnten();
            }
            if (tastatur.oben() /*&& !(dasUfo.gibY() >= 270)*/){
                dasUfo.bewegeOben();
            }
            
            for (int i = 0; i < 10; i++) {
                derAsteroid[i].bewegeDich();
            }            

            Sys.warte();
        }
        Sys.beenden(); 
    }
}
