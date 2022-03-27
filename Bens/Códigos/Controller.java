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


public class Controller extends Component { 

   public SpatialObject aim;
   public SpatialObject camRig;
   public SpatialObject airplane;
   public SpatialObject camera;
  
   public float aimDistance = 5000;
   private Vector3 frozenDirection;
   private boolean isAimFrozen;
   
   public Vector3 aimPos;
    public Vector3 boresightPos() {
          
          if(airplane == null) {
             return myObject.getTransform().forward() * aimDistance;
        }else{
            return (airplane.getTransform().forward() * aimDistance) + airplane.getTransform().getPosition();
       }
    }
    public Vector3 aimPos() {
   
             if(isAimFrozen == true) 
               return new Vector3(0,0,0);
              else
               return aim.getTransform().getGlobalPosition() + (aim.getTransform().forward() * aimDistance);

    }

    private Vector3 getFrozenAimPos() {
        if (aim != null)
                return aim.getTransform().getPosition().sum(frozenDirection * aimDistance);
            else
                return myObject.getTransform().forward().mul(aimDistance);
    }/// Run only once
    @Override
    public void start() {
        frozenDirection = myObject.getTransform().forward();
        isAimFrozen = false;

    }

    /// Repeat every frame
    @Override
    public void repeat() {
        myObject.getTransform().setPosition(airplane.getTransform().getPosition());
       aimPos = aimPos();
       aim.getTransform().setRotation(camRig.getTransform().getGlobalRotation());
    }
}
