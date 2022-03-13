package JAVARuntime;

// Useful imports
import java.util.*;
import java.text.*;
import java.net.*;
import java.math.*;
import java.io.*;
import java.nio.*;

/**
 * @Author 
*/
public class AirplanePhy extends Component {
    
    
public SpatialObject aim;
public SpatialObject airplane;
public float aimDistance;
public boolean isAimFrozen = false;
public boolean useFixed;
public SpatialObject cam;
public SpatialObject camRig;
public float camSmothSpeed = 5;
    /// Run only once
    @Override
    public void start() {
        
    }

    /// Repeat every frame
    @Override
    public void repeat() {
    Thread t = new Thread(new Runnable () {
        
 
        public void run() {
                UpdateCameraPos();
                }
           });
              t.run();
                RotateRig();

    }
    public Vector3 BoresightPos() {
        Vector3 vec = new Vector3();
        
        if(airplane == null) {
            
           vec = airplane.getTransform().forward();
            vec.setZ(vec.getZ() * aimDistance);
            vec.setY(vec.getY() * aimDistance);
            
            vec.setZ(vec.getZ() + airplane.getTransform().getPosition().getZ());
            vec.setY(vec.getY() + airplane.getTransform().getPosition().getY());
          
        } 
        return vec;
    }
    public Vector3 aimPos() {
         Vector3 vec = new Vector3();
         if(aim != null) {
             if(isAimFrozen == true) {
   
            
              }else{
                vec = aim.getTransform().getGlobalPosition();
              
              vec.setX(aim.getTransform().forward().getX() * aimDistance);
              vec.setY(aim.getTransform().forward().getY() * aimDistance);
              vec.setZ(aim.getTransform().forward().getZ() * aimDistance);
              }
         }else{
             vec = myObject.getTransform().forward();
             
             vec.setX(vec.getX() * aimDistance);
             vec.setY(vec.getY() * aimDistance);
             vec.setZ(vec.getZ() * aimDistance);
             
        }
       return vec;
    }
    
    public void UpdateCameraPos() {


    }
    public void RotateRig() {
         if(aim == null || cam == null || camRig == null)
         return;
       
       
      Vector3   upVec = (Math.abs(aim.getTransform().forward().getY()) > 0.9f) ? camRig.getTransform().up() : new Vector3(0,1,0);

       aim.getTransform().setRotation(camRig.getTransform().getGlobalRotation());

      
    }
    public Quaternion Damp(Quaternion a, Quaternion b,float lambda,float dt) {
        return Quaternion.blendOut(a,b,1- Math.exp(-lambda * dt));
    }
    /// Repeat every frame when component or object is disabled
    @Override
    public void disabledRepeat() {
        
    }
}
