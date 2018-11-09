package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

@Autonomous(name="FinalAuto")
public class FinalAuto extends LinearOpMode{

    private static final String VUFORIA_KEY = "AW43gwP/////AAAAmYhyz/zuEEVHnvzoxHlLyZItf4ilP0/dinBMnTUxXLYeVNLMHQmuS0m+8deBPobAQUB6JXl9rH3l3VC6eJQdYCL7ucXcYRzIaySgu5Edw18foo+xbQpFci4D7t/gEPkx5bkW8OsMCN8oaHnjJfDsm2yuE7YGWzmDs4NRIi929mQxrBk7BFxhDpDV97bGssofJZ16mCAaBgeIj+IUtW2RfZZ9QNOQRs0l0Nlf6vaFtI8/alOhJPjwpQc9ZXmyjF8Yc83mSOKLW8ei3UsYTzrAlZtYeHPiG4FHuGx6t/OCuN5z3V4sw06bvt7Hi9eYa2MivKl8GXlKppNt6kUPHRNFTVz11vboZTYAAAzafNiXyfNj";
    private static final float mmPerInch        = 25.4f;
    private static final float mmFTCFieldWidth  = (12*6) * mmPerInch;       // the width of the FTC field (from the center point to the outer panels)
    private static final float mmTargetHeight   = (6) * mmPerInch;          // the height of the center of the target image above the floor
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    private OpenGLMatrix lastLocation = null;
    private boolean targetVisible = false;
    private VuforiaLocalizer vuforia;
    private List<VuforiaTrackable> allTrackables;
    private String currentTarget;
    private ArrayList<Float> pos = new ArrayList<Float>();

    private DcMotor Motor0;
    private DcMotor Motor1;
    private DcMotor Motor2;
    private DcMotor Motor3;
    private DcMotor LiftMotor;
    private Servo MarkerServo;
    private ColorSensor Picker;

    @Override
    public void runOpMode() throws InterruptedException{

        Motor0 = hardwareMap.dcMotor.get("Motor0");
        Motor1 = hardwareMap.dcMotor.get("Motor1");
        Motor2 = hardwareMap.dcMotor.get("Motor2");
        Motor3 = hardwareMap.dcMotor.get("Motor3");
        Motor3.setDirection(DcMotor.Direction.REVERSE);
        LiftMotor = hardwareMap.dcMotor.get("LiftMotor");
        MarkerServo = hardwareMap.servo.get("MarkerServo");
        MarkerServo.setPosition(0);

        float heading = 0;
        String side;

        vuforiaSetup();

        waitForStart();

        start();

        //land right wheels from lander
        LiftMotor.setPower(-.7);
        sleep(4750);
        LiftMotor.setPower(0);

        //move holding peg away from hook
        move(0.2,0, -1, 0);
        sleep(800);
        stopMove();
        move(.2,1,0,0);
        sleep(200);
        stopMove();

        //lower the lift arm
        LiftMotor.setPower(.7);
        sleep(3500);
        LiftMotor.setPower(0);

        //pivot to face vuforia
        move(.3, 0,0,1);
        int count = 0;
        getPos();
        while(Math.abs(heading) > 10 || count < 10000) {
            getPos();
            if (pos.size() == 0) {
                heading = 100;
            }
            else {
                heading = pos.get(5);
            }
            count++;
        }
        stopMove();

        //switches between the 2 autos based on the target shown TODO: MAKE THIS!!!
        getPos();
        if (currentTarget.equals("Front-Craters") || currentTarget.equals("Back-Space")) {
            //Depot Auto


        }
        else {
            //Crater Auto
        }


    }

    /**
     *
     * Sets up vuforia and openGLMatrix
     * It will add all the VuforiaTrackables to the allTrackables List
     *
     */
    private void vuforiaSetup() {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = VUFORIA_KEY ;
        parameters.cameraDirection   = CAMERA_CHOICE;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        VuforiaTrackables targetsRoverRuckus = this.vuforia.loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackable blueRover = targetsRoverRuckus.get(0);
        blueRover.setName("Blue-Rover");
        VuforiaTrackable redFootprint = targetsRoverRuckus.get(1);
        redFootprint.setName("Red-Footprint");
        VuforiaTrackable frontCraters = targetsRoverRuckus.get(2);
        frontCraters.setName("Front-Craters");
        VuforiaTrackable backSpace = targetsRoverRuckus.get(3);
        backSpace.setName("Back-Space");

        allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsRoverRuckus);

