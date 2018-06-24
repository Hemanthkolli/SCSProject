package ipump;

import java.awt.*;
import javax.swing.*;
import javax.annotation.Resources;

/**
 * 
 * The output class draws the simulation into the window.
 * Drawing depends on the status of the current program elements.
 * Because of the need for simulation, the elements are positioned absolutely.
 * In realistic surroundings, the images would be positioned by means of relative values
 * or loading the values ​​from the file.
 * 
 */
public class Output extends JComponent {
    private static final long serialVersionUID = 2L;

    public Controller controller;
    
    Image imgBackround;
    Image imgBackgroundOn;
    Image imgBattery;
    Image imgReservoir;    
    Image imgReservoirDose;
    Image imgReservoirDoseSmall;    
    Image imgAlarmRisingSugar;
    Image imgAlarmMissingReservoir;
    Image imgAlarmUsedReservoir;    
    Image imgErrorBackground;
    Image imgErrorSensor;
    Image imgErrorDelivery;
    Image imgErrorPump;
    Image imgErrorNeedle;
    
    Font fontMini;
    Font fontSmall;
    Font font;
    Font fontLarge;
    
    Color darkRed = new Color(150, 50, 50);
    Color grayGreen = new Color(160, 170, 160);
    
    /**
     * Constructor.
     * It loads pictures and fonts.
     * 
     * @param controller Apparatus
     */
    public Output(Controller controller)
    {
        this.controller = controller;
        loadImages();
        loadFont();
    }
    
    /**
     * It loads all the images to be used.
     */
    private void loadImages()
    {
        imgBackround = loadImage("bg.png");
        imgBackgroundOn = loadImage("bgon.png");
        imgBattery = loadImage("bat.png");
        imgReservoir = loadImage("cart.png");
        imgReservoirDose = loadImage("cartdose.png");
        imgReservoirDoseSmall = loadImage("cartdoses.png");
        imgAlarmRisingSugar = loadImage("alarmsugar.png");
        imgAlarmMissingReservoir = loadImage("alarmmissing.png");
        imgAlarmUsedReservoir = loadImage("alarmwrong.png");
        imgErrorBackground = loadImage("error.png");
        imgErrorSensor = loadImage("esensor.png");
        imgErrorDelivery = loadImage("edelive.png");
        imgErrorPump = loadImage("epump.png");
        imgErrorNeedle = loadImage("eneedle.png");
    }
    
    /**
     * It facilitates individual image loading.
     * 
     * @param location location of the image in the resource folder
     * @return Returns the loaded image
     */
    private Image loadImage(String location)
    {
        return Toolkit.getDefaultToolkit().getImage(Resources.class.getResource("/resources/img/" + location));
    }
    
    private void loadFont()
    {        
        // Attempting to load a digital font
        try{
            font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/resources/font/digital.ttf")).deriveFont(Font.PLAIN, 22);
            fontMini = font.deriveFont(Font.PLAIN, 8f);
            fontSmall = font.deriveFont(Font.PLAIN, 12f);
            fontLarge =  font.deriveFont(Font.PLAIN, 52f);
        }
        // If the font can not be loaded, the default serif font is used
        catch (Exception ex)
        {
            fontMini = new Font("serif", Font.PLAIN, 8);
            fontSmall = new Font("serif", Font.PLAIN, 12);
            font = new Font("serif", Font.PLAIN, 22);
            fontLarge = new Font("serif", Font.PLAIN, 52);
        }
    }
    
