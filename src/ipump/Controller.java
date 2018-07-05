package ipump;

/**
* The Controller class measures blood sugar and provides the required amount of insulin.
* Measurements are done with the sensor and the injection is done with an injector.
 */
public class Controller {
    public Alarm alarm;
    private Sensor sensor;
    private Injector injector;
    private Battery battery;

    // Condition of the appliance
    private boolean on = false;

    // Blood Sugar level
    private int sugarLevel;
    private int sugarLevelPast;
    private int sugarLevelPast2;

    // Reservoir
    private final int NEW_RESERVOIR = 100;
    private int currentReservoir;
    private boolean reservoirIn;
    
    // Has the user requested a dose of insulin?
    private boolean manualPump;

    // Time past the dose
    private long timeDose;
    // Was the dose given then
    private boolean sugarLevelDose;
    private boolean sugarLevelPastDose;
    private boolean sugarLevelPast2Dose;

    private int currentDose;        
    // Let's write down the screen that is the last recorded dose
    private int lastInjectedDose;

    // Minimum dose
    private final int DOSE_MIN = 1;
    // Maximum dose
    private final int DOSE_MAX = 4;
    // Maximum daily dose
    private final int DOSE_DAILY_MAX = 25;
    // Current daily dose
    private int currentDailyDose;
    
    private final int REFRESH_RATE = 10;
    private int minute = 0;

    // Safe Zone of Blood Sugar mg / dl
    private final int SAFE_ZONE_MIN = 75;
    private final int SAFE_ZONE_MAX = 135;

    /**
     * Constructor.
     * Sets the intrinsic value.
     * 
     * @param user is forwarded to the sensor and the injector
     */
    public Controller(Human user)
    {
        sensor = new Sensor(user);
        injector = new Injector(user);
        alarm = new Alarm();
        battery =  new Battery();

        currentDose = 0;
        lastInjectedDose = 0;

        // A new reservoir is being placed
        currentReservoir = NEW_RESERVOIR;
        reservoirIn = true;

        // It is waiting for the user to activate the manual mode
        manualPump = false;

        resetAccumulativeDose();
    }

    /**
	 * Resets the daily dose of insulin.
     * If the alarm has been switched on for the maximum daily dose of the insulin it will go off
     */
    private void resetAccumulativeDose()
    {
        currentDailyDose = 0;
        alarm.maxDose = false;
    }

    /**
     * Update method.
     * Called once every minute
     * As long as it has batteries
     */
    public void update()
    {
        // If it is overnight, the daily dose is reset
        if(Clock.midnightPassed())
        {
            resetAccumulativeDose();
        }
                    
        battery.update(on);        
        
        // If the device does not turn on, it exits the method.
        if (!on)
            return;
        
        // If the device is on and the minute is divisible by 10, the controller is updated
        minute --;
        if (minute <= 0)
        {
            minute += REFRESH_RATE;
            
            // Radiates normally while the battery has more than 5%
            if(battery.getPower() > 5)
            {
                // The sensor works independently of whether there is a dose of insulin
				// When the sensor does not rupture then it will not be able to calculate the amount of secretion in the blood
                if (!alarm.errorSensor)
                {
                    activateSensor();
                    battery.sensor();

                    // Only when the sensor is operating and there is no damaged pump needle or
                    // Insulin delivery will test the required amount of isuline.
                    if (!alarm.errorDelivery && !alarm.errorPump && !alarm.errorNeedle)
                    {
                        // There should be a tank in the appliance
						// and it has a dose over 0 to inject insulin at all
                        if (reservoirIn && currentReservoir > 0)
                        {
                            // The values of the required dose of insulin are obtained
                            currentDose = computeDose();

                            // Only insulin is injected if the current dose is greater than 0
							// or if the user requested the insulin
                            if (currentDose > 0 || manualPump)
                            {
                                administerInsulin(currentDose);
                            }	
                        }
                    }
                }
            }
            // When the battery drops to 5% or less then the system will go out
			// until the battery is fully charged
            else
            {
                on = false;
                Wav.stop();
            }
        }
        
        // If the battery is below 15%, note that the battery is low
        if (battery.getPower() < 15)
        {
            alarm.lowBattery = true;
        }
        else
        {
            alarm.lowBattery = true;
        }
    }