        OpenGLMatrix blueRoverLocationOnField = OpenGLMatrix
                .translation(0, mmFTCFieldWidth, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0));
        blueRover.setLocation(blueRoverLocationOnField);
        OpenGLMatrix redFootprintLocationOnField = OpenGLMatrix
                .translation(0, -mmFTCFieldWidth, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180));
        redFootprint.setLocation(redFootprintLocationOnField);
        OpenGLMatrix frontCratersLocationOnField = OpenGLMatrix
                .translation(-mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90));
        frontCraters.setLocation(frontCratersLocationOnField);
        OpenGLMatrix backSpaceLocationOnField = OpenGLMatrix
                .translation(mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90));
        backSpace.setLocation(backSpaceLocationOnField);

        final int CAMERA_FORWARD_DISPLACEMENT  = 110;   // eg: Camera is 110 mm in front of robot center
        final int CAMERA_VERTICAL_DISPLACEMENT = 200;   // eg: Camera is 200 mm above ground
        final int CAMERA_LEFT_DISPLACEMENT     = 0;     // eg: Camera is ON the robot's center line

        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES,
                        CAMERA_CHOICE == FRONT ? 90 : -90, 0, 0));

        for (VuforiaTrackable trackable : allTrackables)
        {
            ((VuforiaTrackableDefaultListener)trackable.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        }

    }

    /**
     *
     *  Scans for a viewmark, and if it sees one, tells you the x,y,z,roll,pitch,heading of the robot
     *  And it also gets the name of the visible viewmark
     *  All of these are stored into the global variables, pos and currentTarget
     *  Will be assigned null if target not found
     *
     */
    private void getPos() {

        pos.clear();
        // check all the trackable target to see which one (if any) is visible.
        targetVisible = false;
        for (VuforiaTrackable trackable : allTrackables) {
            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                telemetry.addData("Visible Target", trackable.getName());
                currentTarget = trackable.getName();
                targetVisible = true;

                // getUpdatedRobotLocation() will return null if no new information is available since
                // the last time that call was made, or if the trackable is not currently visible.
                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }
                break;
            }
        }

        // Provide feedback as to where the robot is located (if we know).
        if (targetVisible) {
            // express position (translation) of robot in inches.
            VectorF translation = lastLocation.getTranslation();
            telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                    translation.get(0) / mmPerInch, translation.get(1) / mmPerInch, translation.get(2) / mmPerInch);
            pos.add(translation.get(0) / mmPerInch);
            pos.add(translation.get(1) / mmPerInch);
            pos.add(translation.get(2) / mmPerInch);
            // express the rotation of the robot in degrees.
            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
            telemetry.addData("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);
            pos.add(rotation.firstAngle);
            pos.add(rotation.secondAngle);
            pos.add(rotation.thirdAngle);
        }
        else {
            telemetry.addData("Visible Target", "none");
            pos.clear();
            currentTarget = null;
        }
        telemetry.update();
    }

    /**
     * Logs "caption:value" to the phone
     *
     * @param caption before the value
     * @param value that is displayed
     */
    private void tele(String caption, String value) {
        telemetry.addData(caption, value);
        telemetry.update();
    }

    /**
     * The move function makes the robot move or pivot
     *
     * @param speed how fast the robot moves
     * @param dx change in x
     * @param dy change in y
     * @param pivot 0 means it moves normally, 1 = left turn, -1 = right turn
     */
    private void move(double speed, double dx, double dy, double pivot) {
        if (pivot == 0) {
            dy = -dy;
            float M1 = (float)(dy - dx);
            float M2 = (float)(dy + dx);
            Motor0.setPower((M1) * speed);
            Motor1.setPower((M2) * speed);
            Motor2.setPower((-M1) * speed);
            Motor3.setPower((-M2) * speed);
        }
        else {
            pivot *= speed;
            Motor0.setPower(pivot);
            Motor1.setPower(pivot);
            Motor2.setPower(pivot);
            Motor3.setPower(pivot);
        }
    }

    /**
     * stops movement
     */
    private void stopMove() {
        move(0,0,0,0);
    }

    private void moveTo(int x, int y, int z) {

        final double XmvtSpeed = 0;
        final double YmvtSpeed = 72;

    }

}