    /**
     * The method used to draw on the screen.
     * 
     * @param g Graphics allows you to draw in the window component
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        // The background color is greenish to get the look of the old LCDs
        setBackground(grayGreen);
        // Graphics2D is used because we will draw 2D images  
        Graphics2D g2d = (Graphics2D) g;
        // Zoom 4 times to get a pixelized look
        g2d.scale(4, 4);

        // Background
        g2d.drawImage(imgBackround, 0, 0, this);
        
        // It is plotted when the camera is turned on
        if (controller.isOn())
        {
            // Addition to the background image
            g2d.drawImage(imgBackgroundOn, 0, 0, this);
            // Included items
            drawOn(g, g2d);
        }

        // Is drawn when the battery is charging
        // regardless of whether the device is turned on or not
        else if (controller.getBattery().isCharging())
        {
            drawBattery(g2d);
        }
        
        //The process is paused at 16 milliseconds
        fps();
        // The paintComponent method is called as soon as it is completed
        repaint();
    }
    

    /**
     * Draws the screen when the camera is turned on.
     *
     * @param g Graphics allows drawing in the components here for fonts
     * @param g2d Image drawing
     */
    private void drawOn(Graphics g, Graphics2D g2d)
    {
        drawBattery(g2d);
        drawReservior(g2d);
        
        drawDoseIcon(g2d);
        
        drawCriticalErrors(g2d);
        drawOtherErrors(g2d);

        drawText(g, g2d);
    }
    
    

    /**
     * Plotting the battery level.
     * The image of the battery is cut into 5 parts that are arranged horizontally
     * Depending on the current battery capacity, a specific section from the image is loaded.
     *
     * @param g2d Image drawing tools
     */
    private void drawBattery(Graphics2D g2d)
    {
        if (controller.getBatteryPower() >= 80)
        {
            g2d.drawImage(imgBattery, 204, 4, 236, 20, 0, 0, 32, 16, this);
        }
        else if (controller.getBatteryPower() >= 60)
        {
            g2d.drawImage(imgBattery, 204, 4, 236, 20, 0, 16, 32, 32, this);
        }   
        else if (controller.getBatteryPower() >= 40)
        {
            g2d.drawImage(imgBattery, 204, 4, 236, 20, 0, 32, 32, 48, this);
        }
        else if (controller.getBatteryPower() >= 20)
        {
            g2d.drawImage(imgBattery, 204, 4, 236, 20, 0, 48, 32, 64, this);
        }    
        else
        {
            g2d.drawImage(imgBattery, 204, 4, 236, 20, 0, 64, 32, 80, this);
        }
    }
  

    /**
     * Insulin levels remaining in the filling.
     * The image of the charge is cut into 5 parts that are arranged horizontally
     * Depending on the current amount of insulin in the fill, it is loaded
     * a specific section from this image.
     *
     * @param g2d Image drawing tools
     */
    private void drawReservior(Graphics2D g2d) {
        if (controller.getRemaningReservoir() >= 80)
        {
            g2d.drawImage(imgReservoir, 7, 18, 38, 26, 0, 0, 31, 8, this);
        }
        else if (controller.getRemaningReservoir() >= 60)
        {
            g2d.drawImage(imgReservoir, 7, 18, 38, 26, 0, 8, 31, 16, this);
        }   
        else if (controller.getRemaningReservoir() >= 40)
        {
            g2d.drawImage(imgReservoir, 7, 18, 38, 26, 0, 16, 31, 24, this);
        }
        else if (controller.getRemaningReservoir() >= 20)
        {
            g2d.drawImage(imgReservoir, 7, 18, 38, 26, 0, 24, 31, 32, this);
        }    
        else
        {
            g2d.drawImage(imgReservoir, 7, 18, 38, 26, 0, 32, 31, 40, this);
        }
    }
    

    /**
     * Mind to draw icons next to blood measurement
     * to indicate that the dose of insulin was then obtained.
     *
     * @param g2d Image drawing tools
     */
    private void drawDoseIcon(Graphics2D g2d) {
    	// Dose icon next to the main level of blood sugar
        if (controller.getSugarLevelDose(0))
        {
            g2d.drawImage(imgReservoirDose, 89, 38, this);
        }
        
     // Dose icon next to past blood sugar levels
        if (controller.getSugarLevelDose(1))
        {
            g2d.drawImage(imgReservoirDoseSmall, 184, 35, this);
        }
        
        if (controller.getSugarLevelDose(2))
        {
            g2d.drawImage(imgReservoirDoseSmall, 184, 56, this);
        }
    }
    

