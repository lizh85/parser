# SimpleParser

#### 描述
- 简单的表达式引擎
  + 支持四则运算
  + 支持字符串字面量
  + 支持函数

#### 例子

```
package lizh.priv.parser.execute;

import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import lizh.priv.parser.Parser2;

public class ExecutorTest {

    private static final double delta = 0.00000000000001;

    private Function FUNC_SUM = args -> {
        return SDValue.valueOf(args.stream().mapToDouble(SDValue::getAsDouble).sum());
    };

    private Function FUNC_CONCAT = args -> {
        return SDValue.valueOf(args.stream().map(SDValue::getAsString).collect(Collectors.joining()));
    };

    @Test
    public void test1() {
        String exp = "4-1*5/2+6";
        Parser2 parser = new Parser2(exp);

        SDValue result = Executor.execute(parser.parse(), new Context());
        Assert.assertEquals(7.5d, result.getAsDouble(), delta);
    }

    @Test
    public void test2() {
        String exp = "3+4*(93.5+PI) + 1/(6-0.5)";
        Parser2 parser = new Parser2(exp);

        Context context = new Context();
        context.addIdentity("PI", 3.14d);

        SDValue result = Executor.execute(parser.parse(), context);
        Assert.assertEquals(389.7418181818182d, result.getAsDouble(), delta);
    }

    @Test
    public void test3() {
        String exp = "5 + sum(1, 2, PI)";
        Parser2 parser = new Parser2(exp);

        Context context = new Context();
        context.addIdentity("PI", 3.14);
        context.addFunction("sum", FUNC_SUM);

        SDValue result = Executor.execute(parser.parse(), context);
        Assert.assertEquals(11.14d, result.getAsDouble(), delta);
    }

    @Test
    public void test4() {
        String exp = "\"66\" + 77";
        Parser2 parser = new Parser2(exp);

        SDValue result = Executor.execute(parser.parse(), new Context());
        Assert.assertEquals("6677", result.getAsString());
    }

    @Test
    public void test5() {
        String exp = "concat(sum(1, 2, PI), \"hello\")";
        Parser2 parser = new Parser2(exp);

        Context context = new Context();
        context.addIdentity("PI", 3.14);
        context.addFunction("sum", FUNC_SUM);
        context.addFunction("concat", FUNC_CONCAT);

        SDValue result = Executor.execute(parser.parse(), context);
        Assert.assertEquals("6.14hello", result.getAsString());
    }
}
```
