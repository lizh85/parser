package lizh.priv.parser.execute;

import java.util.List;
import java.util.stream.Collectors;

import lizh.priv.parser.ast.FunctionNode;
import lizh.priv.parser.ast.IdentityNode;
import lizh.priv.parser.ast.Node;
import lizh.priv.parser.ast.NumberNode;
import lizh.priv.parser.ast.OperatorNode;
import lizh.priv.parser.ast.StringNode;

public class Executor {

    private final Node<?> ast;

    private final Context context;

    private Executor(Node<?> ast, Context context) {
        this.ast = ast;
        this.context = context;
    }

    public static SDValue execute(Node<?> ast, Context context) {
        return new Executor(ast, context).execute();
    }

    private SDValue execute() {
        return getValue(ast);
    }

    private SDValue getValue(Node<?> node) {
        if (node instanceof StringNode) {
            return SDValue.valueOf(node.getValue());
        } else if (node instanceof NumberNode) {
            return SDValue.valueOf(node.getValue());
        } else if (node instanceof IdentityNode) {
            return context.getIdentity(node.getValue().toString());
        } else if (node instanceof OperatorNode) {
            String operator = node.getValue().toString();
            switch (operator) {
            case "+":
                return add(node);
            case "-":
                return subtract(node);
            case "*":
                return multiply(node);
            case "/":
                return divide(node);
            }
        } else if (node instanceof FunctionNode) {
            String funcName = node.getValue().toString();
            Function func = context.getFunction(funcName);
            List<SDValue> args = node.getChildren().stream().map(this::getValue).collect(Collectors.toList());
            return func.call(args);
        }
        return null;
    }

    /**
     * 加
     */
    private SDValue add(Node<?> node) {
        List<Node<?>> children = node.getChildren();
        SDValue left = getValue(children.get(0));
        SDValue right = getValue(children.get(1));
        if (left.isDouble()) {
            return SDValue.valueOf(left.getAsDouble() + right.getAsDouble());
        }
        return SDValue.valueOf(left.getAsString() + right.getAsString());
    }

    /**
     * 减
     */
    private SDValue subtract(Node<?> node) {
        List<Node<?>> children = node.getChildren();
        SDValue left = getValue(children.get(0));
        if (children.size() > 1) {
            SDValue right = getValue(children.get(1));
            return SDValue.valueOf(left.getAsDouble() - right.getAsDouble());
        }
        return SDValue.valueOf(-left.getAsDouble());
    }

    /**
     * 乘
     */
    private SDValue multiply(Node<?> node) {
        List<Node<?>> children = node.getChildren();
        SDValue left = getValue(children.get(0));
        SDValue right = getValue(children.get(1));
        return SDValue.valueOf(left.getAsDouble() * right.getAsDouble());
    }

    /**
     * 除
     */
    private SDValue divide(Node<?> node) {
        List<Node<?>> children = node.getChildren();
        SDValue left = getValue(children.get(0));
        SDValue right = getValue(children.get(1));
        return SDValue.valueOf(left.getAsDouble() / right.getAsDouble());
    }
}
