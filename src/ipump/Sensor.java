package ipump;

/**
 * The sensor class serves only as an agent between the user and the controller.
 * Analyzes the level of blood sugar in the user's blood.
 * In the right system, this class would work to calculate the blood sugar level based on the measurement.
 * 
 */
public class Sensor {
    private Human user;

    /**
     *Constructor
     */
    public Sensor(Human user)
    {
            this.user = user;
    }

    /**
     * Gives a level of sugar that is high.
     * @return the level of sugar recorded in the user
     */
    public int getSugarLevel()
    {
            return user.getSugarLevel();
    }
}
