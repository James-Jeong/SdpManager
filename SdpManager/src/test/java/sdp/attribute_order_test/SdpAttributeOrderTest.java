package sdp.attribute_order_test;

import media.sdp.Sdp;
import media.sdp.SdpParser;
import media.sdp.attribute.RtpAttribute;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @class public class SdpAttributeOrderTest
 * @brief SdpAttributeOrderTest class
 */
public class SdpAttributeOrderTest {

    private static final Logger logger = LoggerFactory.getLogger(SdpAttributeOrderTest.class);

    String sdp1 = "v=0\n" +
            "o=- 0 0 IN IP4 192.168.7.34\n" +
            "s=-\n" +
            "c=IN IP4 192.168.7.34\n" +
            "t=0 0\n" +
            "m=audio 10000 RTP/AVP 97 98 101 102\n" +
            "a=fmtp:97 mode-set=5; octet-align=1\n" +
            "a=fmtp:98 mode-set=6; octet-align=1\n" +
            "a=fmtp:101 0-16\n" +
            "a=fmtp:102 0-16\n" +
            "a=rtpmap:97 AMR/8000\n" +
            "a=rtpmap:98 AMR-WB/16000\n" +
            "a=rtpmap:101 telephone-event/8000\n" +
            "a=rtpmap:102 telephone-event/16000";

    public void test() {
        try {
            SdpParser sdpParser = new SdpParser();
            Sdp sdp = sdpParser.parseSdp("sdp1", sdp1);
            logger.debug("Success to parse the sdp. ({})", sdp.getData(true));

            for (RtpAttribute rtpAttribute : sdp.getMediaDescriptionFactory().getMediaFactory(Sdp.AUDIO).getCodecList()) {
                if (rtpAttribute.getPayloadId().equals("97")) {
                    Assert.assertTrue(rtpAttribute.isOctetAlignMode());
                    if (rtpAttribute.isOctetAlignMode()) {
                        logger.info("[PAYLOAD-ID 98] is [OA] mode.");
                    } else {
                        logger.warn("[PAYLOAD-ID 98] is [BE] mode.");
                    }
                } else if (rtpAttribute.getPayloadId().equals("98")) {
                    Assert.assertTrue(rtpAttribute.isOctetAlignMode());
                    if (rtpAttribute.isOctetAlignMode()) {
                        logger.info("[PAYLOAD-ID 100] is [OA] mode.");
                    } else {
                        logger.warn("[PAYLOAD-ID 100] is [BE] mode.");
                    }
                }
            }

        } catch (Exception e) {
            logger.warn("Fail to parse the sdp. ({})", sdp1, e);
        }
    }

}
