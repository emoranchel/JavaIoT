package iot.test.grovepi;

import java.io.Closeable;
import java.io.IOException;

public class GrovePi implements Closeable {

  private final GrovePiInterface grovePi;

  public GrovePi(GrovePiInterface grovePi) {
    this.grovePi = grovePi;
  }

  public void setDigital(byte pin, boolean value) throws IOException {
    byte val = value ? (byte) 1 : (byte) 0;
    grovePi.sendCommand((byte) 2, pin, val, (byte) 0);

  }

  public void setPinModeOutput(byte pin) throws IOException {
    grovePi.sendCommand((byte) 5, pin, (byte) 1, (byte) 0);
  }

  @Override
  public void close() {
    grovePi.close();
  }

}
