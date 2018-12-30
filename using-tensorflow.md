# Using TensorFlow

This program was used for the 2018-19 Rover Ruckus Game. The code below is written for a auto that only returns the position of the gold blocks. The [] are where the actual running code goes:

```java
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name="TensorAuto")
public class TensorAuto extends LinearOpMode{

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private static final String VUFORIA_KEY = "[Add your own key here]";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private boolean detected = false;

    //[Hardware variable declarations]

    @Override
    public void runOpMode() throws InterruptedException {

        //[Hardware initialization]

        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        waitForStart();
        start();

        if (tfod != null) {
            tfod.activate();
        }

        //[Move to sampling position]

        String side;

        while (!detected) {
            side = getMinerals();
            telemetry.addData("Gold Side", side);
            telemetry.update();
        }

        //[Things to do after sampling]

        if (side == "Left") {
          //[Do if gold mineral is on the left side]
        }
        else if (side == "Center") {
          //[Do if gold mineral is in the center]
        }
        else {
          //[Do if gold mineral is on the right side]
        }

    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {

        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);

    }

    /**
     * Gets the position of the gold mineral
     * @return the position of the gold mineral (Left, Center, Right)
     */
    private String getMinerals() {
        String side = "None";
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            telemetry.addData("I SPY", updatedRecognitions.size());
            if (updatedRecognitions.size() == 2) {
                int goldMineralX = -1;
                int silverMineralX = -1;
                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                        goldMineralX = (int) recognition.getLeft();
                    } else if (silverMineralX == -1) {
                        silverMineralX = (int) recognition.getLeft();
                    }
                }
                telemetry.addData("GoldX", goldMineralX);
                telemetry.addData("SilverX", silverMineralX);
                if (goldMineralX == -1) {
                    side = "Right";
                } else if (goldMineralX > silverMineralX) {
                    side = "Left";
                } else {
                    side = "Center";
                }
                detected = true;
            }
        }
        return side;
    }

}
```

### Breakdown

```java
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;
```

These are the import and package statements. This file will be in the teamcode folder; the first line should be automatically created. The following 2 lines are to be imported whenever you write an Autonomous OpMode. The 4 import statements after import the Vuforia Camera and localizer, as well as the TensorFlow Lite objects. The last statement imports the List class, which is a standard java.util class and is used to hold the TensorFlow objects.

```java
@Autonomous(name="TensorAuto")
public class TensorAuto extends LinearOpMode{
  //[...]
}
```

This line just says that this file is a LinearOpMode and will be Autonomous, with the name of TensorAuto.

```java
private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

private static final String VUFORIA_KEY = "[Add your own key here]";
private VuforiaLocalizer vuforia;
private TFObjectDetector tfod;
private boolean detected = false;

//[Hardware variable declarations]
```

These public variables will be used by the TensorFlow detection method and will later be put to use. The [Hardware variable declarations] placeholder is where your own declerations for motors, servos, etc. go. (eg. `public DcMotor Motor1;`)

```java
@Override
public void runOpMode() throws InterruptedException {
  //[...]
}
```

The `@Override` annotation simply states that this is a child of the LinearOpMode class and will override its runOpMode method. The `throws InterruptedException` is important as it will throw the error if the stop button or the power switch is pressed.

```java

```
