package com.jsonwhere;

import com.alibaba.fastjson.JSONObject;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

public class JSONDataExpressionVisitor implements ExpressionVisitor {

    private final JSONObject dataObject;
    private boolean result;

    public JSONDataExpressionVisitor(JSONObject data) {
        dataObject = data;
        result = false;
    }

    public boolean getResult() {
        return result;
    }

    /**
     * 根据key获取JsonData中的值
     *
     * @param key
     * @return
     */
    private Object getJSONDataValue(String key) {
        if (!key.contains(".")) {
            return dataObject.get(key);
        }
        String[] tArr = key.split("\\.");
        JSONObject temp = dataObject;
        int length = tArr.length;
        for (int i = 0; i < length - 1; i++) {
            temp = temp.getJSONObject(tArr[i]);
            if (temp == null) {
                result = false;
                return null;
            }
        }
        return temp.get(tArr[length - 1]);
    }

    @Override
    public void visit(BitwiseRightShift aThis) {

    }

    @Override
    public void visit(BitwiseLeftShift aThis) {

    }

    @Override
    public void visit(NullValue nullValue) {

    }

    @Override
    public void visit(Function function) {

    }

    @Override
    public void visit(SignedExpression signedExpression) {

    }

    @Override
    public void visit(JdbcParameter jdbcParameter) {

    }

    @Override
    public void visit(JdbcNamedParameter jdbcNamedParameter) {

    }

    @Override
    public void visit(DoubleValue doubleValue) {

    }

    @Override
    public void visit(LongValue longValue) {

    }

    @Override
    public void visit(HexValue hexValue) {

    }

    @Override
    public void visit(DateValue dateValue) {

    }

    @Override
    public void visit(TimeValue timeValue) {

    }

    @Override
    public void visit(TimestampValue timestampValue) {

    }

    @Override
    public void visit(Parenthesis parenthesis) {

    }

    @Override
    public void visit(StringValue stringValue) {

    }

    @Override
    public void visit(Addition addition) {

    }

    @Override
    public void visit(Division division) {

    }

    @Override
    public void visit(IntegerDivision division) {

    }

    @Override
    public void visit(Multiplication multiplication) {

    }

    @Override
    public void visit(Subtraction subtraction) {

    }

    @Override
    public void visit(AndExpression andExpression) {
        andExpression.getLeftExpression().accept(this);
        if (getResult()) {
            andExpression.getRightExpression().accept(this);
        }
    }

    @Override
    public void visit(OrExpression orExpression) {
        orExpression.getLeftExpression().accept(this);
        if (!getResult()) {
            orExpression.getRightExpression().accept(this);
        }
    }

    @Override
    public void visit(Between between) {

    }

