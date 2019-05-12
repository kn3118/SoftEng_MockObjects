package ic.doc.camera;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class CameraTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  public Sensor sensor = context.mock(Sensor.class);
  public MemoryCard memory = context.mock(MemoryCard.class);
  public Camera camera = new Camera(sensor, memory);

  @Test
  public void switchingTheCameraOnPowersUpTheSensor() {

    context.checking(new Expectations() {
      {
      exactly(1).of(sensor).powerUp();
      }
    });

    camera.powerOn();
  }

  @Test
  public void switchingTheCameraOffPowersDownTheSensor() {

    switchingTheCameraOn();

    context.checking(new Expectations() {
      {
        exactly(1).of(sensor).powerDown();
      }
    });

    camera.powerOff();
  }

  @Test
  public void pressingShutterWhenPowerIsOffDoesNothing() {
    context.checking(new Expectations() {
      {
        never(sensor);
        never(memory);
      }
    });

    camera.pressShutter();
  }

  @Test
  public void pressingShutterWhenPowerOnCopiesData() {

    switchingTheCameraOn();

    context.checking(new Expectations() {
      {
        byte[] data = new byte[10];

        exactly(1).of(sensor).readData(); will(returnValue(data));
        exactly(1).of(memory).write(data);
      }
    });

    camera.pressShutter();
  }

  @Test
  public void switchingTheCameraOffDoesNotPowerDownSensorWhileDataIsBeingWritten() {

    switchingTheCameraOn();

    context.checking(new Expectations() {
      {
        byte[] data = new byte[10];

        exactly(1).of(sensor).readData(); will(returnValue(data));
        exactly(1).of(memory).write(data);
        never(sensor).powerDown();
      }
    });

    camera.pressShutter();
    camera.powerOff();

    context.checking(new Expectations() {
      {
        exactly(1).of(sensor).powerDown();
      }
    });

    camera.writeComplete();
  }

  private void switchingTheCameraOn() {
    context.checking(new Expectations() {
      {
        allowing(sensor).powerUp();
      }
    });
    camera.powerOn();
  }

}
