### What is this ?
This app connects an Android phone to an IOIO to control an Atari 2600, so you don't have to use these old and clunky controllers when you play. Just use any Android phone and enjoy the experience :)

### What is connected to the IOIO?
On the IOIO, the pins 22-26 are connected to a ULN2003 Transistor Array, which is used to pull the 5V Output on the Atari to Ground, which simulates a button press. I connected the Outputs of the ULN2003 directly to the Atari with a cable.

I haven't tried connecting the IOIO to the Android device via Bluetooth, but that would be the obvious next stage, since gaming is not that comfortable if you have a USB cable connected to your phone.

Even for people which have no knowledge of electronics as me, with the IOIO applications like this are easy to build.

### License
Since it was hard for me to get the IOIO jars to build, i took them from: https://github.com/twyatt/ioioquickstart

I am using the Joystick widget from [https://github.com/zerokol/JoystickView](https://github.com/zerokol/JoystickView) , which is licensed under CC-BY-SA, so this app is also licensed as CC-BY-SA.

<a rel="license" href="http://creativecommons.org/licenses/by-sa/3.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-sa/3.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-sa/3.0/">Creative Commons Attribution-ShareAlike 3.0 Unported License</a>.
