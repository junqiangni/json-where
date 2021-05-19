package com.jsonwhere;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;


public class JSONWhere {
    public static boolean isJsonWhereData(String jsonStr,String whereStr) throws Exception {
        //将jsonStr解析成JSONObject
        JSONObject jsonObject = null;
        try {
            jsonObject =  JSON.parseObject(jsonStr);
        } catch (JSONException e) {
           throw new JSONException("json parse is error:"+e.getMessage());
        }
        //将where条件后面的解析成对应的表达式
        Expression expression = null;
        try {
           expression = getWhereExpression(whereStr);
        } catch (JSQLParserException e) {
            throw new JSQLParserException("where parse is error:"+e.getMessage());
        }
        if(jsonObject!=null&&expression!=null){
            JSONDataExpressionVisitor expressionValidator = new JSONDataExpressionVisitor(jsonObject);
            expression.accept(expressionValidator);
            return expressionValidator.getResult();
        }
        return false;
    }

    /**
     * 这里引入 net.sf.jsqlparse 针对sql中的where条件进行解析。获取where的查询条件
     * @return
     */
    public static Expression getWhereExpression(String whereStr) throws JSQLParserException {
        return CCJSqlParserUtil.parseCondExpression(whereStr);
    }
}
