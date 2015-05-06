package iot.test.grovepi;

import java.io.Closeable;
import java.io.IOException;

public interface GrovePiInterface extends Closeable {

  public void sendCommand(byte... sequence) throws IOException;

  @Override
  public void close();

}
