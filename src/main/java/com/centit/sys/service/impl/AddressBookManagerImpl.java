package com.centit.sys.service.impl;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.sys.dao.AddressBookDao;
import com.centit.sys.po.AddressBook;
import com.centit.sys.service.AddressBookManager;

import java.util.List;

public class AddressBookManagerImpl extends BaseEntityManagerImpl<AddressBook>
        implements AddressBookManager {
    private static final long serialVersionUID = 1L;
    private AddressBookDao addressBookDao;

    public void setAddressBookDao(AddressBookDao baseDao) {
        this.addressBookDao = baseDao;
        setBaseDao(this.addressBookDao);
    }

    public Long getNextAddressId() {
        String sKey = addressBookDao.getNextValueOfSequence("S_ADDRESSID");
        return Long.valueOf(sKey);
    }

    public List<AddressBook> getAddressBookByGroup(List<String> usercodes, String bodyname) {
        return addressBookDao.getAddressBookByGroup(usercodes, bodyname);
    }

    public List<AddressBook> getAddressBookByUnit(List<String> unitcode, String bodyname) {
        return addressBookDao.getAddressBookByUnit(unitcode, bodyname);
    }

    public List<AddressBook> listImportAddressBook(List<String> usercodes) {
        return addressBookDao.listImportAddressBook(usercodes);
    }
    /*public List<Staffcertificate> getStaffcertificateByUserCode(String usercode){
	    return addressBookDao.getStaffcertificateByUserCode(usercode);
	}
	public List<Supplierinfo> getSupplierinfoByUserCode(String usercode){
	    return addressBookDao.getSupplierinfoByUserCode(usercode);
	}*/
}

