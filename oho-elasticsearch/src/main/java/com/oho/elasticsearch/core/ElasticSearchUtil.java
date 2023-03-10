package com.oho.elasticsearch.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import com.oho.elasticsearch.core.model.EsBoolQueryParam;
import com.oho.elasticsearch.core.model.EsQueryParam;
import com.oho.elasticsearch.core.model.EsRangeParam;
import com.oho.elasticsearch.core.model.EsResult;
import com.oho.elasticsearch.core.model.constant.EsConstant;
import com.oho.elasticsearch.core.model.enums.EsBoolEnum;
import com.oho.elasticsearch.core.model.enums.EsQueryBuilderEnum;
import com.oho.elasticsearch.core.model.enums.RangeEnum;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * ElasticSearch 工具类
 * ElasticSearch 查询默认返回10条数据，工具类默认Page返回20条数据。返回数据大小通过Page指定。
 *
 * @author Sparkler
 * @createDate 2023 /1/7
 */
@Slf4j
@Component
public class ElasticSearchUtil {

    // =================================== 初始化
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * Instantiates a new Elastic search util.
     *
     * @param elasticsearchRestTemplate the elasticsearch rest template
     */
    public ElasticSearchUtil(ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }


    // ----------------------------------------------------------------------- 公用方法

    /**
     * 拼接高亮条件
     */
    private static void appendHighlight(@NotNull EsQueryParam searchParam, NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        if (CollUtil.isNotEmpty(searchParam.getHighlightFields())) {
            List<HighlightBuilder.Field> highlightFields = new ArrayList<>(searchParam.getHighlightFields().size());
            for (String highlightField : searchParam.getHighlightFields()) {
                highlightFields.add(new HighlightBuilder.Field(highlightField).preTags(searchParam.getPreTags()).postTags(searchParam.getPostTags()));
            }
            nativeSearchQueryBuilder.withHighlightFields(highlightFields);
        }
    }

    /**
     * 拼接分页信息
     */
    private static void appendPage(@NotNull EsQueryParam searchParam, NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        if (ObjUtil.isNotEmpty(searchParam.getPage())) {
            nativeSearchQueryBuilder.withPageable(searchParam.getPage());
        }
    }

    /**
     * 拼接字段排序条件
     */
    private static void appendSort(@NotNull EsQueryParam searchParam, NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        if (CollUtil.isNotEmpty(searchParam.getSort())) {
            Set<String> sortFields = searchParam.getSort().keySet();
            for (String field : sortFields) {
                nativeSearchQueryBuilder.withSorts(SortBuilders.fieldSort(field).order(searchParam.getSort().get(field)));
            }
        }
    }

