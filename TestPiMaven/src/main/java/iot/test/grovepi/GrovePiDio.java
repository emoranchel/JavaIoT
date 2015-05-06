package iot.test.grovepi;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.dio.DeviceManager;
import jdk.dio.i2cbus.I2CDevice;
import jdk.dio.i2cbus.I2CDeviceConfig;

public class GrovePiDio implements GrovePiInterface {

  private final I2CDevice grovePi;

  public GrovePiDio() throws IOException {
    final int i2cBus = 1;                        // Raspberry Pi's I2C bus
    final int address = 0x04;                    // Device address
    final int serialClock = 3400000;             // 3.4MHz Max clock
    final int addressSizeBits = 7;               // Device address size in bits

    I2CDeviceConfig config = new I2CDeviceConfig(i2cBus, address, addressSizeBits, serialClock);
    grovePi = DeviceManager.open(config);
  }

  @Override
  public void sendCommand(byte[] bc) throws IOException {
    ByteBuffer command = ByteBuffer.wrap(bc);
    command.rewind();
    Logger.getLogger("GrovePi").log(Level.INFO, "[DIO]Sending command {0}", Arrays.toString(bc));
    grovePi.write(command);
  }

  @Override
  public void close() {
    try {
      grovePi.close();
    } catch (IOException ex) {
      Logger.getLogger("GrovePi").log(Level.SEVERE, null, ex);
    }
  }

}
