package com.atguigu.eduservice.builder;

import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.Query;
import org.elasticsearch.Version;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.ParsingException;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.lucene.search.Queries;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.LoggingDeprecationHandler;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.support.QueryParsers;
import org.elasticsearch.index.search.MatchQuery;

import java.io.IOException;
import java.util.Objects;

public class MatchQueryBuilderUnderdog extends AbstractQueryBuilder<MatchQueryBuilderUnderdog> {
    public static final ParseField ZERO_TERMS_QUERY_FIELD = new ParseField("zero_terms_query", new String[0]);
    public static final ParseField CUTOFF_FREQUENCY_FIELD = new ParseField("cutoff_frequency", new String[0]);
    public static final ParseField LENIENT_FIELD = new ParseField("lenient", new String[0]);
    public static final ParseField FUZZY_TRANSPOSITIONS_FIELD = new ParseField("fuzzy_transpositions", new String[0]);
    public static final ParseField FUZZY_REWRITE_FIELD = new ParseField("fuzzy_rewrite", new String[0]);
    public static final ParseField MINIMUM_SHOULD_MATCH_FIELD = new ParseField("minimum_should_match", new String[0]);
    public static final ParseField OPERATOR_FIELD = new ParseField("operator", new String[0]);
    public static final ParseField MAX_EXPANSIONS_FIELD = new ParseField("max_expansions", new String[0]);
    public static final ParseField PREFIX_LENGTH_FIELD = new ParseField("prefix_length", new String[0]);
    public static final ParseField ANALYZER_FIELD = new ParseField("analyzer", new String[0]);
    public static final ParseField QUERY_FIELD = new ParseField("query", new String[0]);
//    public static final ParseField GENERATE_SYNONYMS_PHRASE_QUERY = new ParseField("auto_generate_synonyms_phrase_query", new String[0]);
    public static final String NAME = "match";
    public static final Operator DEFAULT_OPERATOR;
    private final String fieldName;
    private final Object value;
    private Operator operator;
    private String analyzer;
    private Fuzziness fuzziness;
    private int prefixLength;
    private int maxExpansions;
    private boolean fuzzyTranspositions;
    private String minimumShouldMatch;
    private String fuzzyRewrite;
    private boolean lenient;
    private MatchQuery.ZeroTermsQuery zeroTermsQuery;
    private Float cutoffFrequency;
    private boolean autoGenerateSynonymsPhraseQuery;

    public MatchQueryBuilderUnderdog(String fieldName, Object value) {
        this.operator = DEFAULT_OPERATOR;
        this.fuzziness = null;
        this.prefixLength = 0;
        this.maxExpansions = 50;
        this.fuzzyTranspositions = true;
        this.fuzzyRewrite = null;
        this.lenient = false;
        this.zeroTermsQuery = MatchQuery.DEFAULT_ZERO_TERMS_QUERY;
        this.cutoffFrequency = null;
        this.autoGenerateSynonymsPhraseQuery = true;
        if (fieldName == null) {
            throw new IllegalArgumentException("[match] requires fieldName");
        } else if (value == null) {
            throw new IllegalArgumentException("[match] requires query value");
        } else {
            this.fieldName = fieldName;
            this.value = value;
        }
    }

