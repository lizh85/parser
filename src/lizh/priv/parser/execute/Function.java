package lizh.priv.parser.execute;

import java.util.List;

@FunctionalInterface
public interface Function {

    public SDValue call(List<SDValue> args);
}
