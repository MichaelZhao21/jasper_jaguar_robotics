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

        //[Ending code]

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
//[Hardware initialization]

initVuforia();

if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
    initTfod();
} else {
    telemetry.addData("Sorry!", "This device is not compatible with TFOD");
}
```

The [hardware initialization] placeholder is where you Initialize the variables using the hardwareMap class (eg. `Motor1 = hardwareMap.dcMotor.get("Motor1");`); The `initVuforia();` statement calls that method (covered later). The 'if' conditional statement at the end makes sure that the phone is compatible with TensorFlow (This is to make sure that it doesn't throw an error). If it is compatible, it will run the `initTfod()` method.

```java
waitForStart();
start();

if (tfod != null) {
    tfod.activate();
}

//[Move to sampling position]
```

The `waitForStart();` and `start();` methods are standard and are required, in that order to run auto. The first statement waits for the start button to be pressed, and the second one starts the autonomous. The second statement activates TensorFlow and starts the tracking algorithm. Then, we implement the code that moves the robot into sampling position, where the camera faces 2 of the minerals.

```java
String side;

while (!detected) {
    side = getMinerals();
    telemetry.addData("Gold Side", side);
    telemetry.update();
}

//[Things to do after sampling]
```

We create a new variable, `side`, to store the side of the gold mineral. The return value of `getMinerals()` is stored, and it is printed via telemetry. If we do not detect 2 objects, then the loop will continue to run. After that, the robot runs until this value is used.

```java
if (side == "Left") {
  //[Do if gold mineral is on the left side]
}
else if (side == "Center") {
  //[Do if gold mineral is in the center]
}
else {
  //[Do if gold mineral is on the right side]
}

//[Ending code]
```

This 'if' conditional will check which side is selected and run the code for that side. Then, it will run the code that ends the autonomous program.

```java
/**
 * Initialize the Vuforia localization engine.
 */
private void initVuforia() {

    VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
    parameters.vuforiaLicenseKey = VUFORIA_KEY;
    parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
    vuforia = ClassFactory.getInstance().createVuforia(parameters);

}
```

This method, which is placed OUTSIDE of the `runOpMode()` block, Initializes the Vuforia camera and store it in the 'vuforia' object.

```java
/**
 * Gets the position of the gold mineral
 * @return the position of the gold mineral (Left, Center, Right)
 */
private String getMinerals() {
    String side = "None";
    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
    if (updatedRecognitions != null) {
        telemetry.addData("I SPY", updatedRecognitions.size());
        //[...]
    }
    //[...]
}
```

This method will return a String, which is the position of the gold mineral. At the beginning, it initializaes a local variable, also called 'side', which will store the position of the gold mineral. The list that is created holds every object that the TensorFlow algorithm recognizes, in `Recognition` objects. The 'if' conditional makes sure that the list is in a valid state and prints out 'I SPY: [# of objects]' to the phone.

```java
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
    //[...]
}
```

Next, the method will check if there are exactly 2 minerals that are visible and create 2 variables: `goldMineralX` and `silverMineralX`. These will store the position of the silver and gold minerals. As it loops through the list of objects that it recognizes, it will check the value of the Recognition and get the value, either gold or silver, with the `recognition.getLabel()` method. Then, based on whether or not it's gold, it will store the x-value of the recognition using the `recognition.getLeft()` method in the `goldMineralX` or the `silverMineralX` variable. Then, it will print the 2 values to the phone, for debugging.

```java
if (goldMineralX == -1) {
    side = "Right";
} else if (goldMineralX > silverMineralX) {
    side = "Left";
} else {
    side = "Center";
}
detected = true;
```

The end of the method will set the side of the gold based on this chart:

| Left | Right | Gold Mineral Pos |
| :-: | :-: | :-: |
| Silver | Silver |  Right |
| Silver | Gold | Center |
| Gold | Silver | Left |

Then, it will set the value of the global variable `detected` to true. This will end the loop that this method is run in, continuing auto.

```java
return side;
```

Finally, this method will return the value of `side`, which is either `"Left"`, `"Center"`, or`"Right"`.