    public MatchQueryBuilderUnderdog(StreamInput in) throws IOException {
        super(in);
        this.operator = DEFAULT_OPERATOR;
        this.fuzziness = null;
        this.prefixLength = 0;
        this.maxExpansions = 50;
        this.fuzzyTranspositions = true;
        this.fuzzyRewrite = null;
        this.lenient = false;
        this.zeroTermsQuery = MatchQuery.DEFAULT_ZERO_TERMS_QUERY;
        this.cutoffFrequency = null;
        this.autoGenerateSynonymsPhraseQuery = true;
        this.fieldName = in.readString();
        this.value = in.readGenericValue();
        if (in.getVersion().before(Version.V_6_0_0_rc1)) {
            MatchQuery.Type.readFromStream(in);
        }

        this.operator = Operator.readFromStream(in);
        if (in.getVersion().before(Version.V_6_0_0_rc1)) {
            in.readVInt();
        }

        this.prefixLength = in.readVInt();
        this.maxExpansions = in.readVInt();
        this.fuzzyTranspositions = in.readBoolean();
        this.lenient = in.readBoolean();
        this.zeroTermsQuery = MatchQuery.ZeroTermsQuery.readFromStream(in);
        this.analyzer = in.readOptionalString();
        this.minimumShouldMatch = in.readOptionalString();
        this.fuzzyRewrite = in.readOptionalString();
        this.fuzziness = (Fuzziness)in.readOptionalWriteable(Fuzziness::new);
        this.cutoffFrequency = in.readOptionalFloat();
        if (in.getVersion().onOrAfter(Version.V_6_1_0)) {
            this.autoGenerateSynonymsPhraseQuery = in.readBoolean();
        }

    }

    protected void doWriteTo(StreamOutput out) throws IOException {
        out.writeString(this.fieldName);
        out.writeGenericValue(this.value);
        if (out.getVersion().before(Version.V_6_0_0_rc1)) {
            MatchQuery.Type.BOOLEAN.writeTo(out);
        }

        this.operator.writeTo(out);
        if (out.getVersion().before(Version.V_6_0_0_rc1)) {
            out.writeVInt(0);
        }

        out.writeVInt(this.prefixLength);
        out.writeVInt(this.maxExpansions);
        out.writeBoolean(this.fuzzyTranspositions);
        out.writeBoolean(this.lenient);
        this.zeroTermsQuery.writeTo(out);
        out.writeOptionalString(this.analyzer);
        out.writeOptionalString(this.minimumShouldMatch);
        out.writeOptionalString(this.fuzzyRewrite);
        out.writeOptionalWriteable(this.fuzziness);
        out.writeOptionalFloat(this.cutoffFrequency);
        if (out.getVersion().onOrAfter(Version.V_6_1_0)) {
            out.writeBoolean(this.autoGenerateSynonymsPhraseQuery);
        }

    }

    public String fieldName() {
        return this.fieldName;
    }

    public Object value() {
        return this.value;
    }

    public MatchQueryBuilderUnderdog operator(Operator operator) {
        if (operator == null) {
            throw new IllegalArgumentException("[match] requires operator to be non-null");
        } else {
            this.operator = operator;
            return this;
        }
    }

    public Operator operator() {
        return this.operator;
    }

    public MatchQueryBuilderUnderdog analyzer(String analyzer) {
        this.analyzer = analyzer;
        return this;
    }

    public String analyzer() {
        return this.analyzer;
    }

    public MatchQueryBuilderUnderdog fuzziness(Object fuzziness) {
        this.fuzziness = Fuzziness.build(fuzziness);
        return this;
    }

    public Fuzziness fuzziness() {
        return this.fuzziness;
    }

    public MatchQueryBuilderUnderdog prefixLength(int prefixLength) {
        if (prefixLength < 0) {
            throw new IllegalArgumentException("[match] requires prefix length to be non-negative.");
        } else {
            this.prefixLength = prefixLength;
            return this;
        }
    }

    public int prefixLength() {
        return this.prefixLength;
    }

    public MatchQueryBuilderUnderdog maxExpansions(int maxExpansions) {
        if (maxExpansions <= 0) {
            throw new IllegalArgumentException("[match] requires maxExpansions to be positive.");
        } else {
            this.maxExpansions = maxExpansions;
            return this;
        }
    }

    public int maxExpansions() {
        return this.maxExpansions;
    }

    public MatchQueryBuilderUnderdog cutoffFrequency(float cutoff) {
        this.cutoffFrequency = cutoff;
        return this;
    }

    public Float cutoffFrequency() {
        return this.cutoffFrequency;
    }

    public MatchQueryBuilderUnderdog minimumShouldMatch(String minimumShouldMatch) {
        this.minimumShouldMatch = minimumShouldMatch;
        return this;
    }

