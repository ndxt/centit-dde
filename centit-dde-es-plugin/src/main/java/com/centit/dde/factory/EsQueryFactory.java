package com.centit.dde.factory;

import com.centit.dde.query.EsQuery;
import com.centit.dde.query.impl.EsMatchQueryImpl;
import com.centit.dde.query.impl.EsTermQueryImpl;
import com.centit.dde.query.impl.EsTermsQueryImpl;
import com.centit.dde.query.utils.EsQueryTypeEnum;

public class EsQueryFactory {
    public static EsQuery getEsQueryType(EsQueryTypeEnum queryTypeEnum){
        switch (queryTypeEnum){
            case TERM:
              return  new EsTermQueryImpl();
            case TERMS:
                return  new EsTermsQueryImpl();
            case MATCH:
                return  new EsMatchQueryImpl();
        }
        return null;
    }
}
