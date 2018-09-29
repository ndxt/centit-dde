package com.centit.dde.util;

import java.util.ArrayList;
import java.util.List;

import com.centit.support.algorithm.StringRegularOpt;
import com.centit.support.compiler.Lexer;

/**
 * 这个类中的代码 将全部删除，使用 Database jar包中的QueryUtils中对应的方法
 */
@Deprecated
public class SQLUtils {
    
    public static String removeOrderBy(String sql){
        Lexer lex = new Lexer();
        lex.setFormula(sql);
        String aWord = lex.getAWord();
        int nPos=lex.getCurrPos();
        while(aWord!=null && !"".equals(aWord) && ! "order".equalsIgnoreCase(aWord)){
            nPos=lex.getCurrPos();
            aWord = lex.getAWord();
            if(aWord==null  || "".equals(aWord) )
                return sql;
            if(aWord.equals("(")){
                lex.seekToRightBracket();
                nPos=lex.getCurrPos();
                aWord = lex.getAWord();
            }
        }
        return sql.substring(0, nPos);
    }
    
    /**
     * 将sql语句  filed部分为界 分三段
     * @param sql
     * @return
     */
    public static List<String> splitSqlByFields(String sql) {
        Lexer lex = new Lexer();
        lex.setFormula(sql);
        List<String> sqlPiece = new ArrayList<String>();
        
        String aWord = lex.getAWord();

        while(aWord!=null && !"".equals(aWord) && ! "select".equalsIgnoreCase(aWord)){
            aWord = lex.getAWord();
            if(aWord==null  || "".equals(aWord) )
                return sqlPiece;
            if(aWord.equals("(")){
                lex.seekToRightBracket();
                aWord = lex.getAWord();
            }
        }
        int nSelectPos = lex.getCurrPos();
        int nFieldBegin = lex.getCurrPos();
        //特别处理sql server 的 top 语句
        aWord = lex.getAWord();
        if("top".equalsIgnoreCase(aWord)){
            aWord = lex.getAWord();
            if(StringRegularOpt.isNumber(aWord) )
                nFieldBegin = lex.getCurrPos();
        }
        
        while(aWord!=null && !"".equals(aWord) && ! "from".equalsIgnoreCase(aWord)){
            aWord = lex.getAWord();
            if(aWord==null  || "".equals(aWord))
                return sqlPiece;
            if(aWord.equals("(")){
                lex.seekToRightBracket();
                aWord = lex.getAWord();
            }
        }
        int nFieldEnd=lex.getCurrPos();
        
        sqlPiece.add(sql.substring(0,nSelectPos) );
        sqlPiece.add(sql.substring(nFieldBegin,nFieldEnd) );
        sqlPiece.add(sql.substring(nFieldEnd) );
        if(nFieldBegin > nSelectPos) // 这有sqlserver 有 top 字句的语句 才有这部分
            sqlPiece.add(sql.substring(nSelectPos,nFieldBegin) );
        return sqlPiece;
    }
    /**
     * 转换为查询符合条件的数量的sql语句  需要考虑with语句
     * @param sql
     * @return
     */
    public static String buildGetCountSQL(String sql) {
        List<String> sqlPieces = splitSqlByFields( sql);
        if(sqlPieces==null || sqlPieces.size()< 3)
            return "";
        
        String sCountSql = sqlPieces.get(0) +" count(1) as rowcount from " + 
                removeOrderBy(sqlPieces.get(2));
        
        return sCountSql;
    }
    
    /**
     * 返回sql语句中所有的 命令变量（:变量名）,最后一个String 为转换为？变量的sql语句
     * @param sql
     * @return
     */
    public static List<String> getSqlNamedParameters(String sql) {
        StringBuilder sqlb = new StringBuilder();
        List<String> params = new ArrayList<String>();
        Lexer lex = new Lexer();
        lex.setFormula(sql);
        String aWord = lex.getAWord();
        while(aWord!=null && !"".equals(aWord)){
            if(":".equals(aWord)){
                aWord = lex.getAWord();
                if(aWord ==null || "".equals(aWord))
                    return params;
                params.add(aWord);  
                sqlb.append(" ? ");
            }else
                sqlb.append(aWord).append(" ");

            aWord = lex.getAWord();
        }
        params.add(sqlb.toString());
        return params;
    }
    
    /**
     * 返回sql语句中所有的 字段
     * @param sql
     * @return
     */
    public static List<String> getSqlFileds(String sql) {
        
        List<String> fields = new ArrayList<String>();
        List<String> sqlPieces = splitSqlByFields( sql);
        if(sqlPieces==null || sqlPieces.size()< 3)
            return fields;
        
        String sFieldSql = sqlPieces.get(1);
        Lexer lex = new Lexer();
        lex.setFormula(sFieldSql);
        
        int nPos =0;
        String aWord = lex.getAWord();
        
        while(aWord!=null && !"".equals(aWord) && !"from".equalsIgnoreCase(aWord)){
            int nPos2 = lex.getCurrPos();;
            
            int nLeftBracket = 0;
            while ( nLeftBracket >0 || (!"".equals(aWord) && ! ",".equals(aWord) && !"from".equalsIgnoreCase(aWord))){
                if( "(".equals(aWord)) nLeftBracket++;
                else if( ")".equals(aWord)) nLeftBracket--;
                if(nLeftBracket < 0)
                    break;
                nPos2 = lex.getCurrPos();
                aWord = lex.getAWord();
            }

            fields.add(sFieldSql.substring(nPos,nPos2).trim() );
            nPos = nPos2;
            if (",".equals(aWord) ) {
                nPos = lex.getCurrPos();
                aWord = lex.getAWord();
            }
        }

        return fields;
    }
    

}

