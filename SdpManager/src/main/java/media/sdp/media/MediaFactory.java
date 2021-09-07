package media.sdp.media;

import media.sdp.SdpFactory;
import media.sdp.attribute.RtpAttribute;
import media.sdp.attribute.base.AttributeFactory;
import media.sdp.attribute.base.FmtpAttributeFactory;
import media.sdp.attribute.base.RtpMapAttributeFactory;
import media.sdp.field.ConnectionField;
import media.sdp.field.MediaField;

import java.util.*;

import static media.sdp.attribute.base.RtpMapAttributeFactory.DTMF;

/**
 * @class public class MediaFactory
 * @brief MediaFactory class
 */
public class MediaFactory extends SdpFactory {

    public static final String RTPMAP = "rtpmap";
    public static final String FMTP = "fmtp";

    private final Map<String, RtpAttribute> attributeFactoryMap = new HashMap<>();
    private List<RtpAttribute> intersectedCodecList = null;

    // Mandatory
    private MediaField mediaField;

    // Optional
    private ConnectionField connectionField;

    ////////////////////////////////////////////////////////////////////////////////

    public MediaFactory(
            char type,
            String mediaType, Vector mediaFormats, int mediaPort,
            String protocol, int portCount) {
        this.mediaField = new MediaField(
                type,
                mediaType,
                mediaPort,
                protocol,
                Arrays.asList((String[]) mediaFormats.toArray(new String[mediaFormats.size()])),
                portCount
        );
    }

    ////////////////////////////////////////////////////////////////////////////////

    public void setConnectionField(char connectionType, String connectionAddress, String connectionAddressType, String connectionNetworkType) {
        this.connectionField = new ConnectionField(
                connectionType,
                connectionAddress,
                connectionAddressType,
                connectionNetworkType
        );
    }

    public ConnectionField getConnectionField() {
        return connectionField;
    }

    public void setConnectionField(ConnectionField connectionField) {
        this.connectionField = connectionField;
    }

    public String getConnectionData () {
        if (connectionField != null) {
            return connectionField.getConnectionType() + "=" +
                    connectionField.getConnectionNetworkType() + " " +
                    connectionField.getConnectionAddressType() + " " +
                    connectionField.getConnectionAddress() +
                    CRLF;
        }

        return "";
    }

    public List<RtpAttribute> getIntersectedCodecList() {
        if (intersectedCodecList == null) {
            return getCodecList();
        }
        return intersectedCodecList;
    }

    public void setIntersectedCodecList(List<RtpAttribute> intersectedCodecList) {
        this.intersectedCodecList = intersectedCodecList;
    }

    public MediaField getMediaField() {
        return mediaField;
    }

    public void setMediaField(MediaField mediaField) {
        this.mediaField = mediaField;
    }

    ////////////////////////////////////////////////////////////////////////////////

    public void addAttributeFactory(AttributeFactory attributeFactory) {
        String payloadId = attributeFactory.getPayloadId();
        if (payloadId == null) {
            return;
        }

        RtpAttribute rtpAttribute = attributeFactoryMap.get(payloadId);
        if (rtpAttribute == null) {
            rtpAttribute = new RtpAttribute(
                    payloadId
            );
            rtpAttribute.setCustomAttributeFactory(attributeFactory);

            attributeFactoryMap.putIfAbsent(
                    payloadId,
                    rtpAttribute
            );
        }
    }

    public void addRtpAttributeFactory(RtpMapAttributeFactory rtpMapAttributeFactory) {
        String payloadId = rtpMapAttributeFactory.getPayloadId();
        if (payloadId == null) {
            return;
        }

        RtpAttribute rtpAttribute = attributeFactoryMap.get(payloadId);
        if (rtpAttribute == null) {
            rtpAttribute = new RtpAttribute(
                    payloadId
            );
            rtpAttribute.setRtpMapAttributeFactory(rtpMapAttributeFactory);

            attributeFactoryMap.putIfAbsent(
                    payloadId,
                    rtpAttribute
            );
        }
    }

