package com.qualcomm.ftcrobotcontroller.opmodes;

//------------------------------------------------------------------------------
//
// PushBotAuto
//
/**
 * Provide a basic autonomous operational mode that uses the left and right
 * drive motors and associated encoders implemented using a state machine for
 * the Push Bot.
 *
 * @author SSI Robotics
 * @version 2015-08-01-06-01
 */
public class PushBotAuto extends PushBotTelemetry
{
    final static int CLICKSPERTILE = 100;
    //--------------------------------------------------------------------------
    //
    // PushBotAuto
    //
    /**
     * Construct the class.
     *
     * The system calls this member when the class is instantiated.
     */
    public PushBotAuto ()

    {
        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.

    } // PushBotAuto

    //--------------------------------------------------------------------------
    //
    // start
    //
    /**
     * Perform any actions that are necessary when the OpMode is enabled.
     *
     * The system calls this member once when the OpMode is enabled.
     */
    @Override public void start ()

    {
        //
        // Call the PushBotHardware (super/base class) start method.
        //
        super.start ();

        //
        // Reset the motor encoders on the drive wheels.
        //
        reset_drive_encoders ();

    } // start

    //--------------------------------------------------------------------------
    //
    // loop
    //
    /**
     * Implement a state machine that controls the robot during auto-operation.
     * The state machine uses a class member and encoder input to transition
     * between states.
     *
     * The system calls this member repeatedly while the OpMode is running.
     */
    @Override public void loop ()
    {
        long start, elapsed;
        switch (v_state)
        {
        //
        // Synchronize the state machine and hardware.
        //
        case 0:
            start = System.currentTimeMillis();
            reset_drive_encoders ();
            if (System.currentTimeMillis() - start >= 5000)
                v_state++;

            break;
// cas 1 wait 10sc cas lst rotate servo 180
        case 1:
            run_using_encoders ();
            set_drive_power (1.0f, 1.0f);

            if (have_drive_encoders_reached (2 * CLICKSPERTILE, 2 * CLICKSPERTILE))
            {
                reset_drive_encoders ();

                set_drive_power (0.0f, 0.0f);

                v_state++;
            }
            break;
        //
        // Wait...
        //
        case 2:
            if (have_drive_encoders_reset ())
            {
                v_state++;
            }
            break;
        //
        // Turn left until the encoders exceed the specified values.
        //
        case 3:
            run_using_encoders ();
            set_drive_power (1.0f, -1.0f);
            if (have_drive_encoders_reached (50, 50))
            {
                reset_drive_encoders ();
                set_drive_power (0.0f, 0.0f);
                v_state++;
            }
            break;

        case 4:
            if (have_drive_encoders_reset())
            {
                v_state++;
            }
            break;

        case 5:
            run_using_encoders ();
            set_drive_power (1.0f, 1.0f);
            if (have_drive_encoders_reached(3 * CLICKSPERTILE, 3 * CLICKSPERTILE))
            {
                reset_drive_encoders ();
                set_drive_power (0.0f, 0.0f);
                v_state++;
            }
            break;

        case 6:
            if (have_drive_encoders_reset())
            {
                v_state++;
            }
            break;

        case 7:
            run_using_encoders();
            set_arm_power(1.0f);
            if (has_arm_encoder_reached(25)){
                reset_drive_encoders ();
                set_arm_power (0.0f);
                v_state++;
            }
            set_servo_position(.5);
            break;

        default:

            break;
        }

        update_telemetry (); // Update common telemetry
        telemetry.addData ("18", "State: " + v_state);

    } // loop

    //--------------------------------------------------------------------------
    //
    // v_state
    //
    /**
     * This class member remembers which state is currently active.  When the
     * start method is called, the state will be initialized (0).  When the loop
     * starts, the state will change from initialize to state_1.  When state_1
     * actions are complete, the state will change to state_2.  This implements
     * a state machine for the loop method.
     */
    private int v_state = 0;

} // PushBotAuto
