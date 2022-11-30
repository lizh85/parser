package lizh.priv.parser;

public class Token {

    public Token(TOKEN_TYPE type, String value, int position) {
        this.type = type;
        this.value = value;
        this.position = position;
    }

    public Token(TOKEN_TYPE type, char value, int position) {
        this.type = type;
        this.value = "" + value;
        this.position = position;
    }

    public Token(TOKEN_TYPE type, int position) {
        this.type = type;
        this.value = type.getValue();
        this.position = position;
    }

    private TOKEN_TYPE type;

    private String value;

    private int position;

    public boolean is(String value) {
        return this.value.equals(value);
    }

    public boolean is(TOKEN_TYPE type) {
        return this.type.equals(type);
    }

    public String value() {
        return value;
    }

    public TOKEN_TYPE type() {
        return type;
    }

    public int position() {
        return position;
    }

    public int length() {
        return value.length();
    }

    @Override
    public String toString() {
        return String.format("%4d %18s  %s", position, type, value);
    }
}
