package lizh.priv.parser;

import java.util.ArrayDeque;
import java.util.Deque;

public class Lexer {

    private final String expression;

    private int position = 0;

    private Token current;

    public Lexer(String expression) {
        this.expression = expression;
        if (expression == null || "".equals(expression.trim())) {
            exception("空表达式", -1);
        }
    }

    private void exception(String msg, int index) {
        throw new ParseException(msg, index);
    }

    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private Token getToken_Number(final int pos) {
        String errorMsg = "不正确的数字形式";
        StringBuilder num = new StringBuilder();
        for (int i = pos; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (isDigit(c) || c == '.') {
                num.append(c);
            } else if (isLetter(c)) {
                exception(errorMsg, pos);
            } else {
                break;
            }
        }

        String strNum = num.toString();
        if (strNum.charAt(0) == '0' && strNum.length() >= 2 && strNum.charAt(1) != '.') {
            exception(errorMsg, pos);
        }
        int pIndx = strNum.indexOf('.');
        if (pIndx != -1) { // 包含小数点
            if (pIndx != strNum.lastIndexOf('.')) {
                exception(errorMsg, pos);
            }
            if (pIndx == strNum.length() - 1) {
                exception(errorMsg, pos);
            }
        }

        return new Token(TOKEN_TYPE.NUMBER, strNum, pos);
    }

    private Token getToken_String(final int pos) {
        boolean escaped = false;
        StringBuilder str = new StringBuilder("\"");
        for (int i = pos + 1; i < expression.length(); i++) {
            char c = expression.charAt(i);
            str.append(c);

            if (escaped) {
                if (c == '\\' || c == '"') {
                    escaped = false;
                } else {
                    exception("无效的转义字符'\\'", pos + i);
                }
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '"') {
                return new Token(TOKEN_TYPE.STRING, str.toString(), pos);
            }
        }

        exception("未结束的字符串", pos);
        return null;
    }

    private Token getToken_Identity(final int pos) {
        StringBuilder id = new StringBuilder();
        for (int i = pos; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (isLetter(c) || isDigit(c)) {
                id.append(c);
            } else {
                break;
            }
        }
        return new Token(TOKEN_TYPE.IDENTITY, id.toString(), pos);
    }

    public Token next() {

        if (position >= expression.length()) {
            current = null;
            return null;
        }

        char c = expression.charAt(position);

        Token token = null;
        if (isLetter(c)) {
            token = getToken_Identity(position);
            position += token.length();
        } else if (isDigit(c)) {
            token = getToken_Number(position);
            position += token.length();
        } else if (c == '+') {
            token = new Token(TOKEN_TYPE.OPERATOR_ADD, position);
            position++;
        } else if (c == '-') {
            token = new Token(TOKEN_TYPE.OPERATOR_SUBTRACT, position);
            position++;
        } else if (c == '*') {
            token = new Token(TOKEN_TYPE.OPERATOR_MULTIPLY, position);
            position++;
        } else if (c == '/') {
            token = new Token(TOKEN_TYPE.OPERATOR_DIVIDE, position);
            position++;
        } else if (c == '(') {
            token = new Token(TOKEN_TYPE.BRACKET_LEFT, position);
            position++;
        } else if (c == ')') {
            token = new Token(TOKEN_TYPE.BRACKET_RIGHT, position);
            position++;
        } else if (c == ',') {
            token = new Token(TOKEN_TYPE.COMMA, c, position);
            position++;
        } else if (c == '"') {
            token = getToken_String(position);
            position += token.length();
        } else if (c == ' ') {
            position++;
            return next();
        } else if (c == '.') {
            exception("小数点不应该出现的这个位置", position);
        } else if (c == '\\') {
            exception("转义符'\\'不应该出现的这个位置", position);
        } else {
            exception("非法字符：" + c, position);
        }

        current = token;

        return token;
    }

    public Token lookAhead() {
        return lookAhead(1);
    }

    public Token lookAhead(int n) {
        int p = position;
        Token token = null;
        for (int i = 0; i < n; i++) {
            token = next();
        }
        position = p;
        return token;
    }

    public boolean aheadWith(TOKEN_TYPE type) {
        Token token = lookAhead();
        return token != null && token.type() == type;
    }

    public boolean aheadWith(TOKEN_TYPE type1, TOKEN_TYPE type2) {
        Token token1 = lookAhead();
        Token token2 = lookAhead();
        return token1 != null && token1.type() == type1 && token2 != null && token2.type() == type2;
    }

    public boolean isEnd() {
        return lookAhead() == null;
    }

    public int position() {
        return position;
    }

    private Deque<Integer> resetPoints = new ArrayDeque<>();

    public void setResetPoint() {
        resetPoints.push(position);
    }

    public void reset() {
        position = resetPoints.peekFirst();
    }

    public void clearResetPoint() {
        resetPoints.pop();
    }

    public Token current() {
        return current;
    }
}
