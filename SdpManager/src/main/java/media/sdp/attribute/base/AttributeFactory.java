package media.sdp.attribute.base;

import media.sdp.SdpFactory;

import java.util.List;

/**
 * @class public class AttributeFactory
 * @brief AttributeFactory class
 */
public class AttributeFactory extends SdpFactory {

    char type;
    String name;
    String payloadId = null;
    String value;

    ////////////////////////////////////////////////////////////////////////////////

    public AttributeFactory(char type, String name, String value, List<String> mediaFormats) {
        this.type = type;
        this.name = name;

        if (value != null) {
            String[] spl = value.split(" ");
            if (spl.length == 0) {
                payloadId = name + ":" + value;
            } else {
                for (String mediaFormat : mediaFormats) {
                    if (mediaFormat.equals(spl[0])) {
                        payloadId = spl[0];
                        break;
                    }
                }

                if (payloadId == null) {
                    payloadId = name + ":" + value;
                }
            }
        } else {
            payloadId = name;
        }

        this.value = value;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayloadId() {
        return payloadId;
    }

    public void setPayloadId(String payloadId) {
        this.payloadId = payloadId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////////

    public String getData () {
        String data = type + "=" + name;

        if (value != null) {
            data += ":";
            data += value;
        }

        data += CRLF;
        return data;
    }
}
