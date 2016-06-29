package com.centit.sys.service;

import java.util.List;

import com.centit.core.service.BaseEntityManager;
import com.centit.sys.po.AddressBook;

public interface AddressBookManager extends BaseEntityManager<AddressBook> {

    public Long getNextAddressId();

    public List<AddressBook> getAddressBookByGroup(List<String> usercodes, String bodyname);

    public List<AddressBook> getAddressBookByUnit(List<String> unitcode, String bodyname);

    public List<AddressBook> listImportAddressBook(List<String> usercodes);
    /*public List<Staffcertificate> getStaffcertificateByUserCode(String usercode);
	public List<Supplierinfo> getSupplierinfoByUserCode(String usercode);*/

}