    public String minimumShouldMatch() {
        return this.minimumShouldMatch;
    }

    public MatchQueryBuilderUnderdog fuzzyRewrite(String fuzzyRewrite) {
        this.fuzzyRewrite = fuzzyRewrite;
        return this;
    }

    public String fuzzyRewrite() {
        return this.fuzzyRewrite;
    }

    public MatchQueryBuilderUnderdog fuzzyTranspositions(boolean fuzzyTranspositions) {
        this.fuzzyTranspositions = fuzzyTranspositions;
        return this;
    }

    public boolean fuzzyTranspositions() {
        return this.fuzzyTranspositions;
    }

    /** @deprecated */
    @Deprecated
    public MatchQueryBuilderUnderdog setLenient(boolean lenient) {
        return this.lenient(lenient);
    }

    public MatchQueryBuilderUnderdog lenient(boolean lenient) {
        this.lenient = lenient;
        return this;
    }

    public boolean lenient() {
        return this.lenient;
    }

    public MatchQueryBuilderUnderdog zeroTermsQuery(MatchQuery.ZeroTermsQuery zeroTermsQuery) {
        if (zeroTermsQuery == null) {
            throw new IllegalArgumentException("[match] requires zeroTermsQuery to be non-null");
        } else {
            this.zeroTermsQuery = zeroTermsQuery;
            return this;
        }
    }

    public MatchQuery.ZeroTermsQuery zeroTermsQuery() {
        return this.zeroTermsQuery;
    }

    public MatchQueryBuilderUnderdog autoGenerateSynonymsPhraseQuery(boolean enable) {
        this.autoGenerateSynonymsPhraseQuery = enable;
        return this;
    }

    public boolean autoGenerateSynonymsPhraseQuery() {
        return this.autoGenerateSynonymsPhraseQuery;
    }

    public void doXContent(XContentBuilder builder, ToXContent.Params params) throws IOException {
        builder.startObject("match");
        builder.startObject(this.fieldName);
        builder.field(QUERY_FIELD.getPreferredName(), this.value);
        builder.field(OPERATOR_FIELD.getPreferredName(), this.operator.toString());
        if (this.analyzer != null) {
            builder.field(ANALYZER_FIELD.getPreferredName(), this.analyzer);
        }

        if (this.fuzziness != null) {
            this.fuzziness.toXContent(builder, params);
        }

        builder.field(PREFIX_LENGTH_FIELD.getPreferredName(), this.prefixLength);
        builder.field(MAX_EXPANSIONS_FIELD.getPreferredName(), this.maxExpansions);
        if (this.minimumShouldMatch != null) {
            builder.field(MINIMUM_SHOULD_MATCH_FIELD.getPreferredName(), this.minimumShouldMatch);
        }

        if (this.fuzzyRewrite != null) {
            builder.field(FUZZY_REWRITE_FIELD.getPreferredName(), this.fuzzyRewrite);
        }

        builder.field(FUZZY_TRANSPOSITIONS_FIELD.getPreferredName(), this.fuzzyTranspositions);
        builder.field(LENIENT_FIELD.getPreferredName(), this.lenient);
        builder.field(ZERO_TERMS_QUERY_FIELD.getPreferredName(), this.zeroTermsQuery.toString());
        if (this.cutoffFrequency != null) {
            builder.field(CUTOFF_FREQUENCY_FIELD.getPreferredName(), this.cutoffFrequency);
        }

//        builder.field(GENERATE_SYNONYMS_PHRASE_QUERY.getPreferredName(), this.autoGenerateSynonymsPhraseQuery);
        this.printBoostAndQueryName(builder);
        builder.endObject();
        builder.endObject();
    }

