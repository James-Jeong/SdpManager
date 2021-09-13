package media.sdp;

import media.sdp.attribute.RtpAttribute;
import media.sdp.attribute.base.FmtpAttributeFactory;
import media.sdp.attribute.base.RtpMapAttributeFactory;
import media.sdp.field.ConnectionField;
import media.sdp.media.MediaDescriptionFactory;
import media.sdp.media.MediaFactory;
import media.sdp.session.SessionDescriptionFactory;
import media.sdp.time.TimeDescriptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @class public class Sdp
 * @brief Sdp class
 */
public class Sdp {

    private static final Logger logger = LoggerFactory.getLogger(Sdp.class);

    public static final String AUDIO = "audio";
    public static final String VIDEO = "video";
    public static final String APPLICATION = "application";

    private final String id;

    private SessionDescriptionFactory sessionDescriptionFactory = null;
    private TimeDescriptionFactory timeDescriptionFactory = null;
    private MediaDescriptionFactory mediaDescriptionFactory = null;

    public Sdp(String id) {
        this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////////

    public String getId() {
        return id;
    }

    public SessionDescriptionFactory getSessionDescriptionFactory() {
        return sessionDescriptionFactory;
    }

    public void setSessionDescriptionFactory(SessionDescriptionFactory sessionDescriptionFactory) {
        this.sessionDescriptionFactory = sessionDescriptionFactory;
    }

    public TimeDescriptionFactory getTimeDescriptionFactory() {
        return timeDescriptionFactory;
    }

    public void setTimeDescriptionFactory(TimeDescriptionFactory timeDescriptionFactory) {
        this.timeDescriptionFactory = timeDescriptionFactory;
    }

    public MediaDescriptionFactory getMediaDescriptionFactory() {
        return mediaDescriptionFactory;
    }

    public void setMediaDescriptionFactory(MediaDescriptionFactory mediaDescriptionFactory) {
        this.mediaDescriptionFactory = mediaDescriptionFactory;
    }

    ////////////////////////////////////////////////////////////////////////////////

    public String getData (boolean isRaw) {
        return sessionDescriptionFactory.getData() +
                timeDescriptionFactory.getData() +
                mediaDescriptionFactory.getData(isRaw);
    }

    ////////////////////////////////////////////////////////////////////////////////

    public void setSessionOriginAddressByOtherSdp(Sdp otherSdp) {
        if (otherSdp == null) {
            return;
        }

        String prevSessionAddress = this.sessionDescriptionFactory.getOriginField().getOriginAddress();
        this.sessionDescriptionFactory.getOriginField().
                setOriginAddress(
                        otherSdp.getSessionDescriptionFactory().getOriginField().
                                getOriginAddress()
                );
        logger.debug("({}) Session Origin Address is changed. ({} > {})",
                id, prevSessionAddress, this.sessionDescriptionFactory.getOriginField().getOriginAddress()
        );
    }

    public void setSessionConnectionAddressByOtherSdp(Sdp otherSdp) {
        if (otherSdp == null) {
            return;
        }

        ConnectionField connectionField = this.sessionDescriptionFactory.getConnectionField();
        if (connectionField == null) {
            return;
        }

        String prevConnectionAddress = connectionField.getConnectionAddress();
        connectionField.
                setConnectionAddress(
                        otherSdp.getSessionDescriptionFactory().getConnectionField().
                                getConnectionAddress()
                );

        logger.debug("({}) Session Connection Address is changed. ({} > {})",
                id,
                prevConnectionAddress,
                connectionField.getConnectionAddress()
        );
    }

    public void setMediaConnectionAddressByOtherSdp(String mediaType, Sdp otherSdp) {
        if (mediaType == null || otherSdp == null) {
            return;
        }

        MediaFactory mediaFactory = this.mediaDescriptionFactory.getMediaFactory(mediaType);
        if (mediaFactory == null) {
            return;
        }

        MediaFactory otherMediaFactory = otherSdp.getMediaDescriptionFactory().getMediaFactory(mediaType);
        if (otherMediaFactory == null) {
            return;
        }

        ConnectionField connectionField = mediaFactory.getConnectionField();
        if (connectionField == null) {
            return;
        }

        ConnectionField otherConnectionField = otherMediaFactory.getConnectionField();
        if (otherConnectionField == null) {
            return;
        }

        String prevConnectionAddress = connectionField.getConnectionAddress();
        connectionField.setConnectionAddress(
                otherConnectionField.getConnectionAddress()
        );

        logger.debug("({}) Media ({}) Connection Address is changed. ({} > {})",
                id,
                mediaType,
                prevConnectionAddress,
                connectionField.getConnectionAddress()
        );
    }

    public void setMediaPort(String mediaType, int port) {
        if (mediaType == null || port <= 0) {
            return;
        }

        MediaFactory mediaFactory = this.mediaDescriptionFactory.getMediaFactory(mediaType);
        if (mediaFactory == null) {
            return;
        }

        int prevMediaPort = mediaFactory.getMediaField().getMediaPort();
        mediaFactory.getMediaField().setMediaPort(
                port
        );

        logger.debug("({}) Media port is changed. ({} > {})",
                id,
                prevMediaPort,
                port
        );
    }

    public void setMediaPortByOtherSdp(String mediaType, Sdp otherSdp) {
        if (mediaType == null || otherSdp == null) {
            return;
        }

        MediaFactory mediaFactory = this.mediaDescriptionFactory.getMediaFactory(mediaType);
        if (mediaFactory == null) {
            return;
        }

        MediaDescriptionFactory otherSdpMdFactory = otherSdp.getMediaDescriptionFactory();
        if (otherSdpMdFactory == null) {
            return;
        }
        MediaFactory otherMediaFactory = otherSdpMdFactory.getMediaFactory(mediaType);
        if (otherMediaFactory == null) {
            return;
        }

        int otherMediaPort = otherMediaFactory.getMediaField().getMediaPort();
        if (otherMediaPort <= 0) {
            return;
        }

        int prevMediaPort = mediaFactory.getMediaField().getMediaPort();
        mediaFactory.getMediaField().setMediaPort(
                otherMediaPort
        );

        logger.debug("({}) Media port is changed. ({} > {})",
                id,
                prevMediaPort,
                otherMediaPort
        );
    }

    public boolean isDtmfEnabled (String mediaType, boolean isIntersected) {
        if (mediaType == null) {
            return false;
        }

        boolean result = false;

        List<RtpAttribute> codecList;
        if (isIntersected) {
            codecList = this.mediaDescriptionFactory.getIntersectedCodecList(mediaType);
        } else {
            codecList = this.mediaDescriptionFactory.getCodecList(mediaType);
        }

        for (RtpAttribute rtpAttribute : codecList) {
            RtpMapAttributeFactory rtpMapAttributeFactory = rtpAttribute.getRtpMapAttributeFactory();
            if (rtpMapAttributeFactory == null) {
                continue;
            }

            if (rtpMapAttributeFactory.getCodecName().equals(RtpMapAttributeFactory.DTMF)) {
                result = true;
                break;
            }
        }

        return result;
    }

    public int getAttributeCount (String mediaType, boolean isIntersected, boolean isDtmf) {
        if (mediaType == null) {
            return 0;
        }

        int result = 0;

        List<RtpAttribute> codecList;
        if (isIntersected) {
            codecList = this.mediaDescriptionFactory.getIntersectedCodecList(mediaType);
        } else {
            codecList = this.mediaDescriptionFactory.getCodecList(mediaType);
        }

        for (RtpAttribute rtpAttribute : codecList) {
            RtpMapAttributeFactory rtpMapAttributeFactory = rtpAttribute.getRtpMapAttributeFactory();
            if (rtpMapAttributeFactory == null) {
                continue;
            }

            if (isDtmf) {
                if (rtpMapAttributeFactory.getCodecName().equals(RtpMapAttributeFactory.DTMF)) {
                    result++;
                }
            } else {
                if (!rtpMapAttributeFactory.getCodecName().equals(RtpMapAttributeFactory.DTMF)) {
                    result++;
                }
            }
        }

        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////

    public boolean intersect (String mediaType, String callId, Sdp otherSdp, boolean isRelay) {
        if (mediaType == null || callId == null || otherSdp == null) {
            return false;
        }

        //int otherSdpDtmfAttributeCount = otherSdp.getAttributeCount(AUDIO, false, true);
        int otherSdpCodecAttributeCount = otherSdp.getAttributeCount(AUDIO, false, false);
        if (otherSdpCodecAttributeCount == 0) {
            logger.warn("({}) Other sdp has not any codec attributes. (sdp=\n{})", callId, otherSdp.getData(true));
            return false;
        }

        List<RtpAttribute> intersectedCodecList = new ArrayList<>();
        List<RtpAttribute> otherSdpCodecList = otherSdp.getMediaDescriptionFactory().getCodecList(AUDIO);

        // 현재 SDP 가 기준
        for (RtpAttribute curRtpAttribute : mediaDescriptionFactory. getCodecList(AUDIO)) {
            RtpMapAttributeFactory curRtpMapAttributeFactory = curRtpAttribute.getRtpMapAttributeFactory();
            if (curRtpMapAttributeFactory == null) {
                continue;
            }

            for (RtpAttribute otherRtpAttribute : otherSdpCodecList) {
                RtpMapAttributeFactory otherRtpMapAttributeFactory = otherRtpAttribute.getRtpMapAttributeFactory();
                if (otherRtpMapAttributeFactory == null) {
                    continue;
                }

                // 1) Codec Name Matched
                if (otherRtpMapAttributeFactory.getCodecName().equals(curRtpMapAttributeFactory.getCodecName())
                        && otherRtpMapAttributeFactory.getSamplingRate().equals(curRtpMapAttributeFactory.getSamplingRate())) {
                    //logger.debug("other: {}, cur: {}", otherAttributeFactory.getCodecName(), curAttributeFactory.getCodecName());

                    if (isRelay) {
                        curRtpAttribute.clearFmtpAttributeFactoryList();
                        for (FmtpAttributeFactory fmtpAttributeFactory : otherRtpAttribute.getFmtpAttributeFactoryList()) {
                            curRtpAttribute.addFmtpAttributeFactory(
                                    new FmtpAttributeFactory(
                                            fmtpAttributeFactory.getType(),
                                            MediaFactory.FMTP,
                                            curRtpAttribute.getPayloadId() + fmtpAttributeFactory.getValueExceptPayloadId(),
                                            this.mediaDescriptionFactory.getMediaFactory(mediaType).
                                                    getMediaField().getMediaFormats()
                                    )
                            );
                            logger.debug("({}) Other FMTP [{}] is set-up.", id, fmtpAttributeFactory.getData());
                        }
                    } else {
                        // DTMF
                        FmtpAttributeFactory curModeSetAttribute = curRtpAttribute.getModeSetAttribute();
                        curRtpAttribute.clearFmtpAttributeFactoryList();

                        if (curModeSetAttribute != null) {
                            int curModeSet = curModeSetAttribute.getModeSetMax();
                            if (curModeSet >= 0) {
                                //FmtpAttributeFactory fmtpAttributeFactory = otherRtpAttribute.getFmtpAttributeFactoryList().get(0);
                                FmtpAttributeFactory fmtpAttributeFactory = otherRtpAttribute.getFirstModeSetFmtpAttributeFactory();
                                if (fmtpAttributeFactory != null) {
                                    String valueStr;
                                    int amfModeSet = fmtpAttributeFactory.getModeSetMax();
                                    if (fmtpAttributeFactory.getModeSetMax() >= 0) {
                                        int resultModeSet = curModeSet;
                                        if (resultModeSet > amfModeSet) {
                                            //resultModeSet = amfModeSet;
                                            resultModeSet = curModeSetAttribute.getModeSetMin();
                                        }
                                        valueStr = curRtpAttribute.getPayloadId() + (fmtpAttributeFactory.isOaMode() ? " octet-align=1;" : "") + " mode-set=" + resultModeSet;
                                    } else {
                                        valueStr = curRtpAttribute.getPayloadId() + fmtpAttributeFactory.getValueExceptPayloadId() + "; mode-set=" + curModeSet;
                                    }

                                    FmtpAttributeFactory newFmtpAttributeFactory = new FmtpAttributeFactory(
                                            fmtpAttributeFactory.getType(),
                                            MediaFactory.FMTP,
                                            valueStr,
                                            this.mediaDescriptionFactory.getMediaFactory(mediaType).
                                                    getMediaField().getMediaFormats()
                                    );

                                    curRtpAttribute.addFmtpAttributeFactory(newFmtpAttributeFactory);
                                    logger.debug("({}) Local FMTP [{}] is set-up.", id, newFmtpAttributeFactory.getData());
                                }
                            }
                        } else {
                            for (FmtpAttributeFactory fmtpAttributeFactory : otherRtpAttribute.getFmtpAttributeFactoryList()) {
                                FmtpAttributeFactory newFmtpAttributeFactory = new FmtpAttributeFactory(
                                        fmtpAttributeFactory.getType(),
                                        MediaFactory.FMTP,
                                        curRtpAttribute.getPayloadId() + fmtpAttributeFactory.getValueExceptPayloadId(),
                                        this.mediaDescriptionFactory.getMediaFactory(mediaType).
                                                getMediaField().getMediaFormats()
                                );

                                curRtpAttribute.addFmtpAttributeFactory(newFmtpAttributeFactory);
                                logger.debug("({}) Local FMTP [{}] is set-up.", id, newFmtpAttributeFactory.getData());
                            }
                        }
                    }

                    if (!intersectedCodecList.contains(curRtpAttribute)) {
                        intersectedCodecList.add(curRtpAttribute);
                    }

                    break;
                }
            }
        }

        if (!intersectedCodecList.isEmpty()) {
            this.mediaDescriptionFactory.setIntersectedCodecList(
                    mediaType,
                    intersectedCodecList
            );
            return true;
        } else {
            return false;
        }
    }


}
