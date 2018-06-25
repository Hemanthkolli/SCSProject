package ipump;

/**
 *The Battery class is dedicated to the battery in the appliance.
 * It is charged by connecting the appliance with the power.
 * It is emptied when the appliance is switched on * as well as when the appliance uses a sensor or injector.
 * 
 * @author Blessy Rajamani
 */
public class Battery {
    private float power;
    private final int MAX_POWER = 100;
    private boolean charging = false;

    /**
     * Constructor
     */
    public Battery()
    {
            power = MAX_POWER;
    }

    /**
     * Updates battery values.
     * Called every minute.
     *
     * @param deviceOn Whether the blood cell level device is switched on
     */
    public void update(boolean deviceOn)
    {
    // If the battery is charged, the battery level increases at constant speed
        if (charging)
        {
            power += 0.5f;
            if (power > MAX_POWER)
            {
                power = MAX_POWER;
            }
        }
    // If the controller is on then the battery level decreases at constant speed.
        else if (deviceOn)
        {
            power -= 0.05f;
        }
    }

    //In case the appliance is not charged
    //The sensor lowers the battery for a certain amount.
     
    public void sensor()
    {
        if (!charging)
        {
            power -= 0.2f;
        }
    }

    //In case the appliance is not charged
    //The amount of injected insulin reduces the battery for a certain amount.
    //@param dose How many doses of insulin are prominent.

    public void doseInection(int dose)
    {
        if (!charging)
        {
            power -= 0.2f * dose;
        }
    }


    //Returns the battery level.
     
    //@return Battery level

    public int getPower()
    {
            return (int) power;
    }

    //The camera is charged and the battery is charged.
     
    public void charging()
    {
        charging = true;
    }

    //The battery in the camera has stopped charging.
     
    public void chargingOff()
    {
        charging = false;
    }

    //Returns the value of recharging the battery.
    //@return Is the battery charged in the appliance
    
    public boolean isCharging()
    {
        return charging;
    }
}
