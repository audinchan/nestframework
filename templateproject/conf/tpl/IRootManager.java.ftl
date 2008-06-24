package ${hss_service_package};
// Generated ${date} by Hibernate Tools ${version} with nest-tools

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.nestframework.commons.hibernate.IPage;

public interface IRootManager<#if hss_jdk5><T, K extends Serializable></#if> {
    /**
     * ���ݶ�̬Hql��ѯ��ҳ����.
     * 
     * @param dqQuery ��ѯ�õĶ�̬Hql����.
     * @param dqCount �����ѯ�������¼���Ķ�̬Hql����.
     * @param paras ��ѯ����.
     * @param pageNumber �ڼ�ҳ.
     * @param pageSize ÿҳ��ʾ��¼��.
     * @return
     */
	public IPage<T> findByDynamicQuery(final String dqQuery,
			final String dqCount, final Map<String, Object> paras,
			final int pageNumber, final int pageSize);
	
	/**
	 * ����������������ֵ���Ҷ����б�.
	 * 
	 * @param propertyName ������.
	 * @param propertyValue ����ֵ.
	 * @return �����б�.
	 */
	public List<T> findByProperty(String propertyName, Object propertyValue);
	
	/**
	 * ����������������ֵ���ҵ�������.
	 * 
	 * @param propertyName ������.
	 * @param propertyValue ����ֵ.
	 * @return ����.
	 */
	public T getByProperty(String propertyName, Object propertyValue);
}
