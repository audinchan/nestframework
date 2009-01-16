package org.nestframework.commons.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IHibernateManager<T, K extends Serializable> {

	/**
	 * ���ݶ�̬Hql��ѯ��ҳ����.
	 * 
	 * @param dqQuery ��ѯ�õĶ�̬Hql����.
	 * @param dqCount �����ѯ�������¼���Ķ�̬Hql���ƣ����Ϊnull�����Զ�ʹ��dqQuery����֮ǰ��ӡ�select count(*) ��.
	 * @param paras ��ѯ�����������null������Ϊû�в�������.
	 * @param pageNumber �ڼ�ҳ.
	 * @param pageSize ÿҳ��ʾ��¼��.
	 * @return ��ҳ����.
	 */
	public IPage<T> findByDynamicQuery(final String dqQuery,
			final String dqCount, final Map<String, Object> paras,
			final int pageNumber, final int pageSize);

	/**
	 * ���ݶ�̬Hql��ѯ��ҳ����(��������).
	 * 
	 * @param dqQuery ��ѯ�õĶ�̬Hql����.
	 * @param dqCount �����ѯ�������¼���Ķ�̬Hql����.
	 * @param paraName ������.
	 * @param paraValue ����ֵ.
	 * @param pageNumber �ڼ�ҳ.
	 * @param pageSize ÿҳ��ʾ��¼��.
	 * @return ��ҳ����.
	 */
	public IPage<T> findByDynamicQuery(final String dqQuery,
			final String dqCount, String paraName, Object paraValue,
			final int pageNumber, final int pageSize);

	/**
	 * ���ݶ�̬Hql��ѯ��ҳ����(û�в���).
	 * 
	 * @param dqQuery ��ѯ�õĶ�̬Hql����.
	 * @param dqCount �����ѯ�������¼���Ķ�̬Hql����.
	 * @param pageNumber �ڼ�ҳ.
	 * @param pageSize ÿҳ��ʾ��¼��.
	 * @return ��ҳ����.
	 */
	public IPage<T> findByDynamicQuery(final String dqQuery,
			final String dqCount, final int pageNumber, final int pageSize);

	/**
	 * ���ݶ�̬Hql��ѯ��ҳ����.
	 * 
	 * @param dqQuery ��ѯ�õĶ�̬Hql����.
	 * @param dqCount �����ѯ�������¼���Ķ�̬Hql����.
	 * @param paraName ������.
	 * @param paraValue ����ֵ.
	 * @param pageNumber �ڼ�ҳ.
	 * @param pageSize ÿҳ��ʾ��¼��.
	 * @return ��ҳ����.
	 */
	public IPage<T> findByDynamicQuery(final String dqQuery,
			final String dqCount, String[] paraName, Object[] paraValue,
			final int pageNumber, final int pageSize);

	/**
	 * ���ݶ�̬Hql��ѯ�����б�.
	 * 
	 * @param dqQuery ��ѯ�õĶ�̬Hql����.
	 * @param paraName ������.
	 * @param paraValue ����ֵ.
	 * @return �����б�.
	 */
	public List<T> findListByDynamicQuery(final String dqQuery,
			String[] paraName, Object[] paraValue);

	/**
	 * ���ݶ�̬Hql��ѯ�����б�(��������).
	 * 
	 * @param dqQuery ��ѯ�õĶ�̬Hql����.
	 * @param paraName ������.
	 * @param paraValue ����ֵ.
	 * @return �����б�.
	 */
	public List<T> findListByDynamicQuery(final String dqQuery,
			String paraName, Object paraValue);

	/**
	 * ���ݶ�̬Hql��ѯ�����б�(û�в���).
	 * 
	 * @param dqQuery ��ѯ�õĶ�̬Hql����.
	 * @return �����б�.
	 */
	public List<T> findListByDynamicQuery(final String dqQuery);

	/**
	 * ���ݶ�̬Hql��ѯ�����б�.
	 * 
	 * @param dqQuery ��ѯ�õĶ�̬Hql����.
	 * @param paras ��ѯ����.
	 * @return �����б�.
	 */
	public List<T> findListByDynamicQuery(final String dqQuery,
			final Map<String, Object> paras);

	/**
	 * ���ݶ�̬Hql��ѯ�����б�.
	 * 
	 * @param dqQuery ��ѯ�õĶ�̬Hql����.
	 * @param paras ��ѯ����.
	 * @param firstResult �ӵڼ�����¼��ʼȡ����(��һ����¼�Ǵ�0��ʼ������).
	 * @param maxResults ���ȡ������¼.
	 * @return �����б�.
	 */
	public List<T> findListByDynamicQuery(final String dqQuery,
			final Map<String, Object> paras, final Integer firstResult,
			final Integer maxResults);

	/**
	 * ����������������ֵ���Ҷ����б�.
	 * 
	 * @param propertyName ������.
	 * @param propertyValue ����ֵ.
	 * @param maxResult ��෵�ض�������¼. С�ڵ���0���ʾ������.
	 * @return �����б�.
	 */
	public List<T> findByProperty(String propertyName,
			final Object propertyValue, final int maxResult);

	/**
	 * ����������������ֵ���Ҷ����б�.
	 * 
	 * @param propertyName ������.
	 * @param propertyValue ����ֵ.
	 * @return �����б�.
	 */
	public List<T> findByProperty(String propertyName,
			final Object propertyValue);

	/**
	 * ����������������ֵ���ҵ�������.
	 * 
	 * @param propertyName ������.
	 * @param propertyValue ����ֵ.
	 * @return ����.
	 */
	public T getByProperty(String propertyName, Object propertyValue);

	/**
	 * ����������������.
	 * 
	 * @param keyName ��������.
	 * @param keyValue ����ֵ.
	 * @param propertyName ��������.
	 * @param propertyValue ����ֵ.
	 */
	public void updateProperty(String keyName, K keyValue, String propertyName,
			Object propertyValue);

}