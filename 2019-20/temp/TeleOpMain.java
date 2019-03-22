package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Main TeleOp test code
 */
@TeleOp(name="TeleOpMain", group="Test")
public class TeleOpMain extends LinearOpMode {

    private JagBot bot = new JagBot(telemetry, hardwareMap, false);

    @Override
    public void runOpMode() {

        bot.init();

        waitForStart();

        while (opModeIsActive()) {

            bot.move(gamepad1.left_stick_x,
                     gamepad1.left_stick_y,
                     gamepad1.right_stick_x);

        }

    }
}
