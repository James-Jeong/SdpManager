package sdp;

import media.sdp.Sdp;
import media.sdp.SdpParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @class public class SdpTest
 * @brief SdpTest class
 */
public class SdpTest {

    private static final Logger logger = LoggerFactory.getLogger(SdpTest.class);

    String sdp1 = "v=0\n" +
            "o=- 0 0 IN IP4 192.168.7.34\n" +
            "s=-\n" +
            "c=IN IP4 192.168.7.34\n" +
            "t=0 0\n" +
            "m=audio 10000 RTP/AVP 97 98 101 102\n" +
            "a=rtpmap:97 AMR/8000\n" +
            "a=fmtp:97 mode-set=5; octet-align=1\n" +
            "a=rtpmap:98 AMR-WB/16000\n" +
            "a=fmtp:98 mode-set=6; octet-align=1\n" +
            "a=rtpmap:101 telephone-event/8000\n" +
            "a=fmtp:101 0-16\n" +
            "a=rtpmap:102 telephone-event/16000\n" +
            "a=fmtp:102 0-16";

    public void test() {
        try {
            SdpParser sdpParser = new SdpParser();
            Sdp sdp = sdpParser.parseSdp("sdp1", sdp1);
            logger.debug("Success to parse the sdp. ({})", sdp.getData(true));
        } catch (Exception e) {
            logger.warn("Fail to parse the sdp. ({})", sdp1, e);
        }
    }

}
