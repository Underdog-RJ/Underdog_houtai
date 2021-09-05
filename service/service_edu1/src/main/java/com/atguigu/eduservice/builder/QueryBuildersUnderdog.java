package com.atguigu.eduservice.builder;

import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;

public final class QueryBuildersUnderdog {
    public static MultiMatchQueryBuilderUnderdog multiMatchQuery(Object text, String... fieldNames) {
        return new MultiMatchQueryBuilderUnderdog(text, fieldNames);
    }
}
