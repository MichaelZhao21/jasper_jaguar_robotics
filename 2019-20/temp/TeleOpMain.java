package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class TeleOpMain extends LinearOpMode {

    JagBot bot = new JagBot();

    @Override
    public void runOpMode() {
        bot.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {

            bot.move(gamepad1.left_stick_x, gamepad1.left_stick_y,
                     gamepad1.right_stick_x);

        }

    }
}
