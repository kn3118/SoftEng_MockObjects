package ic.doc.camera;

public class Camera implements WriteListener {

  private final Sensor sensor;
  private final MemoryCard memoryCard;
  boolean isCameraOn;
  boolean writing = false;

  public Camera(Sensor sensor, MemoryCard memory) {
    this.sensor = sensor;
    this.memoryCard = memory;
    isCameraOn = false;
  }

  public void pressShutter() {
    if (isCameraOn) {
      byte[] data = sensor.readData();
      memoryCard.write(data);
      writing = true;
    }
  }

  public void powerOn() {
    isCameraOn = true;
    sensor.powerUp();
  }

  public void powerOff() {
    if (!writing) {
      sensor.powerDown();
    }
    isCameraOn = false;
  }

  @Override
  public void writeComplete() {
    writing = false;
    if (!isCameraOn) {
      sensor.powerDown();
    }
  }
}

