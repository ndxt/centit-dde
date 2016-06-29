package com.centit.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.core.dao.HQLUtils;
import com.centit.sys.po.AddressBook;

public class AddressBookDao extends BaseDaoImpl<AddressBook> {
    private static final long serialVersionUID = 1L;

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();
            filterField.put("memo", CodeBook.LIKE_HQL_ID);
            filterField.put("searchstring", CodeBook.LIKE_HQL_ID);
            filterField.put("bodytype", CodeBook.EQUAL_HQL_ID);
            filterField.put("unitcode", CodeBook.EQUAL_HQL_ID);
            filterField.put("usercode", CodeBook.EQUAL_HQL_ID);
            filterField.put("isprivate", CodeBook.EQUAL_HQL_ID);
            filterField.put("bodyname", CodeBook.LIKE_HQL_ID);
        }
        return filterField;
    }

    @Override
    public void saveObject(AddressBook o) {
        if (null == o.getUsercode()) {
            o.setUsercode(this.getNextValueOfSequence("S_ADDRESSID"));
        }
        super.saveObject(o);
    }

    @SuppressWarnings("unused")
    public List<AddressBook> getAddressBookByGroup(List<String> usercodes, String bodyname) {
        if (usercodes.size() == 0) {
            usercodes.add("''");
        }
        String hql;
        if (StringUtils.hasText(bodyname)) {
            hql = "from AddressBook a where a.usercode in (:codes) or a.bodyname like '%" + bodyname + "%'";
        } else {
            hql = "from AddressBook a where a.usercode in (:codes)";
        }
        return this.getHibernateTemplate().findByNamedParam(hql, "codes", usercodes);

    }

    public List<AddressBook> getAddressBookByUnit(List<String> unitcode, String bodyname) {
        if (unitcode.size() == 0) {
            unitcode.add("''");
        }
        String hql;
        if (StringUtils.hasText(bodyname)) {
            hql = "from AddressBook a where a.unitcode in (?) and a.bodyname like " +
                    HQLUtils.buildHqlStringForSQL(HQLUtils.getMatchString(bodyname));
        } else {
            hql = "from AddressBook a where a.unitcode in (?)";
        }
        return this.listObjects(hql, (Object) unitcode);

    }


    public List<AddressBook> listImportAddressBook(List<String> usercodes) {
        if (usercodes.size() == 0) {
            usercodes.add("''");
        }
        String hql = "from AddressBook a where a.usercode not in (?)";
        return listObjects(hql, (Object) usercodes);
    }
    
   /* public List<Staffcertificate> getStaffcertificateByUserCode(String usercode) {
        List<Staffcertificate> ls = getHibernateTemplate().find("FROM Staffcertificate where usercode=?",usercode);
        return ls;
    }
    
    public List<Supplierinfo> getSupplierinfoByUserCode(String usercode) {
        List<Supplierinfo> ls = getHibernateTemplate().find("FROM Supplierinfo where usercode=?",usercode);
        return ls;
    }*/
}
