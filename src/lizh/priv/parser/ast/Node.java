package lizh.priv.parser.ast;

import java.util.List;

import org.json.simple.JSONObject;

public interface Node<V> {

    public V getValue();

    public List<Node<?>> getChildren();

    public void addChild(Node<?>... child);

    public void addChildren(List<Node<?>> list);

    public JSONObject toJson();
}
