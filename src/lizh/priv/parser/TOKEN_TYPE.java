package lizh.priv.parser;

public enum TOKEN_TYPE {

    /**
     * 数字
     */
    NUMBER,
    /**
     * 字符串
     */
    STRING,
    /**
     * 操作符+
     */
    OPERATOR_ADD("+"),
    /**
     * 操作符+
     */
    OPERATOR_SUBTRACT("-"),
    /**
     * 操作符+
     */
    OPERATOR_MULTIPLY("*"),
    /**
     * 操作符+
     */
    OPERATOR_DIVIDE("/"),
    /**
     * 左括号
     */
    BRACKET_LEFT("("),
    /**
     * 右括号
     */
    BRACKET_RIGHT(")"),
    /**
     * 逗号
     */
    COMMA(","),
    /**
     * 标识符
     */
    IDENTITY;

    private String value = null;

    private TOKEN_TYPE() {
    }

    private TOKEN_TYPE(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean hasValue() {
        return value != null;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}