    /**
     * 拼接字段过滤条件
     */
    private static void appendSourceFilter(@NotNull EsQueryParam searchParam, NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        boolean hasIncludeField = ArrayUtil.isEmpty(searchParam.getIncludeFields());
        boolean hasExcludeField = ArrayUtil.isEmpty(searchParam.getExcludeFields());
        if (!hasIncludeField || !hasExcludeField) {
            String[] includeFields = !hasIncludeField ? searchParam.getIncludeFields() : new String[0];
            String[] excludeFields = !hasExcludeField ? searchParam.getExcludeFields() : new String[0];
            nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(includeFields, excludeFields));
        }
    }

    /**
     * 通用查询条件拼接
     */
    private static void commonQueryCondition(EsQueryParam searchParam, NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        // 拼接排序条件
        appendSort(searchParam, nativeSearchQueryBuilder);
        // 拼接分页条件
        appendPage(searchParam, nativeSearchQueryBuilder);
        // 拼接高亮条件
        appendHighlight(searchParam, nativeSearchQueryBuilder);
        // 拼接字段过滤条件
        appendSourceFilter(searchParam, nativeSearchQueryBuilder);
    }

    /**
     * 根据参数构建Range查询条件
     */
    private static void rangeQueryTarget(RangeQueryBuilder rangeQueryBuilder, Object value, @NotNull RangeEnum range) {
        switch (range) {
            case FROM:
                rangeQueryBuilder.from(value);
                break;
            case R_FROM:
                rangeQueryBuilder.from(value, Boolean.FALSE);
                break;
            case TO:
                rangeQueryBuilder.to(value);
                break;
            case L_TO:
                rangeQueryBuilder.to(value, Boolean.FALSE);
                break;
            case LT:
                rangeQueryBuilder.lt(value);
                break;
            case LTE:
                rangeQueryBuilder.lte(value);
                break;
            case GT:
                rangeQueryBuilder.gt(value);
                break;
            case GTE:
                rangeQueryBuilder.gte(value);
                break;
            default:
                break;
        }
    }

    /*
     * matchQuery与termQuery区别：
     * matchQuery：会将搜索词分词，再与目标查询字段进行匹配，若分词中的任意一个词与目标字段匹配上，则可查询到。
     * termQuery：不会对搜索词进行分词处理，而是作为一个整体与目标字段进行匹配，若完全匹配，则可查询到==
     */
    // ----------------------------------------------------------------- 构建QueryBuilder

    /**
     * 构建 TermQueryBuilder
     */
    @NotNull
    private static TermQueryBuilder termQueryBuilder(@NotNull EsQueryParam searchParam) {
        return QueryBuilders.termQuery(searchParam.getField().concat(EsConstant.KEYWORD_QUERY), searchParam.getContent());
    }

    /**
     * 构建 TermsQueryBuilder
     */
    @NotNull
    private static TermsQueryBuilder termsQueryBuilder(@NotNull EsQueryParam searchParam) {
        return QueryBuilders.termsQuery(searchParam.getField().concat(EsConstant.KEYWORD_QUERY), searchParam.getContents());
    }

    /**
     * 构建 MatchPhraseQueryBuilder
     */
    @NotNull
    private static MatchPhraseQueryBuilder matchPhraseQueryBuilder(@NotNull EsQueryParam searchParam) {
        return QueryBuilders.matchPhraseQuery(searchParam.getField(), searchParam.getContent());
    }

    /**
     * 构建 MatchQueryBuilder
     */
    @NotNull
    private static MatchQueryBuilder matchQueryBuilder(@NotNull EsQueryParam searchParam) {
        return QueryBuilders.matchQuery(searchParam.getField(), searchParam.getContent());
    }

    /**
     * 构建 QueryStringQueryBuilder
     */
    private static QueryStringQueryBuilder queryStringQueryBuilder(@NotNull EsQueryParam searchParam) {
        return QueryBuilders.queryStringQuery(String.valueOf(searchParam.getContent())).field(searchParam.getField());
    }

    /**
     * 构建 WildcardQueryBuilder
     */
    @NotNull
    private static WildcardQueryBuilder wildcardQuery(@NotNull EsQueryParam searchParam) {
        return QueryBuilders.wildcardQuery(searchParam.getField().concat(EsConstant.KEYWORD_QUERY), String.valueOf(searchParam.getContent()));
    }

    /**
     * 构建 RangeQueryBuilder
     */
    @NotNull
    private static RangeQueryBuilder rangeQueryBuilder(@NotNull EsQueryParam searchParam) {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(searchParam.getField());
        List<EsRangeParam> rangeParams = searchParam.getEsRangeParam();
        for (EsRangeParam rangeParam : rangeParams) {
            rangeQueryTarget(rangeQueryBuilder, rangeParam.getValue(), rangeParam.getRange());
        }
        return rangeQueryBuilder;
    }

    /**
     * 构建 MultiMatchQuery
     */
    @NotNull
    private static MultiMatchQueryBuilder multiMatchQueryBuilder(@NotNull EsQueryParam searchParam) {
        // 拼接查询字段
        String[] fields = searchParam.getFields().toArray(new String[0]);
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(searchParam.getContent(), fields);
        if (ObjUtil.isNotEmpty(searchParam.getOperator())) {
            multiMatchQueryBuilder.operator(searchParam.getOperator());
        }
        return multiMatchQueryBuilder;
    }

    /**
     * 构建 BoolQuery
     */
    @NotNull
    private static BoolQueryBuilder boolQueryBuilder(@NotNull EsQueryParam searchParam) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        List<EsBoolQueryParam> boolQueryParams = searchParam.getBoolQueryParam();
        for (EsBoolQueryParam boolQueryParam : boolQueryParams) {
            QueryBuilder queryBuilders = getQueryBuilders(boolQueryParam.getQueryBuilder(), EsQueryParam.convert(boolQueryParam));
            boolQueryBuilderAppendChild(boolQueryBuilder, queryBuilders, boolQueryParam.getBoolOption());
        }
        return boolQueryBuilder;
    }

    /**
     * 拼接BoolQuery子语句
     */
    private static void boolQueryBuilderAppendChild(BoolQueryBuilder boolQueryBuilder, QueryBuilder queryBuilders, @NotNull EsBoolEnum boolOption) {
        switch (boolOption) {
            case MUST:
                boolQueryBuilder.must(queryBuilders);
                break;
            case MUST_NOT:
                boolQueryBuilder.mustNot(queryBuilders);
                break;
            case SHOULD:
                boolQueryBuilder.should(queryBuilders);
                break;
            default:
        }
    }

    /**
     * 创建查询构建器
     */
    @Nullable
    private static QueryBuilder getQueryBuilders(@NotNull EsQueryBuilderEnum queryBuilder, @NotNull EsQueryParam searchParam) {
        Operator operator = searchParam.getOperator();
        switch (queryBuilder) {
            case TERM_QUERY_BUILDER:
                return termQueryBuilder(searchParam);
            case TERMS_QUERY_BUILDER:
                return termsQueryBuilder(searchParam);
            case MATCH_QUERY_BUILDER:
                return Objects.isNull(operator) ? matchQueryBuilder(searchParam) : matchQueryBuilder(searchParam).operator(operator);
            case MATCH_PHRASE_QUERY_BUILDER:
                return matchPhraseQueryBuilder(searchParam);
            case RANGE_QUERY_BUILDER:
                return rangeQueryBuilder(searchParam);
            case MULTI_MATCH_QUERY_BUILDER:
                return Objects.isNull(operator) ? multiMatchQueryBuilder(searchParam) : multiMatchQueryBuilder(searchParam).operator(operator);
            default:
                return null;
        }
    }

    /**
     * 执行查询返回结果
     */
    private <T> EsResult<T> searchResult(Class<T> clazz, @NotNull NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        SearchHits<T> search = elasticsearchRestTemplate.search(nativeSearchQuery, clazz);
        if (ObjUtil.isNotEmpty(nativeSearchQuery.getPageable())) {
            SearchPage<T> page = SearchHitSupport.searchPageFor(search, nativeSearchQuery.getPageable());
            return new EsResult<T>().convert(page);
        }
        return new EsResult<>(search);
    }

    // ------------------------------------------------------------------------------ 索引操作方法

    /**
     * 创建索引
     *
     * @param index the index
     * @return boolean 是否成功
     */
    public boolean createIndex(Class<?> index) {
        if (indexExist(index)) {
            return Boolean.FALSE;
        }
        return elasticsearchRestTemplate.indexOps(index).create();
    }

    /**
     * 创建索引
     *
     * @param index the index
     * @return boolean 是否成功
     */
    public boolean createIndex(String index) {
        IndexCoordinates indexCoordinates = getIndexCoordinates(index);
        if (indexExist(indexCoordinates)) {
            return Boolean.FALSE;
        }
        return elasticsearchRestTemplate.indexOps(indexCoordinates).create();
    }

    /**
     * 删除索引
     *
     * @param index the index
     * @return boolean 是否成功
     */
    public boolean deleteIndex(Class<?> index) {
        return elasticsearchRestTemplate.indexOps(index).delete();
    }

    /**
     * 删除索引
     *
     * @param index the index
     * @return boolean 是否成功
     */
    public boolean deleteIndex(String index) {
        return elasticsearchRestTemplate.indexOps(getIndexCoordinates(index)).delete();
    }

    /**
     * 判断索引是否存在
     *
     * @param index the index
     * @return boolean 是否存在
     */
    public Boolean indexExist(Class<?> index) {
        return elasticsearchRestTemplate.indexOps(index).exists();
    }

    /**
     * 判断索引是否存在
     *
     * @param index the index
     * @return boolean 是否存在
     */
    public Boolean indexExist(IndexCoordinates index) {
        return elasticsearchRestTemplate.indexOps(index).exists();
    }

    /**
     * 获取索引封装对象
     *
     * @param clazz the clazz
     * @return the index coordinates
     */
    public IndexCoordinates getIndexCoordinates(Class<?> clazz) {
        return elasticsearchRestTemplate.getIndexCoordinatesFor(clazz);
    }

    /**
     * 获取索引封装对象
     *
     * @param indexName the index name
     * @return the index coordinates
     */
    public IndexCoordinates getIndexCoordinates(String indexName) {
        return IndexCoordinates.of(indexName);
    }

    // ------------------------------------------------------------------------------ 数据插入方法

    /**
     * 插入单条数据
     *
     * @param <T>  the type parameter
     * @param data the data
     * @return the string
     */
    public <T> T insert(T data) {
        return elasticsearchRestTemplate.save(data, getIndexCoordinates(data.getClass()));
    }

    /**
     * 插入多条数据
     *
     * @param <T>      the type parameter
     * @param dataList the data
     * @return the integer
     */
    public <T> Integer insert(List<T> dataList) {
        elasticsearchRestTemplate.save(dataList);
        return dataList.size();
    }

    // ------------------------------------------------------------------------------ 数据查询方法

    /**
     * 查询全部
     *
     * @param <T>         the type parameter
     * @param searchParam the search param
     * @param clazz       the clazz
     * @return search hits
     */
    public <T> EsResult<T> matchAll(@NotNull EsQueryParam searchParam, Class<T> clazz) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchAllQuery());
        commonQueryCondition(searchParam, nativeSearchQueryBuilder);
        return searchResult(clazz, nativeSearchQueryBuilder);
    }

    /**
     * 不会进行分词，完全匹配 [ EQ ]
     *
     * @param <T>         the type parameter
     * @param searchParam the search param
     * @param clazz       the clazz
     * @return search hits
     */
    public <T> EsResult<T> termQuery(@NotNull EsQueryParam searchParam, Class<T> clazz) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(termQueryBuilder(searchParam));
        commonQueryCondition(searchParam, nativeSearchQueryBuilder);
        return searchResult(clazz, nativeSearchQueryBuilder);
    }

    /**
     * 一个字段匹配多个值，不会进行分词，完全匹配 [ EQ ]
     *
     * @param <T>         the type parameter
     * @param searchParam the search param
     * @param clazz       the clazz
     * @return search hits
     */
    public <T> EsResult<T> termsQuery(@NotNull EsQueryParam searchParam, Class<T> clazz) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(termsQueryBuilder(searchParam));
        commonQueryCondition(searchParam, nativeSearchQueryBuilder);
        return searchResult(clazz, nativeSearchQueryBuilder);
    }

    /**
     * QueryBuilders.matchPhraseQuery() 不会分词，当成一个整体去匹配，相当于 %like%
     *
     * @param <T>         the type parameter
     * @param searchParam the search param
     * @param clazz       the clazz
     * @return the search hits
     */
    public <T> EsResult<T> matchPhraseQuery(@NotNull EsQueryParam searchParam, Class<T> clazz) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(matchPhraseQueryBuilder(searchParam));
        commonQueryCondition(searchParam, nativeSearchQueryBuilder);
        return searchResult(clazz, nativeSearchQueryBuilder);
    }

    /**
     * 会根据分词器进行分词，分词之后去查询
     * 单个匹配，Field不支持通配符，前缀具有高级特性
     *
     * @param <T>         the type parameter
     * @param searchParam the search param
     * @param clazz       the clazz
     * @return the search hits
     */
    public <T> EsResult<T> matchQuery(@NotNull EsQueryParam searchParam, Class<T> clazz) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(matchQueryBuilder(searchParam));
        commonQueryCondition(searchParam, nativeSearchQueryBuilder);
        return searchResult(clazz, nativeSearchQueryBuilder);
    }

    /**
     * 一个值匹配多个字段，且Field有通配符
     *
     * @param <T>         the type parameter
     * @param searchParam the search param
     * @param clazz       the clazz
     * @return the es result
     */
    public <T> EsResult<T> multiMatchQuery(@NotNull EsQueryParam searchParam, Class<T> clazz) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(multiMatchQueryBuilder(searchParam));
        commonQueryCondition(searchParam, nativeSearchQueryBuilder);
        return searchResult(clazz, nativeSearchQueryBuilder);
    }

    /**
     * 通过Id查询
     *
     * @param <T>   the type parameter
     * @param id    the id
     * @param clazz the clazz
     * @return the t
     */
    public <T> T idQuery(Object id, Class<T> clazz) {
        return elasticsearchRestTemplate.get(String.valueOf(id), clazz);
    }

    /**
     * 通过Id查询
     *
     * @param <T>   the type parameter
     * @param ids   the ids
     * @param clazz the clazz
     * @return the list
     */
    public <T> List<T> idsQuery(List<?> ids, Class<T> clazz) {
        Assert.notNull(ids, "The search Id cannot be empty");

        List<T> idsSearch = new ArrayList<>(ids.size());
        for (Object id : ids) {
            T data = elasticsearchRestTemplate.get(String.valueOf(id), clazz);
            if (ObjUtil.isNotEmpty(data)) {
                idsSearch.add(data);
            }
        }
        return idsSearch;
    }

    /**
     * 不使用通配符的模糊查询，左右匹配
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return the list
     */
    public <T> EsResult<T> queryStringQuery(@NotNull EsQueryParam searchParam, Class<T> clazz) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        QueryStringQueryBuilder queryStringQueryBuilder = queryStringQueryBuilder(searchParam);
        nativeSearchQueryBuilder.withQuery(queryStringQueryBuilder);
        commonQueryCondition(searchParam, nativeSearchQueryBuilder);
        return searchResult(clazz, nativeSearchQueryBuilder);
    }

    /**
     * 模糊查询，支持通配符
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return the list
     */
    public <T> EsResult<T> wildcardQuery(@NotNull EsQueryParam searchParam, Class<T> clazz) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(wildcardQuery(searchParam));
        commonQueryCondition(searchParam, nativeSearchQueryBuilder);
        return searchResult(clazz, nativeSearchQueryBuilder);
    }

    /**
     * 范围查询.
     *
     * @param <T>         the type parameter
     * @param searchParam the search param
     * @param clazz       the clazz
     * @return the es result
     */
    public <T> EsResult<T> rangeQuery(EsQueryParam searchParam, Class<T> clazz) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(rangeQueryBuilder(searchParam));
        commonQueryCondition(searchParam, nativeSearchQueryBuilder);
        return searchResult(clazz, nativeSearchQueryBuilder);
    }

    /**
     * 复合查询 must(and)、must_not(not)、should(or)
     *
     * @param searchParam the index name
     * @param clazz       the clazz
     */
    public <T> EsResult<T> boolQuery(EsQueryParam searchParam, Class<T> clazz) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder(searchParam));
        commonQueryCondition(searchParam, nativeSearchQueryBuilder);
        return searchResult(clazz, nativeSearchQueryBuilder);
    }

}
