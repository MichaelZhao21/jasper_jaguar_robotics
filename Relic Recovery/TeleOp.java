/*
Yo, ok this is Teleop code. It is working at 90% operational functionality. For relic the function-driver will use 
the triggers to move the torquenado down/up. The buttons will make the relic-plate-servo release and grab. 
POSSIBLE ERRORS: The relic-servo's values might be too extreme, if so just tone them down. If the torquenado does not move with 
the triggers, then check the logic of the statement.
*/

//If there's any problems, blame the engineering team. 

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
/**     ___
 *    1| F |4
 *     |   |
 *    2|_B_|3
 *
 *   Clockwise positive, counterclockwise negative
 */

@TeleOp(name = "TeleOp")

public class MainTeleOp extends LinearOpMode {

    //HardwareClass robot = new HardwareClass();

    public DcMotor Motor1;
    public DcMotor Motor2;
    public DcMotor Motor3;
    public DcMotor Motor4;
    public int var = 0;
    public DcMotor GlyphMotor;
    public DcMotor GlyphMotor2;
    public Servo ServoLeft;
    public Servo ServoRight;
    public DcMotor JARM;
    public Servo Mini;
    public ColorSensor Color;
    public Servo relic1;
    public Servo relic2;
    public Servo relic3;
    public int count =0;
    public boolean relicOn = false;


    public void runOpMode() throws InterruptedException {

        //robot.runHardwareMap(hardwareMap);

        Motor1 = hardwareMap.dcMotor.get("Motor1");
        Motor2 = hardwareMap.dcMotor.get("Motor2");
        Motor3 = hardwareMap.dcMotor.get("Motor3");
        Motor4 = hardwareMap.dcMotor.get("Motor4");
        GlyphMotor = hardwareMap.dcMotor.get("Gmotor");
        GlyphMotor2 = hardwareMap.dcMotor.get("Gmotor2");
        ServoLeft = hardwareMap.servo.get("ServoL");
        ServoRight = hardwareMap.servo.get("ServoR");
        JARM = hardwareMap.dcMotor.get("JARM");
        Mini = hardwareMap.servo.get("Mini");
        Color = hardwareMap.colorSensor.get("JARMcl");
        relic1 = hardwareMap.servo.get("relic1");
        relic2 = hardwareMap.servo.get("relic2");
        relic3 = hardwareMap.servo.get("relic3");
        waitForStart();


        while (opModeIsActive()) {

            //SetUp
            Motor2.setDirection(DcMotorSimple.Direction.REVERSE);

            float M1 = gamepad1.left_stick_y - gamepad1.left_stick_x;
            float M2 = gamepad1.left_stick_y + gamepad1.left_stick_x;




            //Toggle Functionality
            if (gamepad1.a) {
                var = 0;
            } else if (gamepad1.b) {
                var = 1;
            }

            //Fast Mode
            if (var == 1) {
             // Oh Ya M2
                // Oh Ya V1
                tele("Normal Mode: Kelly Abbu Mode");

                if (gamepad1.left_trigger > 0 || gamepad1.right_trigger > 0) {

                    if (gamepad1.left_trigger > 0 && gamepad1.right_trigger == 0) {
                        pivot(gamepad1.left_trigger, 0.8);
                    } else if (gamepad1.left_trigger == 0 && gamepad1.right_trigger > 0) {
                        pivot(-gamepad1.right_trigger, 0.8);

                    }

                } else {
                    Motor1.setPower((M1) * 0.8);
                    Motor2.setPower((M2) * 0.8);
                    Motor3.setPower((-M1) * 0.8);
                    Motor4.setPower((-M2) * 0.8);
                }

                //Slow Mode
            } else if (var == 0) {

                tele("Slow Mode: Madhu Level");

                if (gamepad1.left_trigger > 0 || gamepad1.right_trigger > 0) {

                    if (gamepad1.left_trigger > 0 && gamepad1.right_trigger == 0) {
                        pivot(gamepad1.left_trigger, 0.45);
                    } else if (gamepad1.left_trigger == 0 && gamepad1.right_trigger > 0) {
                        pivot(-gamepad1.right_trigger, 0.45);
                    }

                } else {
                    Motor1.setPower((M1) * 0.45);
                    Motor2.setPower((M2) * 0.45);
                    Motor3.setPower((-M1) * 0.45);
                    Motor4.setPower((-M2) * 0.45);
                }
            }

            //relic code
            if(gamepad2.right_bumper){
                relic3.setPosition(1);
            } else if(gamepad2.left_bumper){
                relic3.setPosition(0);
            } 
            
            if(gamepad2.right_trigger != 0 && gamepad2.left_trigger == 0){
                JARM.setPower(gamepad2.right_trigger);
            } else if(gamepad2.left_trigger != 0 && gamepad2.right_trigger == 0){
                JARM.setPower(-gamepad2.left_trigger);
            } else if(gamepad2.right_trigger == 0 && gamepad2.left_trigger == 0) {
                JARM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }

            //servo code
            if (gamepad2.a) {
                ServoLeft.setPosition(0.45);
                ServoRight.setPosition(0.55);
            } else {
                ServoLeft.setPosition(0.9);
                ServoRight.setPosition(0.1);
            }
            

            //GlyphArm Code
            if (gamepad2.left_stick_y != 0) {
                GlyphMotor.setPower(-gamepad2.left_stick_y * 0.7);
            } else {
                GlyphMotor.setPower(0.08);
            }

            GlyphMotor2.setPower(-gamepad2.right_stick_y);


            //To Be Continued...

        }
    }




   //Pivot function
   public void pivot(float target, double multiple){


        Motor1.setPower(target * multiple);
        Motor2.setPower(target * multiple);
        Motor3.setPower(target * multiple);
        Motor4.setPower(target * multiple);


    }

    //telemtry function
   public void tele(String line){
        telemetry.addLine(line);
        telemetry.update();
    }

}

