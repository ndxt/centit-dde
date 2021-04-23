package com.centit.dde.utils;

/**
 * dde支持的es
 */
public enum  EsQueryType {
    //精确
    TERM,
    TERMS,
    MATCH,
    MATCH_ALL,
    RANGE,
    EXISTS,
    MUST,
    MUST_NOT,
    SHOULD,
    CONSTANT_SCORE,
    MATCH_PHRASE,
    MULTI_MATCH
}