	/**
      * Calculates the required dose of insulin.
      * If the current secretory level below the maximum safe zone then the dose is 0.
      * If the current secretion level is less than the current or current level slower then the dose is 0.
      * Otherwise, the required dose of insulin is calculated.
      *
      * In case the estimated value is less than the minimum and the maximum of the maximum
      * then set to minimum or maximum dose.
      *
      * @return Returns the calculated dose of insulin you should get a user
      */
    private int computeDose()
    {
        int compDose;

        // Safe Zone of Sugar
        if (sugarLevel <= SAFE_ZONE_MAX)
        {
            compDose = 0;
        }

        // The level of sugar is growing slower than in the previous step
        // or the sugar level drops
        else if (sugarLevel < sugarLevelPast || (sugarLevel - sugarLevelPast) < (sugarLevelPast - sugarLevelPast2))
        {
            compDose = 0;
        }
        // The level of secera grows or grows faster than in the previous step
        // (sugarLevel> = sugarLevelPast || (sugarLevel - sugarLevelPast)> = (sugarLevelPast - sugarLevelPast2)) // REDUNDANT BY SPECIFICATION
        // Because we have in the pre-order ifu determined that one of the cases when it falls will include all the remaining cases it was
        // The case when it grows or is equal when it is not within normal limits and when it grows faster or equal to the previous step.
        else 
        {
            // dose calculation
            compDose = ((sugarLevel - sugarLevelPast) / 3); // Radi preciznije simlacije
            
            // If less than the minimum then set to the minimum dose
            if ( compDose < DOSE_MIN )
            {
                compDose = DOSE_MIN ;
            }
            // If the dose is greater than the maximum then set to maximum
            else if ( compDose > DOSE_MAX )
            {
                compDose = DOSE_MAX ;
            }
        }

        return compDose;
    }

	/**
      * This method gives the insulin user a budget.
      * Calculate the battery and set the values for when the dose was given.
      *
      * If the current dose is greater than the maximum daily dose then an alarm is triggered
      * and the user does not get insulin unless he is asked for the manual button.
      *
      * If the reservoir has less than the requested dose, the remaining amount of the user is given.
      *
      * The end of the test is verified whether the tank is empty. If you then have an alarm on it.
      * Lastly, manually set the manualPump to false.
      *
      * Parameter dose Insulin dose to be given to the user
      */
    private void administerInsulin(int dose)
    {
        // If the current dose is greater than the maximum daily dose then turn on the alarm
        if (currentDailyDose >= DOSE_DAILY_MAX && !manualPump)
        {
                alarm.maxDose = true;
        }
        // If manual mode is injected, injecting insulin does not matter if the maximum dose is exceeded
        else
        {
		     // If the manual mode is turned on and the amount of insulin is less than or equal to 0 then the minimum dose is reached.
             // In case the system automatically spelled out a larger amount
             // insulin this amount will be used.
            if (manualPump)
            {
                if (dose <= 0)
                {
                    dose = DOSE_MIN;
                }
            }
			 // If it is not manual mode and the dose will exceed the maximum daily dose
             // Then the dose is reduced to be the remaining number of daily doses
            else if (currentDailyDose + dose > DOSE_DAILY_MAX)
            {
                dose = DOSE_DAILY_MAX - currentDailyDose;
            }
                
            // If the current dose is greater than the maximum daily dose then turn on the alarm
            if (currentDailyDose + dose >= DOSE_DAILY_MAX)
            {
                alarm.maxDose = true;
            }

			// If the dose requires more insulin than in the reservoir
			// then take whatever is left in the reservoir
            if (currentReservoir - dose <= 0)
            {
                dose = currentReservoir;
                currentReservoir = 0;
                alarm.emptyReservoir = true;
            } else {
                currentReservoir -= dose;
            }
            
            // Inject the dose
            injector.inject(dose);
            
            // Confirms that the dose was received at that time
            sugarLevelDose = true;
            // Enter dose time
            timeDose = Clock.getTotalTime();
            
            // Increases the daily number of given doses of that day
            currentDailyDose += dose;
            // The amount of last dose
            lastInjectedDose = dose;
            // Reduces the battery
            battery.doseInection(dose);
            // The manual system is switched off
            manualPump = false;
        }
    }
    
	/**
      * The sensor takes the value of the secretion in the blood of the user.
      * Old values are updated with new ones.
      * Promiscuous people are being enrolled to see if the insulin dose is injected in the long run.
      */
    private void activateSensor()
    {          
        sugarLevelPast2 = sugarLevelPast;
        sugarLevelPast = sugarLevel;
        sugarLevel = sensor.getSugarLevel();

        sugarLevelPast2Dose = sugarLevelPastDose;
        sugarLevelPastDose = sugarLevelDose;
        sugarLevelDose = false;
        
        sugarLevelAlarm();
        sugarLevelRising();
    }
    
    /**
     * Includes and deactivates alarms associated with blood sugar levels.
     */
    private void sugarLevelAlarm()
    {
        if (sugarLevel < SAFE_ZONE_MIN)
        {
                alarm.lowBloodSugar = true;
                alarm.highBloodSugar = false;
                Wav.play("alarm.wav");
        }
        else if (sugarLevel > SAFE_ZONE_MAX)
        {
                alarm.lowBloodSugar = false;
                alarm.highBloodSugar = true;
                Wav.play("alarm.wav");
        } else {
                Wav.stop();
        }
    }
    
