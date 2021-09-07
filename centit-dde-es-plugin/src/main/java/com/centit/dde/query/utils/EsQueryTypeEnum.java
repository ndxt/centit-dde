package com.centit.dde.query.utils;

/**
 * dde支持的es查询方式
 */
public enum  EsQueryTypeEnum {
    //精确查询（查询条件不会分词，输入的是什么就是什么）
    TERM,
    //精确查询（一个字段匹配多个值）
    TERMS,
    //ids查询
    IDS,
    //全文检索（查询时查询条件会被分词）
    MATCH,
    //全部查询
    MATCH_ALL,
    //查询 field1 或 field2 中包含有 value1 或者 value2 的所有的数据
    MULTI_MATCH,
    //查询 field 中包含有 value1 和 value 的所有数据
    QUERY_STRING,
    //短语查询   match_phrase查询 (返回结果必须满足2个条件  条件一：查询条件分词后的所有词或短语都必须在文档中包含  条件二：分词后的位置和顺序必须一致
    // 但是条件二可以通过slop参数来改变位置距离容差值，在满足条件一的同时，分词后的位置只要满足slop 设置的值就可以返回)
    MATCH_PHRASE,
    //与match_phrase查询类似,但是会对最后一个Token在倒排序索引列表中进行通配符搜索,通过max_expansions 参数来控制模糊匹配数控制 设置几就代表在最后一个token后偏移几个字进行匹配
    // 例如最后一个token值是"最后"，那么设置max_expansions=2时 就会查询出结果为“最后一个”这样的结果。
    MATCH_PHRASE_PREFIX,
    //区间查询
    RANGE,
    //纠正查询
    FUZZINESS;
}
