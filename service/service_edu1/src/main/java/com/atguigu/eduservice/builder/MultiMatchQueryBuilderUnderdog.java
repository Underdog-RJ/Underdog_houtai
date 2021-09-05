package com.atguigu.eduservice.builder;

import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.Query;
import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.Version;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.ParsingException;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.common.regex.Regex;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.DeprecationHandler;
import org.elasticsearch.common.xcontent.LoggingDeprecationHandler;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.support.QueryParsers;
import org.elasticsearch.index.search.MatchQuery;
import org.elasticsearch.index.search.MultiMatchQuery;
import org.elasticsearch.index.search.QueryParserHelper;

import java.io.IOException;
import java.util.*;

public class MultiMatchQueryBuilderUnderdog  extends AbstractQueryBuilder<MultiMatchQueryBuilderUnderdog> {
    public static final String NAME = "multi_match";
    public static final MultiMatchQueryBuilderUnderdog.Type DEFAULT_TYPE;
    public static final Operator DEFAULT_OPERATOR;
    public static final int DEFAULT_PHRASE_SLOP = 0;
    public static final int DEFAULT_PREFIX_LENGTH = 0;
    public static final int DEFAULT_MAX_EXPANSIONS = 50;
    public static final MatchQuery.ZeroTermsQuery DEFAULT_ZERO_TERMS_QUERY;
    public static final boolean DEFAULT_FUZZY_TRANSPOSITIONS = true;
    private static final ParseField SLOP_FIELD;
    private static final ParseField ZERO_TERMS_QUERY_FIELD;
    private static final ParseField LENIENT_FIELD;
    private static final ParseField CUTOFF_FREQUENCY_FIELD;
    private static final ParseField TIE_BREAKER_FIELD;
    private static final ParseField USE_DIS_MAX_FIELD;
    private static final ParseField FUZZY_REWRITE_FIELD;
    private static final ParseField MINIMUM_SHOULD_MATCH_FIELD;
    private static final ParseField OPERATOR_FIELD;
    private static final ParseField MAX_EXPANSIONS_FIELD;
    private static final ParseField PREFIX_LENGTH_FIELD;
    private static final ParseField ANALYZER_FIELD;
    private static final ParseField TYPE_FIELD;
    private static final ParseField QUERY_FIELD;
    private static final ParseField FIELDS_FIELD;
//    private static final ParseField GENERATE_SYNONYMS_PHRASE_QUERY;
    private static final ParseField FUZZY_TRANSPOSITIONS_FIELD;
    private final Object value;
    private final Map<String, Float> fieldsBoosts;
    private MultiMatchQueryBuilderUnderdog.Type type;
    private Operator operator;
    private String analyzer;
    private int slop;
    private Fuzziness fuzziness;
    private int prefixLength;
    private int maxExpansions;
    private String minimumShouldMatch;
    private String fuzzyRewrite;
    private Boolean useDisMax;
    private Float tieBreaker;
    private Boolean lenient;
    private Float cutoffFrequency;
    private MatchQuery.ZeroTermsQuery zeroTermsQuery;
    private boolean autoGenerateSynonymsPhraseQuery;
    private boolean fuzzyTranspositions;

    public MultiMatchQueryBuilderUnderdog.Type getType() {
        return this.type;
    }

