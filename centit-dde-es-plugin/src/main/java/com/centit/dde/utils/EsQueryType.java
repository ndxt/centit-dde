package com.centit.dde.utils;

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
    //全部查询
    public  final  static String MATCH_ALL="matchAll";
    //区间查询
    public  final  static String RANGE="range";
    //模糊查询
    public  final  static String FUZZINESS="fuzziness";
    //查询的数据进行缓存，提高效率
    public  final  static String CONSTANT_SCORE="constantScore";
    //短语查询   match_phrase查询 (返回结果必须满足2个条件  条件一：查询条件分词后的所有词或短语都必须在文档中包含  条件二：分词后的位置和顺序必须一致
    // 但是条件二可以通过slop参数来改变位置距离容差值，在满足条件一的同时，分词后的位置只要满足slop 设置的值就可以返回)
    public  final  static String MATCH_PHRASE="matchPhrase";
    //与match_phrase查询类似,但是会对最后一个Token在倒排序索引列表中进行通配符搜索,通过max_expansions 参数来控制模糊匹配数控制 设置几就代表在最后一个token后偏移几个字进行匹配
    // 例如最后一个token值是"最后"，那么设置max_expansions=2时 就会查询出结果为“最后一个”这样的结果。
    public  final  static String MATCH_PHRASE_PREFIX="matchPhrasePrefix";
    //查询 field1 或 field2 中包含有 value1 或者 value2 的所有的数据
    public  final  static String MULTI_MATCH="multiMatch";
    //查询 field 中包含有 value1 和 value 的所有数据
    public  final  static String QUERYSTRING="queryString";


}
