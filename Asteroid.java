import GLOOP.*;
public class Asteroid{
    private GLKugel koerper;
    private Ufo dasUfo;

    public Asteroid(GLTextur pTex){
        int x = (int)(Math.random()*1000 - 500);
        int y = (int)(Math.random()*4000);
        int z = (int)(Math.random()*1000 - 500);
        koerper = new GLKugel(x,y,z, 50, pTex);       
        koerper.setzeSkalierung(1,Math.random()+0.3,Math.random()+0.3);
    }

    public void bewegeDich(){
        koerper.verschiebe(0,-5,0);   
        koerper.drehe(0.1,0.1,0.1);

        if (koerper.gibY()<-300) this.zuruecksetzen();
    }

    private void zuruecksetzen(){
        int x = (int)(Math.random()*1000 - 500);
        int z = (int)(Math.random()*1000 - 500);
        koerper.setzePosition(x,4000,z);
        koerper.setzeSkalierung(1,Math.random()+0.3,Math.random()+0.3);
    }
    
    public boolean kollision(){
        boolean kollidiert = false;
        double dy = dasUfo.gibY() - koerper.gibY();
        double dx = dasUfo.gibX() - koerper.gibX();
        double dz = dasUfo.gibZ() - koerper.gibZ();
        double distanz =  Math.sqrt(dx*dx+dy*dy+dz*dz);
        if (distanz < 50){
        kollidiert = true;
       }
       return kollidiert;
    } 
    
    
   
}
