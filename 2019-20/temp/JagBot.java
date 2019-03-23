package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * An object that initializes and contains methods that can be used to
 * run the robot. This was created for the Jasper Jaguar Robotics FTC Team 11419.
 */
public class JagBot {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    private BNO055IMU imu;
    private Orientation angles;
    private Acceleration gravity;

    private Telemetry telemetry;
    private HardwareMap hardwareMap;

    private boolean auto;

    /**
     * Gets a few of the OpMode objects and sets a flag for
     * whether or not this is used for an autonomous opmode
     *
     * @param telemetry Telemetry object
     * @param hardwareMap HardwareMap object
     * @param auto Is this an autonomous?
     */
    public JagBot(Telemetry telemetry,
                  HardwareMap hardwareMap,
                  boolean auto) {
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
        this.auto = auto;
    }

    /**
     * Initiates the robot object using hardwareMap
     * This needs to be done before waitForStart();
     */
    public void init() {

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if (auto) {
            initAuto();
        }
        else {
            initTeleOp();
        }

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

    }

    private void initAuto() {
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void initTeleOp() {
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Move Robot with controller input values
     * @param dx left/right
     * @param dy forward/backward
     * @param ds pivot
     */
    public void move(double dx, double dy, double ds) {
        frontLeft.setPower(dy - dx + ds);
        frontRight.setPower(dy + dx - ds);
        backLeft.setPower(dy + dx + ds);
        backRight.setPower(dy - dx - ds);
    }

    /**
     * Returns the current heading that the robot is facing
     */
    public void getAngle() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("Angular Rotation", angles);
    }

}
