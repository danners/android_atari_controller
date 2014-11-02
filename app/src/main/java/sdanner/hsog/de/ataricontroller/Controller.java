package sdanner.hsog.de.ataricontroller;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import sdanner.hsog.de.ataricontroller.util.JoystickView;


public class Controller extends IOIOActivity {


    private final String joyTAG = "JOYSTICK";
    private final String fireTag = "FIREBUTTON";

    private boolean fire_switch = off();
    private boolean down_switch = off();
    private boolean up_switch = off();
    private boolean left_switch = off();
    private boolean right_switch = off();

    public final boolean HIGH = off();
    public final boolean LOW = on();

    boolean on() {
        return true;
    }
    boolean off() {
        return false;
    }

    private void resetPressedState() {
        up_switch = off();
        down_switch = off();

        left_switch = off();
        right_switch = off();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_controller);

        JoystickView joystick = (JoystickView) findViewById(R.id.joystickView);
        Button fireButton = (Button) findViewById(R.id.fireButton);


        fireButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v(fireTag, "fire");
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    fire_switch = LOW;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    fire_switch = HIGH;
                }
                return true;
            }
        });


        //Event listener that always returns the variation of the angle in degrees, motion power in percentage and direction of movement
        joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
                resetPressedState();

                switch (direction) {
                    case JoystickView.FRONT:
                        Log.v(joyTAG, "FRONT");
                        up_switch = on();
                        break;
                    case JoystickView.FRONT_RIGHT:
                        Log.v(joyTAG, "FRONT_RIGHT");
                        up_switch = on();
                        right_switch = on();
                        break;
                    case JoystickView.RIGHT:
                        Log.v(joyTAG, "RIGHT");
                        right_switch = on();
                        break;
                    case JoystickView.RIGHT_BOTTOM:
                        Log.v(joyTAG, "RIGHT_BOTTOM");
                        right_switch = on();
                        down_switch = on();
                        break;
                    case JoystickView.BOTTOM:
                        Log.v(joyTAG, "BOTTOM");
                        down_switch = on();
                        break;
                    case JoystickView.BOTTOM_LEFT:
                        Log.v(joyTAG, "BOTTOM_LEFT");
                        left_switch = on();
                        down_switch = on();
                        break;
                    case JoystickView.LEFT:
                        Log.v(joyTAG, "LEFT");
                        left_switch = on();
                        break;
                    case JoystickView.LEFT_FRONT:
                        Log.v(joyTAG, "LEFT_FRONT");
                        left_switch = on();
                        up_switch = on();
                        break;
                }
            }
        }, 25);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected IOIOLooper createIOIOLooper() {
        return new AtariLooper();
    }

    // Android Utilities
    private void showVersions(IOIO ioio, String title) {
        toast(String.format("%s\n" +
                        "IOIOLib: %s\n" +
                        "Application firmware: %s\n" +
                        "Bootloader firmware: %s\n" +
                        "Hardware: %s",
                title,
                ioio.getImplVersion(IOIO.VersionType.IOIOLIB_VER),
                ioio.getImplVersion(IOIO.VersionType.APP_FIRMWARE_VER),
                ioio.getImplVersion(IOIO.VersionType.BOOTLOADER_VER),
                ioio.getImplVersion(IOIO.VersionType.HARDWARE_VER)));
    }

    private void toast(final String message) {
        final Context context = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    class AtariLooper extends BaseIOIOLooper {

        private final int PIN_UP = 26;
        private final int PIN_DOWN = 25;
        private final int PIN_LEFT = 24;
        private final int PIN_RIGHT = 23;
        private final int PIN_FIRE = 22;


        private DigitalOutput right;
        private DigitalOutput left;
        private DigitalOutput up;
        private DigitalOutput down;
        private DigitalOutput fire;


        @Override
        protected void setup() throws ConnectionLostException, InterruptedException {
            showVersions(ioio_, "IOIO connected!");


            // led
            ioio_.openDigitalOutput(0, false);

            right = ioio_.openDigitalOutput(PIN_RIGHT, DigitalOutput.Spec.Mode.NORMAL, LOW);

            left = ioio_.openDigitalOutput(PIN_LEFT, DigitalOutput.Spec.Mode.NORMAL, LOW);

            up = ioio_.openDigitalOutput(PIN_UP, DigitalOutput.Spec.Mode.NORMAL, LOW);

            down = ioio_.openDigitalOutput(PIN_DOWN, DigitalOutput.Spec.Mode.NORMAL, LOW);

            fire = ioio_.openDigitalOutput(PIN_FIRE, DigitalOutput.Spec.Mode.NORMAL, LOW);

        }

        @Override
        public void loop() throws ConnectionLostException, InterruptedException {

                fire.write(fire_switch);
                up.write(up_switch);
                down.write(down_switch);
                right.write(right_switch);
                left.write(left_switch);
                Thread.sleep(25);

        }


        @Override
        public void disconnected() {
            showVersions(ioio_, "IOIO disconnected");
        }

        @Override
        public void incompatible() {
            showVersions(ioio_, "Incompatible firmware version!");
        }
    }

}