    protected Query doToQuery(QueryShardContext context) throws IOException {
        if (this.analyzer != null && context.getIndexAnalyzers().get(this.analyzer) == null) {
            throw new QueryShardException(context, "[match] analyzer [" + this.analyzer + "] not found", new Object[0]);
        } else {
            MatchQuery matchQuery = new MatchQuery(context);
            matchQuery.setOccur(this.operator.toBooleanClauseOccur());
            if (this.analyzer != null) {
                matchQuery.setAnalyzer(this.analyzer);
            }

            matchQuery.setFuzziness(this.fuzziness);
            matchQuery.setFuzzyPrefixLength(this.prefixLength);
            matchQuery.setMaxExpansions(this.maxExpansions);
            matchQuery.setTranspositions(this.fuzzyTranspositions);
            matchQuery.setFuzzyRewriteMethod(QueryParsers.parseRewriteMethod(this.fuzzyRewrite, (MultiTermQuery.RewriteMethod)null, LoggingDeprecationHandler.INSTANCE));
            matchQuery.setLenient(this.lenient);
            matchQuery.setCommonTermsCutoff(this.cutoffFrequency);
            matchQuery.setZeroTermsQuery(this.zeroTermsQuery);
            matchQuery.setAutoGenerateSynonymsPhraseQuery(this.autoGenerateSynonymsPhraseQuery);
            Query query = matchQuery.parse(MatchQuery.Type.BOOLEAN, this.fieldName, this.value);
            return Queries.maybeApplyMinimumShouldMatch(query, this.minimumShouldMatch);
        }
    }

    protected boolean doEquals(MatchQueryBuilderUnderdog other) {
        return Objects.equals(this.fieldName, other.fieldName) && Objects.equals(this.value, other.value) && Objects.equals(this.operator, other.operator) && Objects.equals(this.analyzer, other.analyzer) && Objects.equals(this.fuzziness, other.fuzziness) && Objects.equals(this.prefixLength, other.prefixLength) && Objects.equals(this.maxExpansions, other.maxExpansions) && Objects.equals(this.minimumShouldMatch, other.minimumShouldMatch) && Objects.equals(this.fuzzyRewrite, other.fuzzyRewrite) && Objects.equals(this.lenient, other.lenient) && Objects.equals(this.fuzzyTranspositions, other.fuzzyTranspositions) && Objects.equals(this.zeroTermsQuery, other.zeroTermsQuery) && Objects.equals(this.cutoffFrequency, other.cutoffFrequency) && Objects.equals(this.autoGenerateSynonymsPhraseQuery, other.autoGenerateSynonymsPhraseQuery);
    }

    protected int doHashCode() {
        return Objects.hash(new Object[]{this.fieldName, this.value, this.operator, this.analyzer, this.fuzziness, this.prefixLength, this.maxExpansions, this.minimumShouldMatch, this.fuzzyRewrite, this.lenient, this.fuzzyTranspositions, this.zeroTermsQuery, this.cutoffFrequency, this.autoGenerateSynonymsPhraseQuery});
    }

    public String getWriteableName() {
        return "match";
    }

