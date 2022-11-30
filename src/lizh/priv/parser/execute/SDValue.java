package lizh.priv.parser.execute;

import java.text.NumberFormat;

public class SDValue {

    private static final NumberFormat numberFormat;

    static {
        numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
    }

    private String str;

    private Double d;

    private SDValue(String str) {
        this.str = str == null ? "" : str;
    }

    private SDValue(Double d) {
        this.d = d;
    }

    public static SDValue valueOf(String str) {
        return new SDValue(str);
    }

    public static SDValue valueOf(Double d) {
        return new SDValue(d);
    }

    public static SDValue valueOf(Object obj) {
        return obj instanceof Double ? valueOf((Double) obj) : valueOf(obj.toString());
    }

    public boolean isDouble() {
        return d != null;
    }

    public boolean isString() {
        return str != null;
    }

    public String getAsString() {
        return d != null ? numberFormat.format(d) : str;
    }

    public Double getAsDouble() {
        return d != null ? d : Double.valueOf(str);
    }

    @Override
    public String toString() {
        return getAsString();
    }
}
