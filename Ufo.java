import GLOOP.*;
public class Ufo {
    private GLTorus rumpf;
    private GLKugel cockpit;
    private GLKegel fluegel1, fluegel2;
    public GLZylinder laser;

    public Ufo(){
        rumpf = new GLTorus(0,0,0,50,20);
        rumpf.skaliere (0.5,1,0.5);
        rumpf.setzeQualitaet(24);

        cockpit = new GLKugel(0,0,0,25);
        cockpit.setzeMaterial(GLMaterial.GOLD);

        fluegel1 = new GLKegel(-60,-10,0,15,70);
        fluegel1.skaliere(0.2,1,1);        
        fluegel1.drehe(0,90,0, -60,10,0);
        fluegel1.drehe(0,0,45, 0,0,0);
        fluegel1.setzeFarbe(0,0,1);

        fluegel2 = new GLKegel(60,-10,0,15,70);
        fluegel2.skaliere(0.2,1,1);        
        fluegel2.drehe(0,-90,0, 60,10,0);
        fluegel2.drehe(0,0,-45, 0,0,0);
        fluegel2.setzeFarbe(0,0,1);      
        
        laser = new GLZylinder(0,0,-1500,3, 3000);
        laser.drehe(90,0,0, 0,0,0);
        laser.setzeFarbe(1,0.1,0);
        laser.setzeSelbstleuchten(1,0.1,0);
        laser.setzeSichtbarkeit(false);
    }

    public void reset(){
        rumpf.setzePosition(0,0,0);
        fluegel1.setzePosition(-60,-10,0);
        fluegel2.setzePosition(60,-10,0);
        cockpit.setzePosition(0,0,0);
    }
    
    public void bewegeLinks(){
            rumpf.verschiebe(-5,0,0);
            cockpit.verschiebe(-5,0,0);
            fluegel1.verschiebe(-5,0,0);
            fluegel2.verschiebe(-5,0,0);
            laser.verschiebe(-5,0,0);
            
            Sys.warte();
    }

    public void bewegeRechts(){       
        rumpf.verschiebe(5,0,0);
        cockpit.verschiebe(5,0,0);
        fluegel1.verschiebe(5,0,0);
        fluegel2.verschiebe(5,0,0);
        laser.verschiebe(5,0,0);
        Sys.warte();
    } 
    
    public void bewegeUnten(){
        rumpf.verschiebe(0,0,-5);
        cockpit.verschiebe(0,0,-5);
        fluegel1.verschiebe(0,0,-5);
        fluegel2.verschiebe(0,0,-5);
        laser.verschiebe(0,0,-5);
        Sys.warte();
    }

    public void bewegeOben(){       
        rumpf.verschiebe(0,0,5);
        cockpit.verschiebe(0,0,5);
        fluegel1.verschiebe(0,0,5);
        fluegel2.verschiebe(0,0,5);
        laser.verschiebe(0,0,5);
        Sys.warte();    
    } 
    
    public float gibX(){
        float X = rumpf.gibX();
        return(X);
    
    }
    
    public float gibY(){
        float Y = rumpf.gibY();
        return(Y);
    
    }
    
    public float gibZ(){
        float Z = rumpf.gibZ();
        return(Z);
    
    }
    
    public void destroy_animation() {
    
    
    }
}