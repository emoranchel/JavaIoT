package iot.test.grovepi;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrovePi4J implements GrovePiInterface {

  private final I2CBus bus;
  private final I2CDevice device;

  public GrovePi4J() throws IOException {
    this.bus = I2CFactory.getInstance(I2CBus.BUS_1);
    this.device = bus.getDevice(4);

  }

  @Override
  public void sendCommand(byte... sequence) throws IOException {
    Logger.getLogger("GrovePi").log(Level.INFO, "[pi4j]Sending command {0}", Arrays.toString(sequence));
    device.write(1, sequence, 0, sequence.length);
  }

  @Override
  public void close() {
    try {
      bus.close();
    } catch (IOException ex) {
      Logger.getLogger("GrovePi").log(Level.SEVERE, null, ex);
    }
  }

}
