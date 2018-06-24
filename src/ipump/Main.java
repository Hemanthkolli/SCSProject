package ipump;

/**
  * Main class.
  * From here the program starts.
  * Mute to initialize the simulation including input, output and controller.
  * Works all interactions with the simulator.
  *
  */
public class Main {
    // Da li ce se nivo secera u krvi racunati u miligramima/decilitre ili mmol/l.
    // Po default je ispis mmol/l
    public static boolean mg = false;
    
    /**
    * the main method from where the program starts.
    *
    * @param args Arguments from the command line.
    */
    public static void main(String[] args) {
        Human man = new Human();
        Controller controller = new Controller(man);
        Input input = new Input();
        Window window = new Window(controller, input);

        // Main loop simulation
        while(true) {            
        	// Set the clock speed to be equal to the key pressed
            Clock.setSpeed(input.speedKeyPressed());
            // Stops any current sound if the clock speed is greater than 8
            if (Clock.getSpeed() > 8)
            {
                Wav.stop();
            }
            // Simulation Controls
            controls(input, man, controller);
            // Sets the old keys
            input.setOldKeys();
            
         // When a minute of simulation goes through, the program enters the branch
            if (Clock.update())
            {
            	// A man is updated
                man.update();
             // An insulin pump is being updated
                controller.update();    
            }
        }
    }
    
    /**
          * Works all modifications and interactions with the system.
          *
          * @param input keyboard
          * @param man user
          * @param controller appliance
          * @param battery battery in the appliance
    */
    private static void controls(Input input, Human man, Controller controller)
    {
    	// Power on the device if A pressed
        if(input.isKeyAPressed())
        {
            controller.power();
            
            Wav.stop();
            if (controller.isOn())
                Wav.play("beep.wav");   
        }
        
     // When the C button is pressed, the battery is charging
        if (input.isKeyC())
        {
            controller.getBattery().charging();
        } else {
            controller.getBattery().chargingOff();
        }

     // User eats food on pressing E
        if (input.isKeyEPressed())
        {
            man.eat();
            
            Wav.play("eat.wav");
        }

     // The user eats a lot of food on pressing R
        if (input.isKeyRPressed())
        {
            man.eatBig();
            
            Wav.play("eat.wav");
        }

     // Insulin filling was removed from the device I
        if (input.isKeyIPressed())
        {
            controller.reservoirRemoved();
        }

     // Insulin infusion was inserted into the device O
        if (input.isKeyOPressed())
        {
            controller.replaceCartridge(true);
        }

     // An old filling of insulin into the apparatus was inserted P
        if (input.isKeyPPressed())
        {
            controller.replaceCartridge(false);
        }
         
     // If the device is turned on, the device enters the branch
        if (controller.isOn())
        {
        	// Change the milligrams / decilitre millimeter to mmol / l and vice versa X
            if(input.isKeyXPressed())
            {
                mg = !mg;
            }

         // The user has personally pressed the button to get one dose of insulin F
            if(input.isKeyFPressed())
            {
                controller.manualInject();
            }

         // Errors
         // Changes the state of error by delivering insulin. Insulin will no longer be delivered until it is corrected  V          
            if(input.isKeyVPressed())
            {
                controller.alarm.errorDelivery = !controller.alarm.errorDelivery;
                
                Wav.stop();
                if (controller.alarm.errorDelivery)
                    Wav.play("alarm.wav");
            }

         // Changes the state of the error with the sensor. It will not print new values until it is corrected
         // nor will it allow injection of insulin. B
            if(input.isKeyBPressed())
            {
                controller.alarm.errorSensor = !controller.alarm.errorSensor;
                
                Wav.stop();
                if (controller.alarm.errorSensor)
                    Wav.play("alarm.wav");   
            }

         // Changes the status of the error with the needle. It will not deliver more insulin until it is corrected N
            if(input.isKeyNPressed())
            {
                controller.alarm.errorNeedle = !controller.alarm.errorNeedle;
                
                Wav.stop();
                if (controller.alarm.errorNeedle)
                    Wav.play("alarm.wav");   
            }

         // Changes the state of the error with the pump. It will not deliver more insulin until it is corrected M
            if(input.isKeyMPressed())
            {
                controller.alarm.errorPump = !controller.alarm.errorPump;
                
                Wav.stop();
                if (controller.alarm.errorPump)
                    Wav.play("alarm.wav");   
            }
        }
    }
}