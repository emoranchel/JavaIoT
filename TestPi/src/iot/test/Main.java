package iot.test;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import iot.test.grovepi.GrovePi;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.dio.DeviceConfig;
import jdk.dio.DeviceManager;
import jdk.dio.gpio.GPIOPin;
import jdk.dio.gpio.GPIOPinConfig;
import iot.test.BMP085.BMP085Device;
import iot.test.BMP085.BMP085Mode;
import iot.test.BMP085.BMP085Result;
import iot.test.grovepi.GrovePiDio;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

public class Main {

  public static void main(String[] args) throws Exception {
    Logger.getLogger("DIO").setLevel(Level.OFF);
    Logger.getLogger("GrovePi").setLevel(Level.OFF);
    //testGrovePi();
    //testI2C();
    //testLedDio();
    //testLedPi4J();
    //testTemperatureBuzzer();
    testTemperatureRESTClient();
  }

  private static void testLedPi4J() throws InterruptedException {
    GpioController gpio = GpioFactory.getInstance();
    GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00);
    pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
    for (int i = 0; i < 10; i++) {
      pin.toggle();
      Thread.sleep(1000);
    }
  }

  private static void testLedDio() throws InterruptedException, IOException {
    GPIOPinConfig config = new GPIOPinConfig(
            DeviceConfig.DEFAULT,
            17,
            GPIOPinConfig.DIR_OUTPUT_ONLY,
            GPIOPinConfig.MODE_OUTPUT_PUSH_PULL,
            GPIOPinConfig.TRIGGER_NONE,
            false
    );
    try (GPIOPin led = DeviceManager.open(GPIOPin.class, config)) {
      for (int i = 0; i < 4; i++) {
        System.out.println("Turn on!");
        led.setValue(true);
        Thread.sleep(1000);
        System.out.println("Turn off!");
        led.setValue(false);
        Thread.sleep(1000);
      }
    }
    System.out.println("KTNXBYE!");
  }

  private static void testI2C() throws IOException, InterruptedException {
    try (BMP085Device bmp085Device = new BMP085Device()) {
      bmp085Device.initialize();
      for (int i = 0; i < 5; i++) {
        System.out.println(bmp085Device.getTemperaturePressure(BMP085Mode.STANDARD));
        Thread.sleep(1000);
      }
    }
  }

  private static void testGrovePi() throws IOException, InterruptedException {
    try (GrovePi grovePi = new GrovePi(new GrovePiDio())) {
      Thread.sleep(1000);
      final byte led = (byte) 3;
      final byte buzzer = (byte) 4;
      grovePi.setPinModeOutput(led);
      grovePi.setPinModeOutput(buzzer);
      for (int i = 0; i < 5; i++) {
        grovePi.setDigital(buzzer, true);
        grovePi.setDigital(led, true);
        Thread.sleep(500);
        grovePi.setDigital(buzzer, false);
        grovePi.setDigital(led, false);
        Thread.sleep(500);
      }
    }
  }

  private static void testTemperatureBuzzer() throws Exception {
    try (GrovePi grovePi = new GrovePi(new GrovePiDio());
            BMP085Device bmp085Device = new BMP085Device()) {
      Thread.sleep(1000);
      final byte led = (byte) 3;
      final byte buzzer = (byte) 4;
      grovePi.setPinModeOutput(led);
      grovePi.setPinModeOutput(buzzer);
      bmp085Device.initialize();
      for (int i = 0; i < 30; i++) {
        BMP085Result temperaturePressure = bmp085Device.getTemperaturePressure(BMP085Mode.STANDARD);
        System.out.println(temperaturePressure);
        if (temperaturePressure.getTemperatureCelsius() > 31) {
          grovePi.setDigital(buzzer, true);
          grovePi.setDigital(led, true);
        } else {
          grovePi.setDigital(buzzer, false);
          grovePi.setDigital(led, false);
        }
        Thread.sleep(500);
      }
      grovePi.setDigital(buzzer, false);
      grovePi.setDigital(led, false);
    }
  }

  private static void testTemperatureRESTClient() throws Exception {
    try (GrovePi grovePi = new GrovePi(new GrovePiDio());
            BMP085Device bmp085Device = new BMP085Device()) {
      Thread.sleep(1000);
      final byte led = (byte) 3;
      final byte buzzer = (byte) 4;
      grovePi.setPinModeOutput(led);
      grovePi.setPinModeOutput(buzzer);
      bmp085Device.initialize();
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target("http://192.168.1.100:8080/TestIoTServer/rest/temperature");
      for (int i = 0; i < 30; i++) {
        BMP085Result temperaturePressure = bmp085Device.getTemperaturePressure(BMP085Mode.STANDARD);
        System.out.println(temperaturePressure);

        target.request().post(Entity.text("TEMP:" + temperaturePressure.getTemperatureCelsius()));

        if (temperaturePressure.getTemperatureCelsius() > 31) {
          grovePi.setDigital(buzzer, true);
          grovePi.setDigital(led, true);
        } else {
          grovePi.setDigital(buzzer, false);
          grovePi.setDigital(led, false);
        }
        Thread.sleep(500);
      }
      grovePi.setDigital(buzzer, false);
      grovePi.setDigital(led, false);
    }
  }

}