    @Override
    public void visit(EqualsTo equalsTo) {
        String valueKey = equalsTo.getLeftExpression().toString();
        Object value = getJSONDataValue(valueKey);
        if (value == null) {
            result = false;
            return;
        }
        if (equalsTo.getRightExpression() == null) {
            result = false;
            return;
        }
        if (value.toString().equals(equalsTo.getRightExpression().toString())) {
            result = true;
        } else {
            result = false;
        }
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        Object value = getJSONDataValue(greaterThan.getLeftExpression().toString());
        if (value == null) {
            result = false;
            return;
        }
        Expression rightExpression = greaterThan.getRightExpression();
        if (rightExpression == null) {
            result = false;
            return;
        }
        //数字比较
        if (rightExpression instanceof DoubleValue || rightExpression instanceof LongValue) {
            double n = Double.valueOf(value.toString());
            if (rightExpression instanceof DoubleValue) {
                if (n > ((DoubleValue) rightExpression).getValue()) {
                    result = true;
                    return;
                }
            } else {
                if (n > ((LongValue) rightExpression).getValue()) {
                    result = true;
                    return;
                }
            }
            //时间比较
        } else if (rightExpression instanceof TimeValue
                || rightExpression instanceof TimestampValue) {
            if (value.toString().compareTo(((StringValue) rightExpression).getValue()) > 1) {
                result = true;
                return;
            }
            //字符串
        } else if (rightExpression instanceof StringValue) {
            if (value.toString().compareTo(((StringValue) rightExpression).getValue()) > 1) {
                result = true;
                return;
            }
            //其他
        }
        result = false;
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {

    }

    @Override
    public void visit(InExpression inExpression) {

    }

    @Override
    public void visit(FullTextSearch fullTextSearch) {

    }

    @Override
    public void visit(IsNullExpression isNullExpression) {

    }

    @Override
    public void visit(IsBooleanExpression isBooleanExpression) {

    }

    @Override
    public void visit(LikeExpression likeExpression) {
        String valueKey = likeExpression.getLeftExpression().toString();
        Object value = getJSONDataValue(valueKey);
        if (value == null) {
            result = false;
            return;
        }
        if (likeExpression.getRightExpression() == null) {
            result = false;
            return;
        }
        String regExStr=likeExpression.getRightExpression().toString();
        String valueStr = value.toString();
        if (valueStr.startsWith(regExStr)){
            result = likeExpression.isNot()?false:true;;
            return;
        }
        result = false;
    }

    @Override
    public void visit(MinorThan minorThan) {
        Object value = getJSONDataValue(minorThan.getLeftExpression().toString());
        if (value == null) {
            result = false;
            return;
        }
        Expression rightExpression = minorThan.getRightExpression();
        if (rightExpression == null) {
            result = false;
            return;
        }
        //数字比较
        if (rightExpression instanceof DoubleValue || rightExpression instanceof LongValue) {
            double n = Double.valueOf(value.toString());
            if (rightExpression instanceof DoubleValue) {
                if (n < ((DoubleValue) rightExpression).getValue()) {
                    result = true;
                    return;
                }
            } else {
                if (n < ((LongValue) rightExpression).getValue()) {
                    result = true;
                    return;
                }
            }
            //时间比较
        } else if (rightExpression instanceof TimeValue
                || rightExpression instanceof TimestampValue) {
            if (value.toString().compareTo(((StringValue) rightExpression).getValue()) < 0) {
                result = true;
                return;
            }
            //字符串
        } else if (rightExpression instanceof StringValue) {
            if (value.toString().compareTo(((StringValue) rightExpression).getValue()) < 0) {
                result = true;
                return;
            }
            //其他
        }
        result = false;
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {

    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {

    }

    @Override
    public void visit(Column tableColumn) {

    }

    @Override
    public void visit(SubSelect subSelect) {

    }

    @Override
    public void visit(CaseExpression caseExpression) {

    }

    @Override
    public void visit(WhenClause whenClause) {

    }

    @Override
    public void visit(ExistsExpression existsExpression) {

    }

    @Override
    public void visit(AllComparisonExpression allComparisonExpression) {

    }

    @Override
    public void visit(AnyComparisonExpression anyComparisonExpression) {

    }

    @Override
    public void visit(Concat concat) {

    }

    @Override
    public void visit(Matches matches) {

    }

    @Override
    public void visit(BitwiseAnd bitwiseAnd) {

    }

    @Override
    public void visit(BitwiseOr bitwiseOr) {

    }

    @Override
    public void visit(BitwiseXor bitwiseXor) {

    }

    @Override
    public void visit(CastExpression cast) {

    }

    @Override
    public void visit(Modulo modulo) {

    }

    @Override
    public void visit(AnalyticExpression aexpr) {

    }

    @Override
    public void visit(ExtractExpression eexpr) {

    }

    @Override
    public void visit(IntervalExpression iexpr) {

    }

    @Override
    public void visit(OracleHierarchicalExpression oexpr) {

    }

    @Override
    public void visit(RegExpMatchOperator rexpr) {

    }

    @Override
    public void visit(JsonExpression jsonExpr) {

    }

    @Override
    public void visit(JsonOperator jsonExpr) {

    }

    @Override
    public void visit(RegExpMySQLOperator regExpMySQLOperator) {

    }

    @Override
    public void visit(UserVariable var) {

    }

    @Override
    public void visit(NumericBind bind) {

    }

    @Override
    public void visit(KeepExpression aexpr) {

    }

    @Override
    public void visit(MySQLGroupConcat groupConcat) {

    }

    @Override
    public void visit(ValueListExpression valueList) {

    }

    @Override
    public void visit(RowConstructor rowConstructor) {

    }

    @Override
    public void visit(OracleHint hint) {

    }

    @Override
    public void visit(TimeKeyExpression timeKeyExpression) {

    }

    @Override
    public void visit(DateTimeLiteralExpression literal) {

    }

    @Override
    public void visit(NotExpression aThis) {

    }

    @Override
    public void visit(NextValExpression aThis) {

    }

    @Override
    public void visit(CollateExpression aThis) {

    }

    @Override
    public void visit(SimilarToExpression aThis) {

    }

    @Override
    public void visit(ArrayExpression aThis) {

    }

    @Override
    public void visit(VariableAssignment aThis) {

    }

    @Override
    public void visit(XMLSerializeExpr aThis) {

    }
}
