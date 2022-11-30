package lizh.priv.parser.ast;

import lizh.priv.parser.Token;

public class NodeFactory {

    public static Node<?> create(Token token) {
        String value = token.value();
        switch (token.type()) {
        case OPERATOR_ADD:
        case OPERATOR_DIVIDE:
        case OPERATOR_MULTIPLY:
        case OPERATOR_SUBTRACT:
            return new OperatorNode(value);
        case STRING:
            return new StringNode(unquote(value));
        case NUMBER:
            return new NumberNode(Double.parseDouble(value));
        case IDENTITY:
            return new IdentityNode(value);
        default:
            return null;
        }
    }

    public static SimpleNode createSimpleNode() {
        return new SimpleNode();
    }

    public static FunctionNode createFunctionNode(String funcName) {
        return new FunctionNode(funcName);
    }

    private static String unquote(String str) {
        return str.substring(1, str.length() - 1);
    }
}
