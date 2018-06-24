package ipump;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
  * Input class serves to interact with the simulation.
  * The keys are checked if they are pressed now to avoid the problem from
  * Activate the same option several times.
  *
  * The C key is not checking the old state because it serves to fill the device
  * Therefore, if you keep pressed, the appliance is charging.
  *
  */
public class Input implements KeyListener{
    private boolean keyA = false;
    private boolean keyB = false;
    private boolean keyC = false;
    private boolean keyE = false;
    private boolean keyF = false;
    private boolean keyI = false;
    private boolean keyM = false;
    private boolean keyN = false;
    private boolean keyO = false;
    private boolean keyP = false;
    private boolean keyR = false;
    private boolean keyV = false;
    private boolean keyX = false;
    
    private boolean keyAOld = false;
    private boolean keyBOld = false;
    private boolean keyEOld = false;
    private boolean keyFOld = false;
    private boolean keyIOld = false;
    private boolean keyMOld = false;
    private boolean keyNOld = false;
    private boolean keyOOld = false;
    private boolean keyPOld = false;
    private boolean keyROld = false;
    private boolean keyVOld = false;
    private boolean keyXOld = false;
    
    int speedKey = 1;
    
    /**
     * Constructor.
     */
    public Input() {
    }
    
    /**
     * Override the method that is performed when holding the dialed key
     * 
     * @param e The button that is pressed
     */
    public void keyPressed(KeyEvent e){
    	// Keys
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_A: keyA = true; break;
            case KeyEvent.VK_B: keyB = true; break;
            case KeyEvent.VK_C: keyC = true; break;
            case KeyEvent.VK_E: keyE = true; break;
            case KeyEvent.VK_F: keyF = true; break;
            case KeyEvent.VK_I: keyI = true; break;
            case KeyEvent.VK_M: keyM = true; break;
            case KeyEvent.VK_N: keyN = true; break;
            case KeyEvent.VK_O: keyO = true; break;
            case KeyEvent.VK_P: keyP = true; break;
            case KeyEvent.VK_R: keyR = true; break;
            case KeyEvent.VK_V: keyV = true; break;
            case KeyEvent.VK_X: keyX = true; break;
        }
        
     // Speed numbers
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_1: speedKey = 1; break;
            case KeyEvent.VK_2: speedKey = 2; break;
            case KeyEvent.VK_3: speedKey = 4; break;                
            case KeyEvent.VK_4: speedKey = 8; break;
            case KeyEvent.VK_5: speedKey = 16; break;                
            case KeyEvent.VK_6: speedKey = 32; break;
            case KeyEvent.VK_7: speedKey = 64; break;
            case KeyEvent.VK_8: speedKey = 128; break;
            case KeyEvent.VK_9: speedKey = 256; break;                
            case KeyEvent.VK_0: speedKey = 512; break;
        }
    }

    /**
     * Override the method used to stop the hold of the key.
     * 
     * @param e The button that is released
     */
    public void keyReleased(KeyEvent e){   
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_A: keyA = false; break;
            case KeyEvent.VK_B: keyB = false; break;
            case KeyEvent.VK_C: keyC = false; break;
            case KeyEvent.VK_E: keyE = false; break;
            case KeyEvent.VK_F: keyF = false; break;
            case KeyEvent.VK_I: keyI = false; break;
            case KeyEvent.VK_M: keyM = false; break;
            case KeyEvent.VK_N: keyN = false; break;
            case KeyEvent.VK_O: keyO = false; break;
            case KeyEvent.VK_P: keyP = false; break;
            case KeyEvent.VK_R: keyR = false; break;
            case KeyEvent.VK_V: keyV = false; break;
            case KeyEvent.VK_X: keyX = false; break;
        }
    }
    
    /**
     * Shows the past value of the variables.
     * Mind to avoid duplicate pressures.
     */
    public void setOldKeys(){
        keyEOld = keyE;
        keyROld = keyR;
        keyIOld = keyI;
        keyOOld = keyO;
        keyPOld = keyP;
        keyXOld = keyX;
        keyFOld = keyF;
        keyVOld = keyV;
        keyBOld = keyB;
        keyNOld = keyN;
        keyMOld = keyM;
        keyAOld = keyA;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }
        
    /**
     * It returns speed.
     *
     * @return the speed that is determined by the number that is pressed
     */
    public int speedKeyPressed()
    {
        return speedKey;
    }
    
    /**
     * Check if someone keeps pressing the button.
     *
     * @return Whether C is pressed.
     */
    public boolean isKeyC() {
        return keyC;
    }
    
    /**
     * The key is currently pressed.
      *
      * @return Has the A key been pressed at the moment?
     */
    public boolean isKeyAPressed() {
        return keyA && !keyAOld;
    }
        
    /**
     * The key is currently pressed.
      *
      * @return Has the button B been pressed at the moment?
     */
    public boolean isKeyBPressed() {
        return keyB && !keyBOld;
    }
    
    /**
     * The key is currently pressed.
      *
      * @return Has the E key been pressed now?
     */
    public boolean isKeyEPressed() {
        return keyE && !keyEOld;
    }

    /**
     * The key is currently pressed.
      *
      * @return The R key has now been pressed?
     */
    public boolean isKeyRPressed() {
        return keyR && !keyROld;
    }
    
    /**
     * The key is currently pressed.
      *
      * @return Has the F key been pressed now?
     */
    public boolean isKeyFPressed() {
        return keyF && !keyFOld;
    }
       
    /**
     * The key is currently pressed.
      *
      * @return Has the I button been pressed at the moment
     */
    public boolean isKeyIPressed() {
        return keyI && !keyIOld;
    }
    
    /**
     * The key is currently pressed.
      *
      * @return Whether the M button has now been pressed
     */
    public boolean isKeyMPressed() {
        return keyM && !keyMOld;
    }
    
    /**
     * The key is currently pressed.
      *
      * @return Whether the N key has now been pressed
     */
    public boolean isKeyNPressed() {
        return keyN && !keyNOld;
    }
    
    /**
     * The key is currently pressed.
      *
      * @return Whether the O key has now been pressed
     */
    public boolean isKeyOPressed() {
        return keyO && !keyOOld;
    }
       
    /**
     * The key is currently pressed.
      *
      * @return Whether the P button has now been pressed
     */
    public boolean isKeyPPressed() {
        return keyP && !keyPOld;
    }
    
    /**
     * The key is currently pressed.
      *
      * @return Has the V key been pressed at the moment?
     */ 
    public boolean isKeyVPressed() {
        return keyV && !keyVOld;
    }
    
    /**
     * The key is currently pressed.
      *
      * @return Whether the X key has now been pressed
     */
    public boolean isKeyXPressed() {
        return keyX && !keyXOld;
    } 
}
