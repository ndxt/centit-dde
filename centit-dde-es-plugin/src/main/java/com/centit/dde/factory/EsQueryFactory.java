package com.centit.dde.factory;

import com.centit.dde.query.EsQuery;
import com.centit.dde.query.impl.*;
import com.centit.dde.query.utils.EsQueryType;

public class EsQueryFactory {
    public static EsQuery getEsQueryType(String queryType){
        switch (queryType){
            case EsQueryType.TERM:
              return  new EsTermQueryImpl();
            case EsQueryType.TERMS:
                return  new EsTermsQueryImpl();
            case EsQueryType.MATCH:
                return  new EsMatchQueryImpl();
            case EsQueryType.MATCH_ALL:
                return  new EsMatchAllQueryImpl();
            case EsQueryType.MATCH_PHRASE:
                return  new EsMatchPhraseQueryImpl();
            case EsQueryType.BOOLEAN:
                return  new EsBooleanQueryImpl();
        }
        return null;
    }
}
