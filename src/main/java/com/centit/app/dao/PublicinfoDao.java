package com.centit.app.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.transform.Transformers;

import com.centit.app.po.Publicinfo;
import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;

public class PublicinfoDao extends BaseDaoImpl<Publicinfo> {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(PublicinfoDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("infocode", CodeBook.EQUAL_HQL_ID);

            filterField.put("parentinfocode", CodeBook.EQUAL_HQL_ID);

            filterField.put("filecode", CodeBook.EQUAL_HQL_ID);

            filterField.put("filename", CodeBook.LIKE_HQL_ID);

            filterField.put("fileextension", CodeBook.LIKE_HQL_ID);

            filterField.put("ownercode", CodeBook.EQUAL_HQL_ID);

            filterField.put("readcount", CodeBook.LIKE_HQL_ID);

            filterField.put("downloadcount", CodeBook.LIKE_HQL_ID);

            filterField.put("md5", CodeBook.EQUAL_HQL_ID);

            filterField.put("uploadtime", CodeBook.LIKE_HQL_ID);

            filterField.put("modifytimes", CodeBook.LIKE_HQL_ID);

            filterField.put("status", CodeBook.EQUAL_HQL_ID);

            filterField.put("type", CodeBook.EQUAL_HQL_ID);

            filterField.put("isfolder", CodeBook.EQUAL_HQL_ID);

            filterField.put("filedescription", CodeBook.LIKE_HQL_ID);

            filterField.put("unitcode", CodeBook.EQUAL_HQL_ID);
        }
        return filterField;
    }

    /**
     * 列出机构的直接父级机构
     *
     * @param unitcode
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> listUnitDirectParents(String unitcode) {
        String sql = getSession().getNamedQuery("LIST_UNIT_DIRECT_PARENTS").getQueryString();

        return ((List<String>) getSession().createSQLQuery(sql)
                .addScalar("UNITCODE", Hibernate.STRING)
                .setString("unitcode", unitcode)
                .list());
    }

    /**
     * 列出文件路径
     *
     * @param infocode
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, String>> listFolderParents(String infocode) {
        return (List<Map<String, String>>) getSession().getNamedQuery("LIST_PATH_PARENTS")
                .setString("infocode", infocode)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    /**
     * 列出指定公共文件夹目录下所有文件/文件夹
     *
     * @param infocode
     * @param userUnitcode
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Publicinfo> listAllPublicinfos(String infocode) {
        String sql = getSession().getNamedQuery("LIST_ALL_PUBLICFILES").getQueryString();

        return getSession().createSQLQuery(sql)
                .addEntity("p", Publicinfo.class)
                .setString("infocode", infocode)
                .list();

    }

    /**
     * 根据名称获取文件夹对象
     *
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    public Publicinfo getPublicFolderByName(String name, String infocode) {
        String sql = getSession().getNamedQuery("GET_FOLDER_BY_NAME").getQueryString();

        List<Publicinfo> list = getSession().createSQLQuery(sql)
                .addEntity("p", Publicinfo.class)
                .setString("name", name)
                .setString("infocode", infocode)
                .list();

        if (list.size() == 0) {
            return null;
        }

        return list.get(0);
    }

    /**
     * 根据名称获取文件对象
     *
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    public Publicinfo getPublicFileByName(String name, String ext, String infocode) {
        String sql = getSession().getNamedQuery("GET_FILE_BY_NAME").getQueryString();

        List<Publicinfo> list = getSession().createSQLQuery(sql)
                .addEntity("p", Publicinfo.class)
                .setString("name", name)
                .setString("ext", ext)
                .setString("infocode", infocode)
                .list();

        if (list.size() == 0) {
            return null;
        }

        return list.get(0);
    }

    /**
     * 根据名称获取文件对象
     *
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    public Publicinfo getPublicFileByName(String name, String infocode) {
        String sql = getSession().getNamedQuery("GET_FILE_BY_NAME_WITHOUT_EXT").getQueryString();

        List<Publicinfo> list = getSession().createSQLQuery(sql)
                .addEntity("p", Publicinfo.class)
                .setString("name", name)
                .setString("infocode", infocode)
                .list();

        if (list.size() == 0) {
            return null;
        }

        return list.get(0);
    }

    /**
     * 批量列出公共信息文件
     *
     * @param infocodes
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Publicinfo> listPublicinfos(List<String> codes) {
        String hql = getSession().getNamedQuery("LIST_PUBLICFILES_BY_IDS").getQueryString();

        return getHibernateTemplate().findByNamedParam(hql, "codes", codes);
    }

    /**
     * 查询单位公共文件夹的根目录
     *
     * @param unitcode
     * @return
     */
    public Publicinfo getUnitRootDirectory(String unitcode) {
        String sql = getSession().getNamedQuery("GET_UNIT_ROOT_DIRECTORY").getQueryString();

        return (Publicinfo) getSession().createSQLQuery(sql)
                .addEntity("p", Publicinfo.class)
                .setString("unitcode", unitcode)
                .uniqueResult();
    }
}
