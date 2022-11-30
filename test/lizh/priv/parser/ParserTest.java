package lizh.priv.parser;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class ParserTest {

    @Test
    public void test() throws Exception {
        Map<String, Boolean> exps = new LinkedHashMap<>();
        exps.put("3+4*(93.5+PI) + 1/(6-0.5)", true);
        exps.put("3+4*(93.5+PI) + 1/(6-0.5)*PI+2.3", true);
        exps.put("5+sum(1, 2, 3)-PI", true);
        exps.put("3+\"test\\\"666\"+4*(93.5+PI) + 1/(6-0.5)", true);
        exps.put("5-test(3+2)", true);
        exps.put("5-test*(3+2)", true);
        exps.put("3+4*(-6+93.5)", true);
        exps.put("-MAX(6, 8)", true);
        exps.put("-5", true);
        exps.put("-PI+3", true);
        exps.put("1+(-6/PI) - sum(-6, -PI)", true);
        exps.put("1+(-6/PI) - sum(-6, -(PI))", true);

        exps.put("5-test(3-2)9", false);
        exps.put("5-test(3-2,)", false);
        exps.put("1--6", false);
        exps.put("1+-6", false);

        exps.forEach((exp, result) -> {
            Parser2 parser = new Parser2(exp);
            boolean isOK;
            try {
                parser.parse();
                isOK = true;
            } catch (Exception e) {
                isOK = false;
                // System.out.println(exp + " : " + e.getMessage());
            }

            if (isOK != result) {
                System.out.println("与期待结果不符： " + exp + "  ->  " + result);
            }
        });

        exps.forEach((exp, result) -> {
            Parser parser = new Parser(exp);
            boolean isOK;
            try {
                parser.parse();
                isOK = true;
            } catch (Exception e) {
                isOK = false;
                // System.out.println(exp + " : " + e.getMessage());
            }

            if (isOK != result) {
                System.out.println("与期待结果不符： " + exp + "  ->  " + result);
            }
        });
    }
}
