package ipump;


 //Class Clock
 //Called during each frame.
 //All methods in this class are static because there is no point to be more than one instance of this class.
 //Also because this class is related to program time, static
 //It makes it possible to take it from anywhere.
 
 // @author Blessy Rajamani
 
     
public class Clock {
    private static float minute = 0;
    // Totally gone time in minutes
    private static long totalMinutes = 0;
    // Speed of simulation
    private static float speed;
    // Initial time in milliseconds
    private static double startTime = System.currentTimeMillis();
    // Past time since the last call
    private static float deltaTime;

    //Update method counts past time.Calculates how much minutes have passed.speed = 1 takes 1 second.Whatever each senkunde will pass 1 minute simulation@return returns true when it runs 1 minute simulation
    //
    public static boolean update()
    {
        // Past time of the last call in seconds
        deltaTime = (float)(System.currentTimeMillis() - startTime) / 1000;
        // New milisecond time
        startTime = System.currentTimeMillis();

        // Each frame of a changeable minute is taken away for the past time
        minute -= deltaTime;
        // When the variable is smaller or equal to zero then it is considered that it lasted for a minute

        if (minute <= 0) {
            // The variable minute is reset to the new duration calculated at speed

            minute += 1/speed;
            // The total number of minutes is increased for one.

            ++totalMinutes;
            // He's been out for a minute            return true;
        }
        // It did not last for a minute
        return false;
    }

    //Totally past simulation time.@return total time in simulated minutes.//
    
    public static long getTotalTime() {
        return totalMinutes;
    }

    // He calculates if he has passed away.If the forward call of this method was less than 1 day in minutes @return Has he passed away//
    
    public static boolean midnightPassed()
    {
        if(totalMinutes > 1440){
            // The number of minutes per day is subtracted to return the minute for one day.
            // This does not affect only time in the class.
            totalMinutes -= 1440;
            return true;
        }	
        return false;
    }

    // Gives the new clock speed class for a minute calculation.
     
    //@param speedNew New speed
    static void setSpeed(int speedNew) {
        speed = speedNew;
    }

   

     // Clock speed.
     
     //@return Clock speed
     
    static float getSpeed() {
        return speed;
    }
}
