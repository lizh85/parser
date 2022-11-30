package lizh.priv.parser;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 此parser用于验证表达式是否正确。
 *
 * <p>
 * <b>BNF:</b>
 * </p>
 * <pre>
 * Expression ->  {"-"}? Term { Addop Term }* <BR>
 * Addop      ->  "+" | "-" <BR>
 * Term       ->  Factor { Mulop Factor }* <BR>
 * Mulop      ->  "*" | "/" <BR>
 * Factor     ->  Literal | Function | Identity | "(" Expression ")" <BR>
 * Literal    ->  Number | String <BR>
 * Function   ->  Identity "(" Params ")" <BR>
 * Params     ->  Expression { "," Expression }*
 * </pre>
 *
 */
public class Parser {

    private final Lexer lexer;

    public Parser(String expression) {
        lexer = new Lexer(expression);
    }

    public void parse() {
        expression();
        if (!lexer.isEnd()) {
            exception("表达式不正确");
        }
    }

    /**
     * Expression -> {"-"}? Term { Addop Term }*
     */
    private void expression() {
        _try(TOKEN_TYPE.OPERATOR_SUBTRACT);

        term();

        while (_try(this::addop)) {
            term();
        }
    }

    /**
     * Addop -> "+" | "-"
     */
    private void addop() {
        acceptOneOf(TOKEN_TYPE.OPERATOR_ADD, TOKEN_TYPE.OPERATOR_SUBTRACT);
    }

    /**
     * Term -> Factor { Mulop Factor }*
     */
    private void term() {
        factor();

        while (_try(this::mulop)) {
            factor();
        }
    }

    /**
     * Mulop -> "*" | "/"
     */
    private void mulop() {
        acceptOneOf(TOKEN_TYPE.OPERATOR_MULTIPLY, TOKEN_TYPE.OPERATOR_DIVIDE);
    }

    /**
     * Factor -> Literal | Function | Identity | "(" Expression ")"
     */
    private void factor() {
        if (_try(this::literal) || _try(this::function) || _try(this::identity)) {
            return;
        } else if (_try(TOKEN_TYPE.BRACKET_LEFT)) {
            expression();
            accept(TOKEN_TYPE.BRACKET_RIGHT);
            return;
        }

        exception("表达式不正确");
    }

    private void identity() {
        accept(TOKEN_TYPE.IDENTITY);
    }

    /**
     * Literal = Number | String
     */
    private void literal() {
        if (_try(TOKEN_TYPE.NUMBER) || _try(TOKEN_TYPE.STRING)) {
            return;
        }

        exception("期待数字或字符串");
    }

    /**
     * Function = Identity "(" Params ")"
     */
    private void function() {
        identity();
        accept(TOKEN_TYPE.BRACKET_LEFT);
        try {
            params();
            accept(TOKEN_TYPE.BRACKET_RIGHT);
        } catch (ParseException e) {
            e.setIgnorable(false);
            throw e;
        }
    }

    /**
     * Params = Expression { "," Expression }*
     */
    private void params() {
        if (lexer.aheadWith(TOKEN_TYPE.BRACKET_RIGHT)) { // 参数为空
            return;
        }

        expression();

        while (_try(TOKEN_TYPE.COMMA)) {
            expression();
        }
    }

    private void exception(String message) {
        throw new ParseException(message, lexer.position());
    }

    private void accept(TOKEN_TYPE type) {
        Token token = lexer.next();
        if (token != null && token.is(type)) {
            return;
        }
        exception("期待" + type);
    }

    private void acceptOneOf(TOKEN_TYPE... types) {
        Token token = lexer.next();
        if (token != null && Arrays.stream(types).anyMatch(t -> token.type() == t)) {
            return;
        }
        exception("期待" + Arrays.stream(types).map(t -> "'" + t + "'").collect(Collectors.joining("或")));
    }

    private boolean _try(TryFunction func) {
        try {
            lexer.setResetPoint();
            func.call();
            return true;
        } catch (ParseException e) {
            if (e.isIgnorable()) {
                lexer.reset();
                return false;
            }
            throw e;
        } finally {
            lexer.clearResetPoint();
        }
    }

    private boolean _try(TOKEN_TYPE type) {
        if (lexer.aheadWith(type)) {
            lexer.next();
            return true;
        }

        return false;
    }

}
