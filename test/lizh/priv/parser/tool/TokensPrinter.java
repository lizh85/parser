package lizh.priv.parser.tool;

import lizh.priv.parser.Lexer;
import lizh.priv.parser.Token;

public class TokensPrinter {

    public static void main(String[] args) throws Exception {
        test1();
    }

    public static void test1() throws Exception {
        String expr = "3+\"test\\\"666\"+4*(93.5+PI) + 1/(6-0.5)";
        Lexer lexer = new Lexer(expr);
        Token token = null;
        System.out.println(expr);
        System.out.println("--------------------------");
        while ((token = lexer.next()) != null) {
            System.out.println(token.toString());
        }
    }

    public static void test2() throws Exception {
        String expr = "3+\"test\\666\"+4*(93.5+PI) + 1/(6-0.5)";
        Lexer lexer = new Lexer(expr);
        Token token = null;
        System.out.println(expr);
        System.out.println("--------------------------");
        while ((token = lexer.next()) != null) {
            System.out.println(token.toString());
        }
    }

    public static void test3() throws Exception {
        String expr = "3+\"test\\\"+4*(93.5+PI) + 1/(6-0.5)";
        Lexer lexer = new Lexer(expr);
        Token token = null;
        System.out.println(expr);
        System.out.println("--------------------------");
        while ((token = lexer.next()) != null) {
            System.out.println(token.toString());
        }
    }

    public static void test4() throws Exception {
        String expr = "3+\\ 5 - 6";
        Lexer lexer = new Lexer(expr);
        Token token = null;
        System.out.println(expr);
        System.out.println("--------------------------");
        while ((token = lexer.next()) != null) {
            System.out.println(token.toString());
        }
    }

    public static void test5() throws Exception {
        String expr = "5+sum(1, 2, 3)-PI";
        Lexer lexer = new Lexer(expr);
        Token token = null;
        System.out.println(expr);
        System.out.println("--------------------------");
        while ((token = lexer.next()) != null) {
            System.out.println(token.toString());
        }
    }
}
