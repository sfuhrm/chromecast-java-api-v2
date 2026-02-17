import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class DeviceTest {

    @Test
    public void testDeviceMethod() {
        // arrange
        Device device = new Device();

        // act
        String result = device.methodToTest();

        // assert
        Assertions.assertEquals("expectedValue", result);
    }
}