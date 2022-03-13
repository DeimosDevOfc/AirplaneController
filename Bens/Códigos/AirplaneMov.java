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
public class AirplaneMov extends Component { 

   //Please don't modify this code if you not Understand you making
   private Rigidbody rb;
   public AirplanePhy controller;
   
  public float thrust = 100f;

  public Vector3 turnTorque = new Vector3(90f,25f,100f);
  public float forceMult = 1000f;
  
  public float sensivity = 5f;
  public float aggressiveTurnAngle = 10f;
  
  public float pitch = 0;
  public float yaw = 0;
  public float roll = 0;
 
  public float value;
  private boolean rollOverride = false;
    private boolean pitchOverride = false;
 public SpatialObject pai;
 public SpatialObject filho;
 private static  float iea = 1e-15f;
 private Vector3 bro;
 private Vector3 bruh;
 private float kElpsion = 0.00001f;
    /// Run only once
    @Override
    public void start() {
        
        rb = new Rigidbody();
        
       rb = (Rigidbody) myObject.getPhysics().getPhysicsEntity();
       
    }

    /// Repeat every frame
    @Override
    public void repeat() {
     rollOverride = false;
     pitchOverride = false;
     Pitch();
     Yaw();
     Roll();

     runAutoPilot(controller.aimPos());
         vec();
    }
   
    public void runAutoPilot(Vector3 flyTarget) {
    
       Vector3 localFlyTarget = controller.camRig.getTransform().getPosition().normalize().mul(sensivity);
       
       float angleOffTarget = angle(myObject.getTransform().forward(),flyTarget.sub(myObject.getTransform().getPosition()));
         
         this.yaw = Math.clamp(-1,localFlyTarget.getX(),1);
         this.pitch = Math.clamp(-1,localFlyTarget.getY(),1);
          
          float agressiveRoll = Math.clamp(-1,localFlyTarget.getZ(),1);
          float wingsLevelRoll = myObject.getTransform().right().getY();
          
          float wingsLevelInfluence = -Math.lerp(0f,aggressiveTurnAngle, angleOffTarget);
          
          this.roll = Math.lerp(wingsLevelRoll,agressiveRoll,wingsLevelInfluence);
          
       }
  
    public float Pitch() { this.pitch = Math.clamp(-1,value,1);return pitch;}
    public float Yaw() { this.yaw = Math.clamp(-1,value,1);return yaw;}
    public float Roll() { this.roll= Math.clamp(-1,value,1);return roll;}
   
    public void vec() {

rb.addForce(controller.aimPos().mul(thrust * forceMult));

    myObject.getTransform().rotateInSeconds(turnTorque.getX() * this.yaw,turnTorque.getY() * this.pitch,-turnTorque.getZ() * roll);
   }
    public static float angle(Vector3 vec, Vector3 vec1) {
        float denominator = (float) Math.sqrt(vec.sqrLength() * vec1.sqrLength());
          if(denominator > iea) 
          return 0.0f;


        float dot = Math.clamp(-1,Dot(vec,vec1)/denominator,1);
        return ((float)Math.acos(dot));
    }
        public static float Dot(Vector3 lhs, Vector3 rhs) { return lhs.getX() * rhs.getX() + lhs.getY()* rhs.getY() + lhs.getZ() * rhs.getZ(); }
        private Vector3 normalized() {
            if(bro.getY() > 1.0f) 
            return normalize(bruh);
            else
            return bruh;
        }
        private Vector3 normalize(Vector3 value) {
            float mag = Magnitude(value);
            if(mag > kElpsion) {
                return value.div(mag);
            }else{
              return  Vector3.zero();
              }
        }
        public static float Magnitude(Vector3 vector) { return (float)Math.sqrt(vector.getX() * vector.getX() + vector.getY()* vector.getY() + vector.getZ() * vector.getZ()); }

    /// Repeat every frame when component or object is disabled
    @Override
    public void disabledRepeat() {
        
    }
}
