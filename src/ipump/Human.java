package ipump;

/**
 * The human class serves to emulate the functions of a person in order to test
 * Blood sugar regulator.
 * 
 */
public class Human {
    private float sugarLevel;
    private float sugarNew;
    private final float SUGAR_MODIFIER = 0.02f;
    private float sugarMaxSpeed;
    
    private float insulin;
    private final float INSULIN_MODIFIER = 0.03f;
    private float insulinMaxSpeed;
    
    private final int SUGAR_LEVEL_MIN = 10;
    private final int SUGAR_LEVEL_MAX = 400;

    /**
     * Constructor.
     */
    public Human()
    {
            sugarLevel = 100;
    }

    /**
     * Calls at equal intervals to emulate human functions.
     */
    public void update()
    {
        sugar();
        insulin();

        // Limits that blood sugar levels can not exceed the maximum and fall below the minimum
        if (sugarLevel < SUGAR_LEVEL_MIN)
        {
            sugarLevel = SUGAR_LEVEL_MIN;
        }
        else if (sugarLevel > SUGAR_LEVEL_MAX)
        {
            sugarLevel = SUGAR_LEVEL_MAX;
        }
    }
    
    /**
     * Calculation of blood sugar.
     */
    private void sugar()
    {
        // It raises blood sugar when the maximum sugar increase rate is greater than 0
        // and when the negative value of the new sugar is lower than the maximum rate of increase in sugar
        if (sugarMaxSpeed > 0 && -sugarNew < sugarMaxSpeed)
        {
            // It raises blood sugar when a new amount of sugar comes in
            // The difference between maximum and new sugar ranges from 0 and increases
            // In the end, it decays to get a parabola.
            sugarLevel += sugarMaxSpeed - Math.abs(sugarNew);
            // SugarModifier serves to determine the amount of new sugar.
            sugarNew -= SUGAR_MODIFIER;
        }    
        // When there is no new blood sugar, then sugarMaxSpeed ​​and sugarNew are placed at zero
        // so as not to adversely affect the blood sugar
        else
        {
            sugarMaxSpeed = 0;
            sugarNew = 0;
        }
    }

    /**
     * Insulin calculation.
     */
    private void insulin()
    {
    	// Natural insulin
        // The value is such that it takes at least 30 minutes to get down to 1mg in a decilitre of blood
        // (for testing when it stops falling and in an uncertain zone it is)
        sugarLevel -= 0.032f;

        // Reduces blood sugar when new insulin comes in
        // The difference between maximum and new insulin ranges from 0 and increases
        // In the end, it decays to get a parabola.
        if (insulinMaxSpeed > 0 && -insulin < insulinMaxSpeed)
        {
        // Reduces blood sugar when a new amount of sugar comes in
        sugarLevel -= insulinMaxSpeed - Math.abs(insulin);
        // INSULIN_MODIFIER serves to determine the amount of new insulin.
        // initially increases due to sudden insulin delivery and then decreases with the above formula
        insulin -= INSULIN_MODIFIER;
        }
        // When there is no insulin in the blood then insulinMaxSpeed ​​and insulin are placed at zero
        // how not to increase the amount of sugar in the blood stream
        else
        {
            insulinMaxSpeed = 0;
            insulin = 0;
        }
    }

    /**
	 * Meal
     * Increases sugar levels.
     */
    public void eat()
    {
        sugarMaxSpeed = 0.75f;
        sugarNew = sugarMaxSpeed;
    }

    /**
	 * Large meal
     * Increases sugar levels.
     */
    public void eatBig()
    {
        sugarMaxSpeed = 0.9f;
        sugarNew = sugarMaxSpeed;
    }

    /**
	 * Accepts injected insulin dose.
     * @param doše Ubrizgan insulin
     */
    public void setInsulin(int dose) {
            insulinMaxSpeed = (float) dose / 2;
            insulin = insulinMaxSpeed;
    }
    
    /**
	 * Blood sugar level.
     * @return sugar level
     */
    public int getSugarLevel() {
            return (int)sugarLevel;
    }
}