    /**
	 * Checks whether the current blood sugar levels are increasing.
	 * If increased, the alarm is activated.
     */
    private void sugarLevelRising()
    {
        if (sugarLevel > sugarLevelPast)
        {
            alarm.sugarRising = true;
        }
        else
        {
            alarm.sugarRising = false;
        }
    }

    
    /**
	 * Removing the reservoir.
	 * The number of current insulin in the reservoir is reduced to zero.
	 * An alarm is set to not insert the insulin reservoir.
     */
    public void reservoirRemoved()
    {
        if (reservoirIn)
        {
            currentReservoir = 0;
            reservoirIn = false;
            alarm.missingReservoir = true;
            alarm.incorrectReservoir = false;
            Wav.play("beep.wav");   
        }
    }

    /**
	  * Can only put a new tank if there is no current.
      *
      * The assumption according to the z model specification is that the newly inserted reservoir will be charged to the maximum.
      * If there is a possibility of arbitrary charging, there should be a sensor activated when plugging in, so that it can calculate the number of doses in advance.
      * The error in the calculation of the number of doses would lead to the injection of a non-existent amount of insulin.
      * This is reset by means of a part of a tank that is removed when it is attached to the appliance. When the plug-in is used, the appliance will not use it independently of the amount in it.
      *
      * If the tank is not new then the alarm is triggered to be inaccurate but at the same time an alarm is triggered to warn that the tank is missing.
      *
      * If the tank is new, then the current tank is reset, and the alarms are connected to the tank.
      *
      * @param seal Is it a new tank
     */
    public void replaceCartridge(boolean seal)
    {
        if (reservoirIn == false)
        {
            if (seal)
            {
                alarm.incorrectReservoir = false;
                currentReservoir = NEW_RESERVOIR;
                reservoirIn = true;
                alarm.missingReservoir = false;
                Wav.play("beep.wav");
            } else {
                reservoirIn = true;
                alarm.incorrectReservoir = true;
                alarm.missingReservoir = false;
                Wav.play("beep.wav");   
            }
        }
    }

//------------------------------------------------------------------------------
// Getting Setter
    
    /**
     * Activates the appliance to take one dose.
     */
    public void manualInject() {
        manualPump = true;
    }
    
    /**
	 * Turns the appliance on and off.
     * Ensures that blood analysis is performed immediately as soon as it is triggered.
     */
    public void power() {
        on = !on;
        
        minute = 0;
    }
    
    /**
	 * Getter that checks whether the camera is on.
     *
     * @return Is the camera turned on?
     */
    public boolean isOn() {
        return on;
    }
    
    /**
     * Getter for the remaining doses in the reservoir.
     *
     * @return Remaining doses in the reservoir
     */
    public int getRemaningReservoir() {
        return currentReservoir;
    }

    /**
	 * Getter for the level of blood sugar at a certain measurement.
     *
     * @param and age of blood sugar level measurement
     * @return Blood sugar level on this measurement
     */
    public int getSugarLevel(int i) {
        switch(i)
        {
            case 0: return sugarLevel;
            case 1: return sugarLevelPast;
            case 2: return sugarLevelPast2;
            default: return sugarLevel;
        }
    }
    
    /**
	 * Getter for whether a dose is taken at a certain measurement.
     *
     * @param and age of blood sugar level measurement
     * @return Dose taken on this measurement
     */
    public boolean getSugarLevelDose(int i) {
        switch(i)
        {
            case 0: return sugarLevelDose;
            case 1: return sugarLevelPastDose;
            case 2: return sugarLevelPast2Dose;
            default: return sugarLevelDose;
        }
    }
    /**
	 * Getter for total routine daily dose.
	 * 
	 * @return Take the daily dose
     */
    public int getTotalDailyDose() {
        return currentDailyDose;
    }
    
    /**
	 * Getter for late last time.
     *
     * @return Prospects your daily dose in minutes
     */
    public int getTimePreviousDose() {
        return (int)timeDose;
    }
    
    /**
	 * Getter for the amount of last dose taken
     *
     * @return The amount of last dose taken
     */
    public int getLastDose() {
        return lastInjectedDose;
    }

    /**
	 * Getter for maximum daily dose.
	 *
	 * @return Maximum daily dose
     */
    public int getMaxDailyDose() {
        return DOSE_DAILY_MAX;
    }
    
    /**
	 * Is the tank installed?
     *
     * @return Is a tank installed?
     */
    public boolean isReservoirIn() {
        return reservoirIn;
    }
    
    /**
	 * Is an unmarked reservoir installed.
     *
     * @return Is an unmarked reservoir installed
     */
    public boolean isReservoirCorret() {
        return alarm.incorrectReservoir;
    }
    
    /**
	 * Getter for battery level.
	 *
	 * @return
     */
    public int getBatteryPower() {
        return battery.getPower();
    }
    
    /**
	 * Getter for the battery
	 *
	 * @return Battery
     */
    public Battery getBattery() {
        return battery;
    }
}
