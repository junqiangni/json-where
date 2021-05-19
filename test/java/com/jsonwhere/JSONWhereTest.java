package com.jsonwhere;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JSONWhereTest {

    @Test
    void testTetWhereExpression() throws JSQLParserException {
        String whereSql = "a=10.12";
        Expression expression1 = JSONWhere.getWhereExpression(whereSql);
        Assertions.assertTrue("a = 10.12".equals(expression1.toString()));
        EqualsTo equalsTo = (EqualsTo) expression1;
        equalsTo.getStringExpression();
        Column columnleft = (Column) equalsTo.getLeftExpression();
        Assertions.assertTrue("a".equals(columnleft.getColumnName()));
        String whereSql1 = "a=2 and d like 'he%' or a =1";
        Expression expression2 = JSONWhere.getWhereExpression(whereSql1);
        Assertions.assertTrue("a = 2 AND d LIKE 'he%' OR a = 1".equals(expression2.toString()));
    }
    @Test
    void testEqualWhereExpression() throws Exception {
        String jsonStr = "{a:1,d:{c:1.23},c:'hello'}";
        Assertions.assertTrue(JSONWhere.isJsonWhereData(jsonStr,"a=1"));
        Assertions.assertTrue(JSONWhere.isJsonWhereData(jsonStr,"d.c=1.23"));
        Assertions.assertTrue(JSONWhere.isJsonWhereData(jsonStr,"c=hello"));
        Assertions.assertFalse(JSONWhere.isJsonWhereData(jsonStr,"d.t=1"));
        Assertions.assertFalse(JSONWhere.isJsonWhereData(jsonStr,"c=1"));
    }

    @Test
    void testAndEqualWhereExpression() throws Exception {
        String jsonStr = "{a:1,d:{c:1.23},c:'hell0'}";
        Assertions.assertTrue(JSONWhere.isJsonWhereData(jsonStr,"a=1 and d.c=1.23"));
        Assertions.assertFalse(JSONWhere.isJsonWhereData(jsonStr," c = 1 and a=1 "));
    }
    @Test
    void testOrEqualWhereExpression() throws Exception {
        String jsonStr = "{a:1,d:{c:1.23},c:'hell0'}";
        Assertions.assertTrue(JSONWhere.isJsonWhereData(jsonStr,"a=1 or d.t=1.23"));
        Assertions.assertFalse(JSONWhere.isJsonWhereData(jsonStr," c = 1 or a=2 "));
    }

    @Test
    void testGreaterThanWhereExpression() throws Exception {
        String jsonStr = "{a:1,d:{c:1.23},c:'hell0'}";
        Assertions.assertTrue(JSONWhere.isJsonWhereData(jsonStr,"a>0"));
        Assertions.assertFalse(JSONWhere.isJsonWhereData(jsonStr,"d.c>2"));
       // Assertions.assertFalse(JSONWhere.isJsonWhereData(jsonStr," c = 1 or a=2 "));
    }

    @Test
    void testMinorThanWhereExpression() throws Exception {
        String jsonStr = "{a:1,d:{c:1.23},c:'hell0'}";
        Assertions.assertFalse(JSONWhere.isJsonWhereData(jsonStr,"a<0"));
        Assertions.assertFalse(JSONWhere.isJsonWhereData(jsonStr,"d.c<1.11"));
        Assertions.assertTrue(JSONWhere.isJsonWhereData(jsonStr,"a<2"));
    }

    @Test
    void testLikeWhereExpression() throws Exception {
        String jsonStr = "{a:1,d:{c:1.23},c:'hello'}";
        Assertions.assertTrue(JSONWhere.isJsonWhereData(jsonStr,"c like hel%"));
        Assertions.assertFalse(JSONWhere.isJsonWhereData(jsonStr,"c like helde%"));
        //  %el% %l0 表达式不支持
//        Assertions.assertFalse(JSONWhere.isJsonWhereData(jsonStr,"c like '%el%"));
       // Assertions.assertFalse(JSONWhere.isJsonWhereData(jsonStr,"c like  %l0"));
    }
}
