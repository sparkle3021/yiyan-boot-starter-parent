package com.yiyan.boot.common.utils;

import com.yiyan.boot.common.utils.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

/**
 * spring el表达式解析
 *
 * @author MENGJIAO
 * @createDate 2023-06-21 15:00
 */
@Slf4j
public class SpElUtils {
    private static final ExpressionParser parser = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public static String parseSpEl(Method method, Object[] args, String spEl) {
        String[] params = Optional.ofNullable(parameterNameDiscoverer.getParameterNames(method)).orElse(new String[]{});//解析参数名
        EvaluationContext context = new StandardEvaluationContext();//el解析需要的上下文对象
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);//所有参数都作为原材料扔进去
        }
        Expression expression = parser.parseExpression(spEl);
        String expressionValue;
        try {
            expressionValue = expression.getValue(context, String.class);
        } catch (Exception e) {
            expressionValue = JsonUtils.toJson(expression.getValue(context, Object.class));
        }
        log.info("[ spEl 解析内容 ]: {}", expressionValue);
        return expressionValue;
    }

    public static Map<String, String> parseSpEls(Method method, Object[] args, String spEl) {
        //解析参数名
        String[] params = Optional.ofNullable(parameterNameDiscoverer.getParameterNames(method)).orElse(new String[]{});
        //el解析需要的上下文对象
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        String[] spElItem = spEl.split(",");
        Map<String, String> spELMap = new java.util.HashMap<>(spElItem.length);
        for (String item : spElItem) {
            Expression expression = parser.parseExpression(item);
            String expressionValue;
            try {
                expressionValue = expression.getValue(context, String.class);
            } catch (Exception e) {
                expressionValue = JsonUtils.toJson(expression.getValue(context, Object.class));
            }
            spELMap.put(item, expressionValue);
            log.info("[ item - spEl 解析内容 ]:{} - {}", item, expressionValue);
        }
        return spELMap;
    }

    public static String getMethodKey(Method method) {
        return method.getDeclaringClass() + "#" + method.getName();
    }
}