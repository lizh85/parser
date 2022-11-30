package lizh.priv.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lizh.priv.parser.ast.FunctionNode;
import lizh.priv.parser.ast.Node;
import lizh.priv.parser.ast.NodeFactory;

/**
 * 此parser用于验证表达式是否正确，同时生成对应的抽象语法树。
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
public class Parser2 {

    private final Lexer lexer;

    private Node<?> tryNode;

    public Parser2(String expression) {
        lexer = new Lexer(expression);
    }

    public Node<?> parse() {
        Node<?> node = expression();
        if (!lexer.isEnd()) {
            throw exception("表达式不正确");
        }

        return node;
    }

    /**
     * Expression -> {"-"}? Term { Addop Term }*
     */
    private Node<?> expression() {
        _try(TOKEN_TYPE.OPERATOR_SUBTRACT);
        Node<?> subtractNode = tryNode;

        Node<?> leftTermNode = term();
        if (subtractNode != null) {
            subtractNode.addChild(leftTermNode);
            leftTermNode = subtractNode;
        }

        while (_try(this::addop)) {
            Node<?> addopNode = tryNode;
            Node<?> rightTermNode = term();

            addopNode.addChild(leftTermNode, rightTermNode);
            leftTermNode = addopNode;
        }

        return leftTermNode;
    }

    /**
     * Addop -> "+" | "-"
     */
    private Node<?> addop() {
        acceptOneOf(TOKEN_TYPE.OPERATOR_ADD, TOKEN_TYPE.OPERATOR_SUBTRACT);
        return NodeFactory.create(lexer.current());
    }

    /**
     * Term -> Factor { Mulop Factor }*
     */
    private Node<?> term() {
        Node<?> leftFactorNode = factor();

        while (_try(this::mulop)) {
            Node<?> mulopNode = tryNode;
            Node<?> rightFactorNode = factor();

            mulopNode.addChild(leftFactorNode, rightFactorNode);
            leftFactorNode = mulopNode;
        }

        return leftFactorNode;
    }

    /**
     * Mulop -> "*" | "/"
     */
    private Node<?> mulop() {
        acceptOneOf(TOKEN_TYPE.OPERATOR_MULTIPLY, TOKEN_TYPE.OPERATOR_DIVIDE);
        return NodeFactory.create(lexer.current());
    }

    /**
     * Factor -> Literal | Function | Identity | "(" Expression ")"
     */
    private Node<?> factor() {
        if (_try(this::literal) || _try(this::function) || _try(this::identity)) {
            return tryNode;
        } else if (_try(TOKEN_TYPE.BRACKET_LEFT)) {
            Node<?> node = expression();
            accept(TOKEN_TYPE.BRACKET_RIGHT);
            return node;
        }

        throw exception("表达式不正确");
    }

    private Node<?> identity() {
        accept(TOKEN_TYPE.IDENTITY);
        return NodeFactory.create(lexer.current());
    }

    /**
     * Literal = Number | String
     */
    private Node<?> literal() {
        if (_try(TOKEN_TYPE.NUMBER) || _try(TOKEN_TYPE.STRING)) {
            return NodeFactory.create(lexer.current());
        }

        throw exception("期待数字或字符串");
    }

    /**
     * Function = Identity "(" Params ")"
     */
    private Node<?> function() {
        identity();
        FunctionNode funcNode = NodeFactory.createFunctionNode(lexer.current().value());

        accept(TOKEN_TYPE.BRACKET_LEFT);
        try {
            funcNode.addChildren(params());
            accept(TOKEN_TYPE.BRACKET_RIGHT);
        } catch (ParseException e) {
            e.setIgnorable(false);
            throw e;
        }

        return funcNode;
    }

    /**
     * Params = Expression { "," Expression }*
     */
    private List<Node<?>> params() {
        if (lexer.aheadWith(TOKEN_TYPE.BRACKET_RIGHT)) { // 参数为空
            return null;
        }

        List<Node<?>> nodes = new ArrayList<>();
        nodes.add(expression());

        while (_try(TOKEN_TYPE.COMMA)) {
            nodes.add(expression());
        }

        return nodes;
    }

    private ParseException exception(String message) {
        return new ParseException(message, lexer.position());
    }

    private void accept(TOKEN_TYPE type) {
        Token token = lexer.next();
        if (token != null && token.is(type)) {
            return;
        }
        throw exception("期待" + type);
    }

    private void acceptOneOf(TOKEN_TYPE... types) {
        Token token = lexer.next();
        if (token != null && Arrays.stream(types).anyMatch(t -> token.type() == t)) {
            return;
        }
        throw exception("期待" + Arrays.stream(types).map(t -> "'" + t + "'").collect(Collectors.joining("或")));
    }

    private boolean _try(TryFunction2 func) {
        try {
            lexer.setResetPoint();
            tryNode = (Node<?>) func.call();
            return true;
        } catch (ParseException e) {
            tryNode = null;
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
            tryNode = NodeFactory.create(lexer.next());
            return true;
        }

        tryNode = null;
        return false;
    }

}
