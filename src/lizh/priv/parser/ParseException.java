package lizh.priv.parser;

public class ParseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private boolean ignorable = true;

    public ParseException(String message, int position) {
        super(message + "(位置 : " + position + ")");
    }

    public boolean isIgnorable() {
        return ignorable;
    }

    public void setIgnorable(boolean ignorable) {
        this.ignorable = ignorable;
    }

}