    /**
     * Draws maps for critical errors
     * which prevent the normal functioning of the appliance.
     *
     * @param g2d Image drawing tools
     */
    private void drawCriticalErrors(Graphics2D g2d) {
        if (controller.alarm.errorPump || controller.alarm.errorDelivery ||
            controller.alarm.errorSensor || controller.alarm.errorNeedle)
        {
            g2d.drawImage(imgErrorBackground, 194, 85, this);
            if (controller.alarm.errorPump)
            {
                g2d.drawImage(imgErrorPump, 197, 94, this);
            }
            
            if (controller.alarm.errorDelivery)
            {
                g2d.drawImage(imgErrorDelivery, 197, 103, this);
            }
            
            if (controller.alarm.errorSensor)
            {
                g2d.drawImage(imgErrorSensor, 197, 112, this);
            }
            
            if (controller.alarm.errorNeedle)
            {
                g2d.drawImage(imgErrorNeedle, 197, 121, this);
            }
        }
    }


    /**
     * Mute to extract additional errors like
     * increased blood sugar, lack of insulin loading,
     * or old filling of insulin.
     *
     * @param g2d Image drawing tools
     */
    private void drawOtherErrors(Graphics2D g2d) {
        if (controller.alarm.sugarRising)
        {
            g2d.drawImage(imgAlarmRisingSugar, 83, 100, this);    
        }
                
        if (controller.alarm.missingReservoir)
        {
            g2d.drawImage(imgAlarmMissingReservoir, 120, 100, this);    
        }
        
        if (controller.alarm.incorrectReservoir)
        {
            g2d.drawImage(imgAlarmUsedReservoir, 157, 100, this);    
        }
    }


