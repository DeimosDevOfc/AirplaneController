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
public class AirplaneHud extends Component { 


public UIRect boresight;
public UIRect point;
public Camera playerCam;
public AirplanePhy m;
private AtomicFloat f;
    /// Run only once
    @Override
    public void start() {
        
    }

    /// Repeat every frame
    @Override

    public void repeat() {
        f.set(m.BoresightPos().getX());
        boresight.setLeft(f.intValue());
    }

    /// Repeat every frame when component or object is disabled
    @Override
    public void disabledRepeat() {
        
    }
}
