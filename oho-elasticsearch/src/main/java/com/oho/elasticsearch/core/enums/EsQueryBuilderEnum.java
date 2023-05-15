package com.oho.elasticsearch.core.enums;

/**
 * @author Sparkler
 * @createDate 2023/1/9
 */
public enum EsQueryBuilderEnum {
    /**
     * 构建 TermQueryBuilder
     */
    TERM_QUERY_BUILDER,

    /**
     * 构建 TermsQueryBuilder
     */
    TERMS_QUERY_BUILDER,

    /**
     * 构建 MatchPhraseQueryBuilder
     */
    MATCH_PHRASE_QUERY_BUILDER,

    /**
     * 构建 MatchQueryBuilder
     */
    MATCH_QUERY_BUILDER,

    /**
     * 构建 RangeQueryBuilder
     */
    RANGE_QUERY_BUILDER,

    /**
     * 构建 MultiMatchQuery
     */
    MULTI_MATCH_QUERY_BUILDER,

    /**
     * 构建 WildcardQuery
     */
    WILDCARD_QUERY
}
