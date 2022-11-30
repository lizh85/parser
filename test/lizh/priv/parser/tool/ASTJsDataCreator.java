package lizh.priv.parser.tool;

import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONValue;

import lizh.priv.parser.Parser2;
import lizh.priv.parser.ast.Node;

public class ASTJsDataCreator {

    public static void main(String[] args) throws Exception {
        String exp = "min(8*6-max(4, 0), 55)-6/3-4*5/2-1";
        Parser2 parser = new Parser2(exp);
        Node<?> node = parser.parse();

        Path path = Paths.get("html", "data.js");
        try (BufferedWriter out = Files.newBufferedWriter(path, Charset.forName("UTF-8"))) {
            out.write(String.format("window.exp = \"%s\"; \nwindow.data = %s;", JSONValue.escape(exp),
                    node.toJson().toString()));
        }

    }
}
