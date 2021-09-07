package media.sdp.attribute.base;

import java.util.List;

/**
 * @class public class FmtpAttributeFactory extends AttributeFactory
 * @brief FmtpAttributeFactory class
 */
public class FmtpAttributeFactory extends AttributeFactory {

    public static final String MODE_SET = "mode-set";
    public static final String OCTET_ALIGN = "octet-align";

    int modeSet;
    boolean isOaMode = false;

    public FmtpAttributeFactory(char type, String name, String value, List<String> mediaFormats) {
        super(type, name, value, mediaFormats);

        String parameter = value.substring(value.indexOf(" "));
        if (parameter.contains(MODE_SET)) {
            String modeSetStr = parameter.substring(parameter.indexOf(MODE_SET));
            modeSetStr = modeSetStr.substring(modeSetStr.indexOf("=") + 1);
            if (modeSetStr.contains(";")) {
                modeSet = Integer.parseInt(modeSetStr.substring(0, modeSetStr.indexOf(';')).trim());
            } else {
                modeSet = Integer.parseInt(modeSetStr.substring(0, 1));
            }
        }

        if (parameter.contains(OCTET_ALIGN)) {
            String octetAlignStr = parameter.substring(parameter.indexOf(OCTET_ALIGN));
            octetAlignStr = octetAlignStr.substring(octetAlignStr.indexOf("=") + 1);
            if (octetAlignStr.contains(";")) {
                isOaMode = Integer.parseInt(octetAlignStr.substring(0, octetAlignStr.indexOf(';')).trim()) == 1;
            } else {
                isOaMode = Integer.parseInt(octetAlignStr.substring(0, 1)) == 1;
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////

    public int getModeSet() {
        return modeSet;
    }

    public void setModeSet(int modeSet) {
        this.modeSet = modeSet;
    }

    public boolean isOaMode() {
        return isOaMode;
    }

    public void setOaMode(boolean oaMode) {
        isOaMode = oaMode;
    }
}
