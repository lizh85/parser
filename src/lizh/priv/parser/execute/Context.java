package lizh.priv.parser.execute;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private final Map<String, SDValue> identityMap = new HashMap<>();

    private final Map<String, Function> functionMap = new HashMap<>();

    public Context addIdentity(String name, String value) {
        identityMap.put(name, SDValue.valueOf(value));
        return this;
    }

    public Context addIdentity(String name, Double value) {
        identityMap.put(name, SDValue.valueOf(value));
        return this;
    }

    public Context addFunction(String name, Function func) {
        functionMap.put(name, func);
        return this;
    }

    public SDValue getIdentity(String name) {
        return identityMap.get(name);
    }

    public Function getFunction(String name) {
        return functionMap.get(name);
    }

}
