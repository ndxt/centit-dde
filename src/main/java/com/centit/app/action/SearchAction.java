package com.centit.app.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.centit.core.action.BaseAction;
import com.centit.support.searcher.DocDesc;
import com.centit.support.searcher.SearchCondition;
import com.centit.support.searcher.Searcher;
import com.centit.sys.security.FUserDetail;

public class SearchAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    public String keyWord;
    public String beginTime;
    public String endTime;
    public List<String> typeList;
    public List<String> optList;
    public String owner;


    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }

    public List<String> getOptList() {
        return optList;
    }

    public void setOptList(List<String> optList) {
        this.optList = optList;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    //private List<DocDesc> doclist;
    private int docsum;

    public int getDocsum() {
        return docsum;
    }

    public void setDocsum(int docsum) {
        this.docsum = docsum;
    }

    public String search() {
        try {
            String sQuery = request.getParameter("query");
            String sGj = request.getParameter("gj");
            //Object gjObj = null;//((DynaBean) form).get("gj");
            //if(gjObj != null )
            //sGj = gjObj.toString();

            SearchCondition sc = new SearchCondition(sQuery);
            //BeanUtils.copyProperties(sc, form);

            String[] typelist = request.getParameterValues("types");
            String[] optlist = request.getParameterValues("opts");
            if (typelist != null) {
                Map<String, String> tl = new HashMap<String, String>();
                for (String tp : typelist) {
                    tl.put(tp, "1");
                    sc.addType(tp);
                }
                request.setAttribute("types", tl);
            }
            if (optlist != null) {
                Map<String, String> ol = new HashMap<String, String>();
                for (String op : optlist) {
                    ol.put(op, "1");
                    sc.addOpt(op);
                }
                request.setAttribute("opts", ol);
            }
            String onlyowner = request.getParameter("onlyowner");
            if ("yes".equals(onlyowner)) {
                FUserDetail ui = ((FUserDetail) this.getLoginUser());
                if (ui != null)
                    sc.setOwner(ui.getUsercode());
            }

            if (sc.getKeyWord() != null)
                sQuery = sc.getKeyWord();
            if (sQuery != null) {
                sQuery = sQuery.trim();
                if (StringUtils.isNotEmpty(sQuery)) {
                    sc.setKeyWord(sQuery);
                    List<DocDesc> doclist = Searcher.search(sc);
                    request.setAttribute("doclist", doclist);
                    request.setAttribute("docsum", doclist.size());
                } else
                    request.setAttribute("docsum", 0);
            } else
                request.setAttribute("docsum", 0);

            //	((DynaBean) form).set("gj", sGj);
            request.setAttribute("gj", sGj);
            return "search";
        } catch (Exception e) {
            e.printStackTrace();
            return "search";
        }
    }
}

