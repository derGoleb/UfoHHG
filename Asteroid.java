import GLOOP.*;
public class Asteroid{
    private GLKugel koerper;
    private Ufo dasUfo;

    public Asteroid(GLTextur pTex){
        int x = (int)(Math.random()*10000 - 5000);
        int y = (int)(Math.random()*4000);
        int z = (int)(Math.random()*10000 - 5000);
        koerper = new GLKugel(x,y,z, 50, pTex);       
        koerper.setzeSkalierung(1,Math.random()+0.3,Math.random()+0.3);
    }

    public void bewegeDich(int speed){
        koerper.verschiebe(0,speed*-5,0);   
        koerper.drehe(0.1,0.1,0.1);

        if (koerper.gibY()<-300) this.zuruecksetzen();
    }

    public void zuruecksetzen(){
        int x = (int)(Math.random()*10000 - 5000);
        int z = (int)(Math.random()*10000 - 5000);
        koerper.setzePosition(dasUfo.gibX()+x,4000,dasUfo.gibZ()+z);
        koerper.setzeSkalierung(1,Math.random()+0.3,Math.random()+0.3);
    }
    
    public boolean kollision(){
        boolean kollidiert = false;
        double dy = dasUfo.gibY() - koerper.gibY();
        double dx = dasUfo.gibX() - koerper.gibX();
        double dz = dasUfo.gibZ() - koerper.gibZ();
        double distanz =  Math.sqrt(dx*dx+dy*dy+dz*dz);
        if (distanz < 65){
        kollidiert = true;
       }
       return kollidiert;
    } 
    
    public boolean laser_hit(){
        boolean lkollidiert = false;
        double dy = dasUfo.gibY() - koerper.gibY();
        double dx = dasUfo.gibX() - koerper.gibX();
        double dz = 0;
        double distanz =  Math.sqrt(dx*dx+dy*dy+dz*dz);
        if (distanz < 100){
        lkollidiert = true;
       }
       System.out.println(distanz);
       return lkollidiert;
       
    }
    
    public void setzeUfo(Ufo neuesUfo){
        dasUfo = neuesUfo;
    
    }
    
}
