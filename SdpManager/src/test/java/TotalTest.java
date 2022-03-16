import org.junit.Test;
import sdp.SdpTest;
import sdp.attribute_order_test.SdpAttributeOrderTest;

/**
 * @class public class TotalTest
 * @brief TotalTest class
 */
public class TotalTest {

    @Test
    public void testAll() {
        // SDP TEST
        SdpTest sdpTest = new SdpTest();
        sdpTest.test();

        SdpAttributeOrderTest sdpAttributeOrderTest = new SdpAttributeOrderTest();
        sdpAttributeOrderTest.test();
    }

}