    public MultiMatchQueryBuilderUnderdog(Object value, String... fields) {
        this.type = DEFAULT_TYPE;
        this.operator = DEFAULT_OPERATOR;
        this.slop = 0;
        this.prefixLength = 0;
        this.maxExpansions = 50;
        this.fuzzyRewrite = null;
        this.cutoffFrequency = null;
        this.zeroTermsQuery = DEFAULT_ZERO_TERMS_QUERY;
        this.autoGenerateSynonymsPhraseQuery = true;
        this.fuzzyTranspositions = true;
        if (value == null) {
            throw new IllegalArgumentException("[multi_match] requires query value");
        } else if (fields == null) {
            throw new IllegalArgumentException("[multi_match] requires fields at initialization time");
        } else {
            this.value = value;
            this.fieldsBoosts = new TreeMap();
            String[] var3 = fields;
            int var4 = fields.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String field = var3[var5];
                this.field(field);
            }

        }
    }

    public MultiMatchQueryBuilderUnderdog(StreamInput in) throws IOException {
        super(in);
        this.type = DEFAULT_TYPE;
        this.operator = DEFAULT_OPERATOR;
        this.slop = 0;
        this.prefixLength = 0;
        this.maxExpansions = 50;
        this.fuzzyRewrite = null;
        this.cutoffFrequency = null;
        this.zeroTermsQuery = DEFAULT_ZERO_TERMS_QUERY;
        this.autoGenerateSynonymsPhraseQuery = true;
        this.fuzzyTranspositions = true;
        this.value = in.readGenericValue();
        int size = in.readVInt();
        this.fieldsBoosts = new TreeMap();

        for(int i = 0; i < size; ++i) {
            String field = in.readString();
            float boost = in.readFloat();
            this.checkNegativeBoost(boost);
            this.fieldsBoosts.put(field, boost);
        }

        this.type = MultiMatchQueryBuilderUnderdog.Type.readFromStream(in);
        this.operator = Operator.readFromStream(in);
        this.analyzer = in.readOptionalString();
        this.slop = in.readVInt();
        this.fuzziness = (Fuzziness)in.readOptionalWriteable(Fuzziness::new);
        this.prefixLength = in.readVInt();
        this.maxExpansions = in.readVInt();
        this.minimumShouldMatch = in.readOptionalString();
        this.fuzzyRewrite = in.readOptionalString();
        this.useDisMax = in.readOptionalBoolean();
        this.tieBreaker = in.readOptionalFloat();
        if (in.getVersion().onOrAfter(Version.V_6_1_0)) {
            this.lenient = in.readOptionalBoolean();
        } else {
            this.lenient = in.readBoolean();
        }

        this.cutoffFrequency = in.readOptionalFloat();
        this.zeroTermsQuery = MatchQuery.ZeroTermsQuery.readFromStream(in);
        if (in.getVersion().onOrAfter(Version.V_6_1_0)) {
            this.autoGenerateSynonymsPhraseQuery = in.readBoolean();
            this.fuzzyTranspositions = in.readBoolean();
        }

    }

    protected void doWriteTo(StreamOutput out) throws IOException {
        out.writeGenericValue(this.value);
        out.writeVInt(this.fieldsBoosts.size());
        Iterator var2 = this.fieldsBoosts.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<String, Float> fieldsEntry = (Map.Entry)var2.next();
            out.writeString((String)fieldsEntry.getKey());
            out.writeFloat((Float)fieldsEntry.getValue());
        }

        this.type.writeTo(out);
        this.operator.writeTo(out);
        out.writeOptionalString(this.analyzer);
        out.writeVInt(this.slop);
        out.writeOptionalWriteable(this.fuzziness);
        out.writeVInt(this.prefixLength);
        out.writeVInt(this.maxExpansions);
        out.writeOptionalString(this.minimumShouldMatch);
        out.writeOptionalString(this.fuzzyRewrite);
        out.writeOptionalBoolean(this.useDisMax);
        out.writeOptionalFloat(this.tieBreaker);
        if (out.getVersion().onOrAfter(Version.V_6_1_0)) {
            out.writeOptionalBoolean(this.lenient);
        } else {
            out.writeBoolean(this.lenient == null ? false : this.lenient);
        }

        out.writeOptionalFloat(this.cutoffFrequency);
        this.zeroTermsQuery.writeTo(out);
        if (out.getVersion().onOrAfter(Version.V_6_1_0)) {
            out.writeBoolean(this.autoGenerateSynonymsPhraseQuery);
            out.writeBoolean(this.fuzzyTranspositions);
        }

    }

    public Object value() {
        return this.value;
    }

    public MultiMatchQueryBuilderUnderdog field(String field) {
        if (Strings.isEmpty(field)) {
            throw new IllegalArgumentException("supplied field is null or empty.");
        } else {
            this.fieldsBoosts.put(field, 1.0F);
            return this;
        }
    }

    public MultiMatchQueryBuilderUnderdog field(String field, float boost) {
        if (Strings.isEmpty(field)) {
            throw new IllegalArgumentException("supplied field is null or empty.");
        } else {
            this.checkNegativeBoost(boost);
            this.fieldsBoosts.put(field, boost);
            return this;
        }
    }

    public MultiMatchQueryBuilderUnderdog fields(Map<String, Float> fields) {
        Iterator var2 = fields.values().iterator();

        while(var2.hasNext()) {
            float fieldBoost = (Float)var2.next();
            this.checkNegativeBoost(fieldBoost);
        }

        this.fieldsBoosts.putAll(fields);
        return this;
    }

    public Map<String, Float> fields() {
        return this.fieldsBoosts;
    }

    public MultiMatchQueryBuilderUnderdog type(MultiMatchQueryBuilderUnderdog.Type type) {
        if (type == null) {
            throw new IllegalArgumentException("[multi_match] requires type to be non-null");
        } else {
            this.type = type;
            return this;
        }
    }

    public MultiMatchQueryBuilderUnderdog type(Object type) {
        if (type == null) {
            throw new IllegalArgumentException("[multi_match] requires type to be non-null");
        } else {
            this.type = MultiMatchQueryBuilderUnderdog.Type.parse(type.toString().toLowerCase(Locale.ROOT), LoggingDeprecationHandler.INSTANCE);
            return this;
        }
    }

    public MultiMatchQueryBuilderUnderdog.Type type() {
        return this.type;
    }

    public MultiMatchQueryBuilderUnderdog operator(Operator operator) {
        if (operator == null) {
            throw new IllegalArgumentException("[multi_match] requires operator to be non-null");
        } else {
            this.operator = operator;
            return this;
        }
    }

    public Operator operator() {
        return this.operator;
    }

    public MultiMatchQueryBuilderUnderdog analyzer(String analyzer) {
        this.analyzer = analyzer;
        return this;
    }

    public String analyzer() {
        return this.analyzer;
    }

    public MultiMatchQueryBuilderUnderdog slop(int slop) {
        if (slop < 0) {
            throw new IllegalArgumentException("No negative slop allowed.");
        } else {
            this.slop = slop;
            return this;
        }
    }

    public int slop() {
        return this.slop;
    }

    public MultiMatchQueryBuilderUnderdog fuzziness(Object fuzziness) {
        if (fuzziness != null) {
            this.fuzziness = Fuzziness.build(fuzziness);
        }

        return this;
    }

    public Fuzziness fuzziness() {
        return this.fuzziness;
    }

    public MultiMatchQueryBuilderUnderdog prefixLength(int prefixLength) {
        if (prefixLength < 0) {
            throw new IllegalArgumentException("No negative prefix length allowed.");
        } else {
            this.prefixLength = prefixLength;
            return this;
        }
    }

    public int prefixLength() {
        return this.prefixLength;
    }

    public MultiMatchQueryBuilderUnderdog maxExpansions(int maxExpansions) {
        if (maxExpansions <= 0) {
            throw new IllegalArgumentException("Max expansions must be strictly great than zero.");
        } else {
            this.maxExpansions = maxExpansions;
            return this;
        }
    }

    public int maxExpansions() {
        return this.maxExpansions;
    }

    public MultiMatchQueryBuilderUnderdog minimumShouldMatch(String minimumShouldMatch) {
        this.minimumShouldMatch = minimumShouldMatch;
        return this;
    }

    public String minimumShouldMatch() {
        return this.minimumShouldMatch;
    }

    public MultiMatchQueryBuilderUnderdog fuzzyRewrite(String fuzzyRewrite) {
        this.fuzzyRewrite = fuzzyRewrite;
        return this;
    }

    public String fuzzyRewrite() {
        return this.fuzzyRewrite;
    }

    /** @deprecated */
    @Deprecated
    public MultiMatchQueryBuilderUnderdog useDisMax(Boolean useDisMax) {
        this.useDisMax = useDisMax;
        return this;
    }

    public Boolean useDisMax() {
        return this.useDisMax;
    }

    public MultiMatchQueryBuilderUnderdog tieBreaker(float tieBreaker) {
        this.tieBreaker = tieBreaker;
        return this;
    }

    public MultiMatchQueryBuilderUnderdog tieBreaker(Float tieBreaker) {
        this.tieBreaker = tieBreaker;
        return this;
    }

    public Float tieBreaker() {
        return this.tieBreaker;
    }

    public MultiMatchQueryBuilderUnderdog lenient(boolean lenient) {
        this.lenient = lenient;
        return this;
    }

    public boolean lenient() {
        return this.lenient == null ? false : this.lenient;
    }

    public MultiMatchQueryBuilderUnderdog cutoffFrequency(float cutoff) {
        this.cutoffFrequency = cutoff;
        return this;
    }

    public MultiMatchQueryBuilderUnderdog cutoffFrequency(Float cutoff) {
        this.cutoffFrequency = cutoff;
        return this;
    }

    public Float cutoffFrequency() {
        return this.cutoffFrequency;
    }

    public MultiMatchQueryBuilderUnderdog zeroTermsQuery(MatchQuery.ZeroTermsQuery zeroTermsQuery) {
        if (zeroTermsQuery == null) {
            throw new IllegalArgumentException("[multi_match] requires zero terms query to be non-null");
        } else {
            this.zeroTermsQuery = zeroTermsQuery;
            return this;
        }
    }

    public MatchQuery.ZeroTermsQuery zeroTermsQuery() {
        return this.zeroTermsQuery;
    }

    public MultiMatchQueryBuilderUnderdog autoGenerateSynonymsPhraseQuery(boolean enable) {
        this.autoGenerateSynonymsPhraseQuery = enable;
        return this;
    }

    public boolean autoGenerateSynonymsPhraseQuery() {
        return this.autoGenerateSynonymsPhraseQuery;
    }

    public boolean fuzzyTranspositions() {
        return this.fuzzyTranspositions;
    }

    public MultiMatchQueryBuilderUnderdog fuzzyTranspositions(boolean fuzzyTranspositions) {
        this.fuzzyTranspositions = fuzzyTranspositions;
        return this;
    }

    public void doXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject("multi_match");
        builder.field(QUERY_FIELD.getPreferredName(), this.value);
        builder.startArray(FIELDS_FIELD.getPreferredName());
        Iterator var3 = this.fieldsBoosts.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<String, Float> fieldEntry = (Map.Entry)var3.next();
            builder.value((String)fieldEntry.getKey() + "^" + fieldEntry.getValue());
        }

        builder.endArray();
        builder.field(TYPE_FIELD.getPreferredName(), this.type.toString().toLowerCase(Locale.ENGLISH));
        builder.field(OPERATOR_FIELD.getPreferredName(), this.operator.toString());
        if (this.analyzer != null) {
            builder.field(ANALYZER_FIELD.getPreferredName(), this.analyzer);
        }

        builder.field(SLOP_FIELD.getPreferredName(), this.slop);
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

        if (this.useDisMax != null) {
            builder.field(USE_DIS_MAX_FIELD.getPreferredName(), this.useDisMax);
        }

        if (this.tieBreaker != null) {
            builder.field(TIE_BREAKER_FIELD.getPreferredName(), this.tieBreaker);
        }

        if (this.lenient != null) {
            builder.field(LENIENT_FIELD.getPreferredName(), this.lenient);
        }

        if (this.cutoffFrequency != null) {
            builder.field(CUTOFF_FREQUENCY_FIELD.getPreferredName(), this.cutoffFrequency);
        }

        builder.field(ZERO_TERMS_QUERY_FIELD.getPreferredName(), this.zeroTermsQuery.toString());
