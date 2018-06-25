package ipump;

 //Alert class alert for warnings that may occur when the appliance is inoperative.
 //This class contains only public promises. Because the simulation of this class can be accessed by anyone
 //and then test the system when some mistake happened during the execution.
 //In the real system this would be split into several classes where each one has a sensor to determine when a failure occurred.
 
 //@author Blessy Rajamani
 
public class Alarm {
    // The percentage of secretion in blood is too low
    public boolean lowBloodSugar = false;
    // The percentage of secretion in blood is too high
    public boolean highBloodSugar = false;
    //Insulin reservoir is not inserted
    public boolean missingReservoir = false;
    //has already used the insulin reservoir
    public boolean incorrectReservoir = false;
    // The insulin reservoir is empty
    public boolean emptyReservoir = false;
    // The battery is low
    public boolean lowBattery = false;
    // The appliance feeds the maximum daily dose.
    public boolean maxDose = false;
    // Current blood sugar level is on the rise
    public boolean sugarRising = false;

    // Error with pump
    public boolean errorPump = false;
    // Error with delivery of insulin
    public boolean errorDelivery = false;
    // Error in the sensor
    public boolean errorSensor = false;
    // Error with needle
    public boolean errorNeedle = false;
}
