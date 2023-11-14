package com.yiyan.boot.elasticsearch.core.constant;

/**
 * @author Sparkler
 * @createDate 2023/1/8
 */
public class EsConstant {
    public static final String KEYWORD_QUERY = ".keyword";

    /**
     * 通配符 * , 可匹配多个字符
     */
    public static final String WILDCARD_ANY = "*";

    /**
     * 通配符 ？ , 可匹配任意字符
     */
    public static final String WILDCARD_ONE = "?";

    /**
     * 默认高亮Tag开始标签
     */
    public static final String DEFAULT_PRE_TAG = "<span style='color:#f50'>";

    /**
     * 默认高亮Tag结束标签
     */
    public static final String DEFAULT_POST_TAG = "</span>";
}