    public static MatchQueryBuilderUnderdog fromXContent(XContentParser parser) throws IOException {
        String fieldName = null;
        Object value = null;
        float boost = 1.0F;
        String minimumShouldMatch = null;
        String analyzer = null;
        Operator operator = DEFAULT_OPERATOR;
        Fuzziness fuzziness = null;
        int prefixLength = 0;
        int maxExpansion = 50;
        boolean fuzzyTranspositions = true;
        String fuzzyRewrite = null;
        boolean lenient = false;
        Float cutOffFrequency = null;
        MatchQuery.ZeroTermsQuery zeroTermsQuery = MatchQuery.DEFAULT_ZERO_TERMS_QUERY;
        boolean autoGenerateSynonymsPhraseQuery = true;
        String queryName = null;
        String currentFieldName = null;

        while(true) {
            XContentParser.Token token;
            while((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    currentFieldName = parser.currentName();
                } else if (token == XContentParser.Token.START_OBJECT) {
                    throwParsingExceptionOnMultipleFields("match", parser.getTokenLocation(), fieldName, currentFieldName);
                    fieldName = currentFieldName;

                    while((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                        if (token == XContentParser.Token.FIELD_NAME) {
                            currentFieldName = parser.currentName();
                        } else {
                            if (!token.isValue()) {
                                throw new ParsingException(parser.getTokenLocation(), "[match] unknown token [" + token + "] after [" + currentFieldName + "]", new Object[0]);
                            }

                            if (QUERY_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                value = parser.objectText();
                            } else if (ANALYZER_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                analyzer = parser.text();
                            } else if (AbstractQueryBuilder.BOOST_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                boost = parser.floatValue();
                            } else if (Fuzziness.FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                fuzziness = Fuzziness.parse(parser);
                            } else if (PREFIX_LENGTH_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                prefixLength = parser.intValue();
                            } else if (MAX_EXPANSIONS_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                maxExpansion = parser.intValue();
                            } else if (OPERATOR_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                operator = Operator.fromString(parser.text());
                            } else if (MINIMUM_SHOULD_MATCH_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                minimumShouldMatch = parser.textOrNull();
                            } else if (FUZZY_REWRITE_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                fuzzyRewrite = parser.textOrNull();
                            } else if (FUZZY_TRANSPOSITIONS_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                fuzzyTranspositions = parser.booleanValue();
                            } else if (LENIENT_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                lenient = parser.booleanValue();
                            } else if (CUTOFF_FREQUENCY_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                cutOffFrequency = parser.floatValue();
                            } else if (ZERO_TERMS_QUERY_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                String zeroTermsDocs = parser.text();
                                if ("none".equalsIgnoreCase(zeroTermsDocs)) {
                                    zeroTermsQuery = MatchQuery.ZeroTermsQuery.NONE;
                                } else {
                                    if (!"all".equalsIgnoreCase(zeroTermsDocs)) {
                                        throw new ParsingException(parser.getTokenLocation(), "Unsupported zero_terms_docs value [" + zeroTermsDocs + "]", new Object[0]);
                                    }

                                    zeroTermsQuery = MatchQuery.ZeroTermsQuery.ALL;
                                }
                            } else if (AbstractQueryBuilder.NAME_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                queryName = parser.text();
                            } else {
//                                if (!GENERATE_SYNONYMS_PHRASE_QUERY.match(currentFieldName, parser.getDeprecationHandler())) {
//                                    throw new ParsingException(parser.getTokenLocation(), "[match] query does not support [" + currentFieldName + "]", new Object[0]);
//                                }

                                autoGenerateSynonymsPhraseQuery = parser.booleanValue();
                            }
                        }
                    }
                } else {
                    throwParsingExceptionOnMultipleFields("match", parser.getTokenLocation(), fieldName, parser.currentName());
                    fieldName = parser.currentName();
                    value = parser.objectText();
                }
            }

            if (value == null) {
                throw new ParsingException(parser.getTokenLocation(), "No text specified for text query", new Object[0]);
            }

            MatchQueryBuilderUnderdog matchQuery = new MatchQueryBuilderUnderdog(fieldName, value);
            matchQuery.operator(operator);
            matchQuery.analyzer(analyzer);
            matchQuery.minimumShouldMatch(minimumShouldMatch);
            if (fuzziness != null) {
                matchQuery.fuzziness(fuzziness);
            }

            matchQuery.fuzzyRewrite(fuzzyRewrite);
            matchQuery.prefixLength(prefixLength);
            matchQuery.fuzzyTranspositions(fuzzyTranspositions);
            matchQuery.maxExpansions(maxExpansion);
            matchQuery.lenient(lenient);
            if (cutOffFrequency != null) {
                matchQuery.cutoffFrequency(cutOffFrequency);
            }

            matchQuery.zeroTermsQuery(zeroTermsQuery);
            matchQuery.autoGenerateSynonymsPhraseQuery(autoGenerateSynonymsPhraseQuery);
            matchQuery.queryName(queryName);
            matchQuery.boost(boost);
            return matchQuery;
        }
    }

    static {
        DEFAULT_OPERATOR = Operator.OR;
    }
}
