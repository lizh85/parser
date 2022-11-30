package lizh.priv.parser.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public abstract class AbstractNode<V> implements Node<V> {

    private V value;

    private List<Node<?>> children = new ArrayList<>();

    public AbstractNode(V value) {
        this.value = value;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public List<Node<?>> getChildren() {
        return children;
    }

    @Override
    public void addChild(Node<?>... child) {
        if (child != null) {
            children.addAll(Arrays.asList(child));
        }
    }

    @Override
    public void addChildren(List<Node<?>> list) {
        if (list != null) {
            children.addAll(list);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", this.getClass().getSimpleName());
        json.put("name", value.toString());
        json.put("value", value.toString());
        if (!children.isEmpty()) {
            JSONArray array = new JSONArray();
            children.forEach(c -> {
                array.add(c.toJson());
            });

            json.put("children", array);
        }

        return json;
    }

}
