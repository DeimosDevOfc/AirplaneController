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
public class AirplanePhysics extends Component { 
    public SpatialObject cube;
    public Controller controller;
    public float thrust = 100;
    private float pitch;
    private float yaw;
    private float roll;
    private float kElpsion = 0.00001f;
    public Vector3 turnTorque = new Vector3(90f,55f,45f);
    public float sensivity = 5;
    public float agressiveTurn = 5;
    private static  float iea = 1e-15f;
    private Rigidbody rb;
    private boolean pitchOverride;
    private boolean rollOverride;
    private Vector2 joystick;
    private float autoYaw;
    private float autoPitch;
    private float autoRoll;
    
    /// Run only once
    @Override
    public void start() {
        rb = (Rigidbody) myObject.getPhysics().getPhysicsEntity();
        joystick = Input.getAxis("joystick").getValue();
   
    }

    /// Repeat every frame
    @Override
    public void repeat() {
        Pitch();
        Yaw();
        Roll();
        rollOverride = false;
        pitchOverride = false;
        
        if(Math.abs(joystick.getX()) >= .25f) {
            rollOverride = true;
        }
        if(Math.abs(joystick.getY()) >= .25) {
            pitchOverride = true;
        }
        FixedRepeat();
        yaw = autoYaw;

        pitch = (pitchOverride) ? joystick.getY() * 2: this.autoPitch;
        roll = (rollOverride) ? joystick.getX() * 2 : this.autoRoll;
        if(Math.abs(joystick.getX()) >= .25 || Math.abs(joystick.getY()) >= .25)
        RunAutoPilot(controller.aimPos(),this.pitch, this.yaw, this.roll);
        else
       RunAutoPilot(controller.aimPos());
    }
    

    private void RunAutoPilot(Vector3 flyTarget,float pitch,float yaw,float roll) {
        Vector3 localFlyTarget = myObject.getTransform().inverseTransformPoint(flyTarget).normalize();
        float angleOffTarget = angle(myObject.getTransform().forward(), flyTarget.sub(myObject.getTransform().getPosition()));
        pitch = -Math.clamp(-1,localFlyTarget.getY(),1);
        yaw = Math.clamp(-1,localFlyTarget.getX(),1);
       
        float agressiveRoll = Math.clamp(-1,localFlyTarget.getX(),1);
        float wingsLevelRoll = myObject.getTransform().right().getY();
        
        float wingsLevelInfluence = -Math.lerp(0f,agressiveTurn, angleOffTarget);
        roll = Math.lerp(wingsLevelRoll,agressiveRoll,wingsLevelInfluence);
        
    }
    private void RunAutoPilot(Vector3 flyTarget) {


        Vector3 localFlyTarget = myObject.getTransform().inverseTransformPoint(flyTarget).mul(sensivity);
        float angleOffTarget = angle(myObject.getTransform().forward(), flyTarget.sub(myObject.getTransform().getPosition()));
       cube.getTransform().setPosition(localFlyTarget);
        this.autoPitch = -Math.clamp(-1,localFlyTarget.getY(),1);
        this.autoYaw = Math.clamp(-1,localFlyTarget.getX(),1);
        
        float agressiveRoll = Math.clamp(-1,localFlyTarget.getX(),1);
        float wingsLevelRoll = myObject.getTransform().right().getY();
    
        float wingsLevelInfluence = Math.lerp(0f,agressiveTurn, angleOffTarget);
        this.autoRoll = Math.lerp(wingsLevelRoll,agressiveRoll,wingsLevelInfluence);
    }


    private float Pitch() {return Math.clamp(-1,pitch,1);}
    private float Yaw() {return Math.clamp(-1,yaw,1);}
    private float Roll() {return Math.clamp(-1,roll,1);}
    public float angle(Vector3 vec, Vector3 vec1) {
        float denominator = (float) Math.sqrt(vec.sqrLength() * vec1.sqrLength());
          if(denominator > iea) 
          return 0.0f;

        float dot = Math.clamp(-1,Dot(vec,vec1)/denominator,1);
        return ((float)Math.acos(dot));
    } 
  
    public void FixedRepeat() {
       myObject.getTransform().moveInSeconds(0,0,40);
        myObject.getTransform().rotateInSeconds(turnTorque.getX() * this.pitch,turnTorque.getY() * this.yaw,-turnTorque.getZ() * this.roll);
    }
    public float Dot(Vector3 lhs, Vector3 rhs) { return lhs.getX() * rhs.getX() + lhs.getY()* rhs.getY() + lhs.getZ() * rhs.getZ(); }
    
    
    /// Repeat every frame when component or object is disabled
    @Override
    public void disabledRepeat() {
        
    }
}