    public void addFmtpAttributeFactory(FmtpAttributeFactory fmtpAttributeFactory) {
        String payloadId = fmtpAttributeFactory.getPayloadId();
        if (payloadId == null) {
            return;
        }

        RtpAttribute rtpAttribute = attributeFactoryMap.get(payloadId);
        if (rtpAttribute != null) {
            if (fmtpAttributeFactory.getName().equals(FMTP)) {
                rtpAttribute.addFmtpAttributeFactory(fmtpAttributeFactory);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////

    public List<RtpAttribute> getCodecList () {
        List<RtpAttribute> codecList = new ArrayList<>();

        for (String format : mediaField.getMediaFormats()) {
            for (Map.Entry<String, RtpAttribute> entry : attributeFactoryMap.entrySet()) {
                if (entry == null) {
                    continue;
                }

                RtpAttribute rtpAttribute = entry.getValue();
                if (rtpAttribute == null) {
                    continue;
                }

                if (format.equals(rtpAttribute.getPayloadId())) {
                    codecList.add(rtpAttribute);
                    break;
                }
            }
        }

        return codecList;
    }

    ////////////////////////////////////////////////////////////////////////////////

    public String getData (boolean isRaw) {
        String mediaString = getConnectionData();
        mediaString += mediaField.getType() + "=" +
                mediaField.getMediaType() + " " +
                mediaField.getMediaPort() + " " +
                mediaField.getProtocol() + " ";

        StringBuilder data = new StringBuilder(
                mediaString
        );

        int i = 0;
        List<RtpAttribute> curCodecList = intersectedCodecList;
        if (curCodecList == null) {
            curCodecList = getCodecList();
        }

        // CODEC : Payload ID
        for (RtpAttribute rtpAttribute : curCodecList) {
            String codecName = rtpAttribute.getRtpMapAttributeFactory().getCodecName();
            if (codecName.equals(DTMF)) {
                i++;
                continue;
            }

            data.append(rtpAttribute.getPayloadId());

            if (!isRaw) {
                break;
            }

            if ((i + 1) < curCodecList.size()) {
                data.append(" ");
            }

            i++;
        }

        if (data.charAt(data.length() - 1) != ' ') {
            data.append(" ");
        }

        // CODEC : Payload ID
        i = 0;
        for (RtpAttribute rtpAttribute : curCodecList) {
            String codecName = rtpAttribute.getRtpMapAttributeFactory().getCodecName();
            if (!codecName.equals(DTMF)) {
                i++;
                continue;
            }

            data.append(rtpAttribute.getPayloadId());

            if (!isRaw) {
                break;
            }

            if ((i + 1) < curCodecList.size()) {
                data.append(" ");
            }

            i++;
        }
        data.append(CRLF);

        // CODEC : RTPMAP & FMTP
        for (RtpAttribute rtpAttribute : curCodecList) {
            String codecName = rtpAttribute.getRtpMapAttributeFactory().getCodecName();
            if (codecName.equals(DTMF)) {
                continue;
            }

            data.append(rtpAttribute.getData());
            if (!isRaw) {
                break;
            }
        }

        // DTMF : RTPMAP & FMTP
        for (RtpAttribute rtpAttribute : curCodecList) {
            String codecName = rtpAttribute.getRtpMapAttributeFactory().getCodecName();
            if (!codecName.equals(DTMF)) {
                continue;
            }

            data.append(rtpAttribute.getData());
            if (!isRaw) {
                break;
            }
        }

        // CUSTOM
        List<RtpAttribute> rtpAttributeList = new ArrayList<>(attributeFactoryMap.values());
        Collections.reverse(rtpAttributeList);
        for (RtpAttribute rtpAttribute : rtpAttributeList) {
            if (!mediaField.getMediaFormats().contains(rtpAttribute.getPayloadId())) {
                data.append(rtpAttribute.getData());
            }
        }

        return data.toString();
    }

}