//        builder.field(GENERATE_SYNONYMS_PHRASE_QUERY.getPreferredName(), this.autoGenerateSynonymsPhraseQuery);
        builder.field(FUZZY_TRANSPOSITIONS_FIELD.getPreferredName(), this.fuzzyTranspositions);
        this.printBoostAndQueryName(builder);
        builder.endObject();
    }

    public static MultiMatchQueryBuilderUnderdog fromXContent(XContentParser parser) throws IOException {
        Object value = null;
        Map<String, Float> fieldsBoosts = new HashMap();
        MultiMatchQueryBuilderUnderdog.Type type = DEFAULT_TYPE;
        String analyzer = null;
        int slop = 0;
        Fuzziness fuzziness = null;
        int prefixLength = 0;
        int maxExpansions = 50;
        Operator operator = DEFAULT_OPERATOR;
        String minimumShouldMatch = null;
        String fuzzyRewrite = null;
        Boolean useDisMax = null;
        Float tieBreaker = null;
        Float cutoffFrequency = null;
        Boolean lenient = null;
        MatchQuery.ZeroTermsQuery zeroTermsQuery = DEFAULT_ZERO_TERMS_QUERY;
        boolean autoGenerateSynonymsPhraseQuery = true;
        boolean fuzzyTranspositions = true;
        float boost = 1.0F;
        String queryName = null;
        String currentFieldName = null;

        while(true) {
            XContentParser.Token token;
            while((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    currentFieldName = parser.currentName();
                } else if (FIELDS_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                    if (token == XContentParser.Token.START_ARRAY) {
                        while(parser.nextToken() != XContentParser.Token.END_ARRAY) {
                            parseFieldAndBoost(parser, fieldsBoosts);
                        }
                    } else {
                        if (!token.isValue()) {
                            throw new ParsingException(parser.getTokenLocation(), "[multi_match] query does not support [" + currentFieldName + "]", new Object[0]);
                        }

                        parseFieldAndBoost(parser, fieldsBoosts);
                    }
                } else {
                    if (!token.isValue()) {
                        throw new ParsingException(parser.getTokenLocation(), "[multi_match] unknown token [" + token + "] after [" + currentFieldName + "]", new Object[0]);
                    }

                    if (QUERY_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                        value = parser.objectText();
                    } else if (TYPE_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                        type = MultiMatchQueryBuilderUnderdog.Type.parse(parser.text(), parser.getDeprecationHandler());
                    } else if (ANALYZER_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                        analyzer = parser.text();
                    } else if (AbstractQueryBuilder.BOOST_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                        boost = parser.floatValue();
                    } else if (SLOP_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                        slop = parser.intValue();
                    } else if (Fuzziness.FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                        fuzziness = Fuzziness.parse(parser);
                    } else if (PREFIX_LENGTH_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                        prefixLength = parser.intValue();
                    } else if (MAX_EXPANSIONS_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                        maxExpansions = parser.intValue();
                    } else if (OPERATOR_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                        operator = Operator.fromString(parser.text());
                    } else if (MINIMUM_SHOULD_MATCH_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                        minimumShouldMatch = parser.textOrNull();
                    } else if (FUZZY_REWRITE_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                        fuzzyRewrite = parser.textOrNull();
                    } else if (USE_DIS_MAX_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                        useDisMax = parser.booleanValue();
                    } else if (TIE_BREAKER_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                        tieBreaker = parser.floatValue();
                    } else if (CUTOFF_FREQUENCY_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                        cutoffFrequency = parser.floatValue();
                    } else if (LENIENT_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                        lenient = parser.booleanValue();
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
                    }
//                    else if (GENERATE_SYNONYMS_PHRASE_QUERY.match(currentFieldName, parser.getDeprecationHandler())) {
//                        autoGenerateSynonymsPhraseQuery = parser.booleanValue();
//                    }
                        else {
                        if (!FUZZY_TRANSPOSITIONS_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                            throw new ParsingException(parser.getTokenLocation(), "[multi_match] query does not support [" + currentFieldName + "]", new Object[0]);
                        }

                        fuzzyTranspositions = parser.booleanValue();
                    }
                }
            }

            if (value == null) {
                throw new ParsingException(parser.getTokenLocation(), "No text specified for multi_match query", new Object[0]);
            }

            if (fuzziness == null || type != MultiMatchQueryBuilderUnderdog.Type.CROSS_FIELDS && type != MultiMatchQueryBuilderUnderdog.Type.PHRASE && type != MultiMatchQueryBuilderUnderdog.Type.PHRASE_PREFIX) {
                MultiMatchQueryBuilderUnderdog builder = ((MultiMatchQueryBuilderUnderdog)((MultiMatchQueryBuilderUnderdog)(new MultiMatchQueryBuilderUnderdog(value, new String[0])).fields(fieldsBoosts).type(type).analyzer(analyzer).cutoffFrequency(cutoffFrequency).fuzziness(fuzziness).fuzzyRewrite(fuzzyRewrite).useDisMax(useDisMax).maxExpansions(maxExpansions).minimumShouldMatch(minimumShouldMatch).operator(operator).prefixLength(prefixLength).slop(slop).tieBreaker(tieBreaker).zeroTermsQuery(zeroTermsQuery).autoGenerateSynonymsPhraseQuery(autoGenerateSynonymsPhraseQuery).boost(boost)).queryName(queryName)).fuzzyTranspositions(fuzzyTranspositions);
                if (lenient != null) {
                    builder.lenient(lenient);
                }

                return builder;
            }

            throw new ParsingException(parser.getTokenLocation(), "Fuzziness not allowed for type [" + type.parseField.getPreferredName() + "]", new Object[0]);
        }
    }

    private static void parseFieldAndBoost(XContentParser parser, Map<String, Float> fieldsBoosts) throws IOException {
        String fField = null;
        Float fBoost = 1.0F;
        char[] fieldText = parser.textCharacters();
        int end = parser.textOffset() + parser.textLength();

        for(int i = parser.textOffset(); i < end; ++i) {
            if (fieldText[i] == '^') {
                int relativeLocation = i - parser.textOffset();
                fField = new String(fieldText, parser.textOffset(), relativeLocation);
                fBoost = Float.parseFloat(new String(fieldText, i + 1, parser.textLength() - relativeLocation - 1));
                break;
            }
        }

        if (fField == null) {
            fField = parser.text();
        }

        fieldsBoosts.put(fField, fBoost);
    }

    public String getWriteableName() {
        return "multi_match";
    }

    protected Query doToQuery(QueryShardContext context) throws IOException {
        MultiMatchQuery multiMatchQuery = new MultiMatchQuery(context);
        if (this.analyzer != null) {
            if (context.getIndexAnalyzers().get(this.analyzer) == null) {
                throw new QueryShardException(context, "[multi_match] analyzer [" + this.analyzer + "] not found", new Object[0]);
            }

            multiMatchQuery.setAnalyzer(this.analyzer);
        }

        multiMatchQuery.setPhraseSlop(this.slop);
        if (this.fuzziness != null) {
            multiMatchQuery.setFuzziness(this.fuzziness);
        }

        multiMatchQuery.setFuzzyPrefixLength(this.prefixLength);
        multiMatchQuery.setMaxExpansions(this.maxExpansions);
        multiMatchQuery.setOccur(this.operator.toBooleanClauseOccur());
        if (this.fuzzyRewrite != null) {
            multiMatchQuery.setFuzzyRewriteMethod(QueryParsers.parseRewriteMethod(this.fuzzyRewrite, (MultiTermQuery.RewriteMethod)null, LoggingDeprecationHandler.INSTANCE));
        }

        if (this.tieBreaker != null) {
            multiMatchQuery.setTieBreaker(this.tieBreaker);
        }

        if (this.cutoffFrequency != null) {
            multiMatchQuery.setCommonTermsCutoff(this.cutoffFrequency);
        }

        if (this.lenient != null) {
            multiMatchQuery.setLenient(this.lenient);
        }

        multiMatchQuery.setZeroTermsQuery(this.zeroTermsQuery);
        multiMatchQuery.setAutoGenerateSynonymsPhraseQuery(this.autoGenerateSynonymsPhraseQuery);
        multiMatchQuery.setTranspositions(this.fuzzyTranspositions);
        if (this.useDisMax != null) {
            boolean typeUsesDismax = this.type.tieBreaker() != 1.0F;
            if (typeUsesDismax != this.useDisMax) {
                if (this.useDisMax && this.tieBreaker == null) {
                    multiMatchQuery.setTieBreaker(0.0F);
                } else {
                    multiMatchQuery.setTieBreaker(1.0F);
                }
            }
        }

        Map newFieldsBoosts;
        if (this.fieldsBoosts.isEmpty()) {
            List<String> defaultFields = context.defaultFields();
            boolean isAllField = defaultFields.size() == 1 && Regex.isMatchAllPattern((String)defaultFields.get(0));
            if (isAllField && this.lenient == null) {
                multiMatchQuery.setLenient(true);
            }

            newFieldsBoosts = QueryParserHelper.resolveMappingFields(context, QueryParserHelper.parseFieldsAndWeights(defaultFields));
        } else {
            newFieldsBoosts = QueryParserHelper.resolveMappingFields(context, this.fieldsBoosts);
        }

        return multiMatchQuery.parse(MultiMatchQueryBuilder.Type.MOST_FIELDS, newFieldsBoosts, this.value, this.minimumShouldMatch);
    }

    protected int doHashCode() {
        return Objects.hash(new Object[]{this.value, this.fieldsBoosts, this.type, this.operator, this.analyzer, this.slop, this.fuzziness, this.prefixLength, this.maxExpansions, this.minimumShouldMatch, this.fuzzyRewrite, this.useDisMax, this.tieBreaker, this.lenient, this.cutoffFrequency, this.zeroTermsQuery, this.autoGenerateSynonymsPhraseQuery, this.fuzzyTranspositions});
    }

    protected boolean doEquals(MultiMatchQueryBuilderUnderdog other) {
        return Objects.equals(this.value, other.value) && Objects.equals(this.fieldsBoosts, other.fieldsBoosts) && Objects.equals(this.type, other.type) && Objects.equals(this.operator, other.operator) && Objects.equals(this.analyzer, other.analyzer) && Objects.equals(this.slop, other.slop) && Objects.equals(this.fuzziness, other.fuzziness) && Objects.equals(this.prefixLength, other.prefixLength) && Objects.equals(this.maxExpansions, other.maxExpansions) && Objects.equals(this.minimumShouldMatch, other.minimumShouldMatch) && Objects.equals(this.fuzzyRewrite, other.fuzzyRewrite) && Objects.equals(this.useDisMax, other.useDisMax) && Objects.equals(this.tieBreaker, other.tieBreaker) && Objects.equals(this.lenient, other.lenient) && Objects.equals(this.cutoffFrequency, other.cutoffFrequency) && Objects.equals(this.zeroTermsQuery, other.zeroTermsQuery) && Objects.equals(this.autoGenerateSynonymsPhraseQuery, other.autoGenerateSynonymsPhraseQuery) && Objects.equals(this.fuzzyTranspositions, other.fuzzyTranspositions);
    }

    static {
        DEFAULT_TYPE = MultiMatchQueryBuilderUnderdog.Type.BEST_FIELDS;
        DEFAULT_OPERATOR = Operator.OR;
        DEFAULT_ZERO_TERMS_QUERY = MatchQuery.DEFAULT_ZERO_TERMS_QUERY;
        SLOP_FIELD = new ParseField("slop", new String[0]);
        ZERO_TERMS_QUERY_FIELD = new ParseField("zero_terms_query", new String[0]);
        LENIENT_FIELD = new ParseField("lenient", new String[0]);
        CUTOFF_FREQUENCY_FIELD = new ParseField("cutoff_frequency", new String[0]);
        TIE_BREAKER_FIELD = new ParseField("tie_breaker", new String[0]);
        USE_DIS_MAX_FIELD = (new ParseField("use_dis_max", new String[0])).withAllDeprecated("use tie_breaker instead");
        FUZZY_REWRITE_FIELD = new ParseField("fuzzy_rewrite", new String[0]);
        MINIMUM_SHOULD_MATCH_FIELD = new ParseField("minimum_should_match", new String[0]);
        OPERATOR_FIELD = new ParseField("operator", new String[0]);
        MAX_EXPANSIONS_FIELD = new ParseField("max_expansions", new String[0]);
        PREFIX_LENGTH_FIELD = new ParseField("prefix_length", new String[0]);
        ANALYZER_FIELD = new ParseField("analyzer", new String[0]);
        TYPE_FIELD = new ParseField("type", new String[0]);
        QUERY_FIELD = new ParseField("query", new String[0]);
        FIELDS_FIELD = new ParseField("fields", new String[0]);
//        GENERATE_SYNONYMS_PHRASE_QUERY = new ParseField("auto_generate_synonyms_phrase_query", new String[0]);
        FUZZY_TRANSPOSITIONS_FIELD = new ParseField("fuzzy_transpositions", new String[0]);
    }

    public static enum Type implements Writeable {
        BEST_FIELDS(org.elasticsearch.index.search.MatchQuery.Type.BOOLEAN, 0.0F, new ParseField("best_fields", new String[]{"boolean"})),
        MOST_FIELDS(org.elasticsearch.index.search.MatchQuery.Type.BOOLEAN, 1.0F, new ParseField("most_fields", new String[0])),
        CROSS_FIELDS(org.elasticsearch.index.search.MatchQuery.Type.BOOLEAN, 0.0F, new ParseField("cross_fields", new String[0])),
        PHRASE(org.elasticsearch.index.search.MatchQuery.Type.PHRASE, 0.0F, new ParseField("phrase", new String[0])),
        PHRASE_PREFIX(org.elasticsearch.index.search.MatchQuery.Type.PHRASE_PREFIX, 0.0F, new ParseField("phrase_prefix", new String[0]));

        private org.elasticsearch.index.search.MatchQuery.Type matchQueryType;
        private final float tieBreaker;
        private final ParseField parseField;

        private Type(org.elasticsearch.index.search.MatchQuery.Type matchQueryType, float tieBreaker, ParseField parseField) {
            this.matchQueryType = matchQueryType;
            this.tieBreaker = tieBreaker;
            this.parseField = parseField;
        }

        public float tieBreaker() {
            return this.tieBreaker;
        }

        public org.elasticsearch.index.search.MatchQuery.Type matchQueryType() {
            return this.matchQueryType;
        }

        public ParseField parseField() {
            return this.parseField;
        }

        public static MultiMatchQueryBuilderUnderdog.Type parse(String value, DeprecationHandler deprecationHandler) {
            MultiMatchQueryBuilderUnderdog.Type[] values = values();
            MultiMatchQueryBuilderUnderdog.Type type = null;
            MultiMatchQueryBuilderUnderdog.Type[] var4 = values;
            int var5 = values.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                MultiMatchQueryBuilderUnderdog.Type t = var4[var6];
                if (t.parseField().match(value, deprecationHandler)) {
                    type = t;
                    break;
                }
            }

            if (type == null) {
                throw new ElasticsearchParseException("failed to parse [{}] query type [{}]. unknown type.", new Object[]{"multi_match", value});
            } else {
                return type;
            }
        }

        public static MultiMatchQueryBuilderUnderdog.Type readFromStream(StreamInput in) throws IOException {
            return values()[in.readVInt()];
        }

        public void writeTo(StreamOutput out) throws IOException {
            out.writeVInt(this.ordinal());
        }
    }
}
