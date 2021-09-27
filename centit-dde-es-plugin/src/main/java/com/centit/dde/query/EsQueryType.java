package com.centit.dde.query;

/**
 * dde支持的es查询方式
 */
public class   EsQueryType {
    //精确查询（查询条件不会分词，输入的是什么就是什么）
    public  final  static String  TERM="term";
    //精确查询（一个字段匹配多个值）
    public  final  static String TERMS="terms";
    //全文检索（查询时查询条件会被分词）
    public  final  static String MATCH="match";
    //全文检索（查询时查询条件会被分词）
    public  final  static String IDS="ids";
    //全部查询
    public  final  static String MATCH_ALL="match_all";
    //区间查询
    public  final  static String RANGE="range";
    //查询的数据进行缓存，提高效率
    public  final  static String CONSTANT_SCORE="constant_score";
    //短语查询   match_phrase查询 (返回结果必须满足2个条件  条件一：查询条件分词后的所有词或短语都必须在文档中包含  条件二：分词后的位置和顺序必须一致
    // 但是条件二可以通过slop参数来改变位置距离容差值，在满足条件一的同时，分词后的位置只要满足slop 设置的值就可以返回)
    public  final  static String MATCH_PHRASE="match_phrase";
    //与match_phrase查询类似,但是会对最后一个Token在倒排序索引列表中进行通配符搜索,通过max_expansions 参数来控制模糊匹配数控制 设置几就代表在最后一个token后偏移几个字进行匹配
    // 例如最后一个token值是"最后"，那么设置max_expansions=2时 就会查询出结果为“最后一个”这样的结果。
    public  final  static String MATCH_PHRASE_PREFIX="match_phrase_prefix";

    //一值多字段查询，将查询条件进行分词， 分词后进行对多个字段的值进行匹配
    public  final  static String MULTI_MATCH="multi_match";
    /**
     * query_string
     *  1.不写字段名时默认查询全部字段
     *  2.查询时字段可以写多个，将查询条件分词后进行几个字段的值匹配，有点类型 multi_match  一值匹配多字段
     *  3.query_string查询时可以通过operator 来控制逻辑判断   也就是控制分词后词组间用and  还是or进行查询
     *  4.query_string  和simple_query_string 的区别就是 query_string可以直接在查询条件中加OR AND
     *    query_string会自动识别OR  AND    ;simple_query_string 则不会，他会将其中的OR  AND  作为查询条件中的一个进行匹配，会进行分词处理
     *    simple_query_string 必须通过operator来控制逻辑判断，到底是用or  还是 and  默认or;
     */
    public  final  static String QUERYSTRING="query_string";

    //类似query_string   2者的区别就是前者会对查询条件中的or  and 认为是逻辑判断条件   后者会直接认为是查询条件中的一个条件
    public  final  static String SIMPLEQUERYSTRING="simple_query_string";

    //通配符查询
    public  final  static String WILDCARD = "wildcard";
}