    /**
     * Print all Text elements on the interface.
     *
     * @param g Graphics allows drawing in the components here for fonts
     * @param g2d Image drawing
     */
    private void drawText(Graphics g, Graphics2D g2d) {
    	// Print time
        g.setFont(font);
        g2d.setColor(Color.BLACK);

        // If less than 10 minutes are added to zero in the text to make it look nice
        if (Clock.getTotalTime()%60 < 10)
        {
            g2d.drawString(Clock.getTotalTime()/60 + " : 0" + Clock.getTotalTime()%60, 98, 17);
        }
        else 
        {
            g2d.drawString(Clock.getTotalTime()/60 + " : " + Clock.getTotalTime()%60, 98, 17);
        }
        
        
        // If it is over noon and has passed more than 20 minutes
        // pm the character should be positioned more to the right so as not to be covered by numbers
        g.setFont(fontSmall);
        if (Clock.getTotalTime()/60 >= 12)
        {
            if (Clock.getTotalTime()/60 >= 20)
            {
                g2d.drawString("pm", 156, 17);
            } else {
                g2d.drawString("pm", 148, 17);       
            }
        } else {
            g2d.drawString("am", 150, 17);
        }
        
        // Print the current blood sugar level
        // When the appliance is turned on, the initial values ​​are 0.0,
        // therefore we will not paint the values ​​with red if they are zero or less
        g.setFont(fontLarge);
        g2d.setColor(Color.BLACK);
        if((controller.getSugarLevel(0) > 0) &&
                (controller.getSugarLevel(0) < 75 || controller.getSugarLevel(0) > 135))
        {
            g2d.setColor(darkRed);
        }
        if (Main.mg)
        {
            g2d.drawString("" + controller.getSugarLevel(0), 100, 70);
        } else {
            g2d.drawString("" + (float)Math.round(controller.getSugarLevel(0) * 0.0555 * 10) / 10, 100, 70);
        }
        
     // Print a measuring unit of blood sugar
        g.setFont(fontMini);
        if(Main.mg)
        {
            g2d.drawString("mg", 165, 62);
            g2d.drawString("__", 165, 64);
            g2d.drawString("dl", 165, 70);
        } else{
            g2d.drawString("mmol", 163, 62);
            g2d.drawString("__", 165, 64);
            g2d.drawString(" l", 165, 70);
        }
        
        g.setFont(font);
        
        // Print in value for previous measurement of blood sugar,
        // as well as in which measuring units are expressed values.
        // When the appliance is turned on, the initial values ​​are 0.0,
        // therefore we will not paint the values ​​with red if they are zero or less
        g2d.setColor(Color.BLACK);
        // If blood sugar level is too high or low, it will be marked with red
        if((controller.getSugarLevel(1) > 0) &&
                (controller.getSugarLevel(1) < 75 || controller.getSugarLevel(1) > 135))
        {
            g2d.setColor(darkRed);
        }
        if (Main.mg)
        {
            g2d.drawString("" + controller.getSugarLevel(1), 190, 50);
        } else {
        	// Conversion between mg / dl and mmol / l is done by multiplying by 0.555
        	// The 10-way and 10-by-10 is done to round the number to one digit after the comma
            g2d.drawString("" + (float)Math.round(controller.getSugarLevel(1) * 0.0555 * 10) / 10, 190, 50);
        }
        g.setFont(font);
        
        
        // Print in measurement values ​​before the previous measurement of blood sugar,
        // as well as in which measuring units are expressed values.
        g2d.setColor(Color.BLACK);

     // If blood sugar level is too high or low, it will be marked with red
        if((controller.getSugarLevel(2) > 0) &&
                (controller.getSugarLevel(2) < 75 || controller.getSugarLevel(2) > 135))
        {
            g2d.setColor(darkRed);
        }
        if (Main.mg)
        {
            g2d.drawString("" + controller.getSugarLevel(2), 190, 70);
        } else {
        	// Conversion between mg / dl and mmol / l is done by multiplying by 0.555
            // The 10-way and 10-by-10 is done to round the number to one digit after the comma
            g2d.drawString("" + (float)Math.round(controller.getSugarLevel(2) * 0.0555 * 10) / 10, 190, 70);
        }

        // Prints the remaining insulin in the device
        g2d.setColor(Color.BLACK);
        g.setFont(font);
        // If there is 0 charge then it is marked in red.
        if (controller.getRemaningReservoir() == 0)
        {
            g2d.setColor(darkRed);    
        }
        g2d.drawString("" + controller.getRemaningReservoir(), 10, 16);

        
        // Print for daily maximum daily dose of insulin.
        g2d.setColor(Color.BLACK);
        if (controller.getTotalDailyDose() >= controller.getMaxDailyDose())
        {
            g2d.setColor(darkRed);
        }
        g2d.drawString("" + controller.getTotalDailyDose() + "/" + controller.getMaxDailyDose(), 12, 110);
        
        

        // Print at the time the previous dose of insulin was obtained.
        g2d.setColor(Color.BLACK);

        // Time of previous dose
        if (controller.getTimePreviousDose()%60 < 10)
        {
            g2d.drawString(" - " +
                controller.getTimePreviousDose()/60 + ":0" + controller.getTimePreviousDose()%60,
                15, 62);
        }
        else 
        {
            g2d.drawString(" - " +
                controller.getTimePreviousDose()/60 + ":" + controller.getTimePreviousDose()%60,
                15, 62);
        }
        

        // Print for how many doses were in previous insulin injection
        g2d.drawString("" + controller.getLastDose(), 5, 62);
        
        
        
     // Print static texts
        g.setFont(fontSmall);
        g2d.drawString("Sugar level" , 98 , 84);
        
        g2d.drawString("Previous dose" , 4, 76);
        
        g.setFont(fontMini);
        g2d.drawString("Past levels" , 182, 80);
        
        g2d.drawString("Daily dose" , 16, 122);
    }
    	

    /**
     * Means to limit the boundaries to stand at 16 milliseconds,
     * giving a little time to the CPU for other things.
     * Required due to the complexity of the simulation,
     * Because each frame draws all the images and changes the logic at the same time.
     */
    private void fps(){
        try{
            Thread.sleep(16);
        }catch(Exception exp){
        }
    }
}
