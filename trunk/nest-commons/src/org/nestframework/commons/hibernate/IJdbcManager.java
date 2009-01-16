package org.nestframework.commons.hibernate;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public interface IJdbcManager {

	/**
	 * ����̬sql.
	 * 
	 * @param params
	 *            ����.
	 * @param name
	 *            ��̬Sql������.
	 * @return
	 * @throws Exception
	 */
	public ISqlElement processSql(Map<String, Object> params, String name)
			throws Exception;

	/**
	 * ���ݶ�̬Sql���ơ��������ҷ�ҳ����.
	 * 
	 * @param <E>
	 *            ��ҳ�����е�Ԫ�ص�����.
	 * @param querySqlName
	 *            ��ѯSql������.
	 * @param countSqlName
	 *            ͳ������Sql������.
	 * @param params
	 *            ����.
	 * @param pageNo
	 *            �ڼ�ҳ.
	 * @param pageSize
	 *            ÿҳ��ʾ����������.
	 * @param rh
	 *            �д���ӿ�.
	 * @return ��ҳ����.
	 * @throws Exception
	 *             �쳣.
	 */
	public <E> IPage<E> findPage(String querySqlName, String countSqlName,
			Map<String, Object> params, final int pageNo, final int pageSize,
			final IRowHandler<E> rh) throws Exception;

	/**
	 * ���ݶ�̬Sql���ơ��������ҷ�ҳ����.
	 * 
	 * @param <E>
	 *            ��ҳ�����е�Ԫ�ص�����.
	 * @param querySqlName
	 *            ��ѯSql������.
	 * @param countSqlName
	 *            ͳ������Sql������.
	 * @param pageNo
	 *            �ڼ�ҳ.
	 * @param pageSize
	 *            ÿҳ��ʾ����������.
	 * @param rh
	 *            �д���ӿ�.
	 * @param paramName
	 *            ������
	 * @param paramValue
	 *            ����ֵ
	 * @return ��ҳ����.
	 * @throws Exception
	 *             �쳣.
	 */
	public <E> IPage<E> findPage(String querySqlName, String countSqlName,
			final int pageNo, final int pageSize, final IRowHandler<E> rh,
			String[] paramName, Object... paramValue) throws Exception;

	/**
	 * ���ݶ�̬Sql���ơ��������ҷ�ҳ����(��������).
	 * 
	 * @param <E>
	 *            ��ҳ�����е�Ԫ�ص�����.
	 * @param querySqlName
	 *            ��ѯSql������.
	 * @param countSqlName
	 *            ͳ������Sql������.
	 * @param pageNo
	 *            �ڼ�ҳ.
	 * @param pageSize
	 *            ÿҳ��ʾ����������.
	 * @param rh
	 *            �д���ӿ�.
	 * @param paramName
	 *            ������.
	 * @param paramValue
	 *            ����ֵ.
	 * @return ��ҳ����.
	 * @throws Exception
	 *             �쳣.
	 */
	public <E> IPage<E> findPage(String querySqlName, String countSqlName,
			final int pageNo, final int pageSize, final IRowHandler<E> rh,
			String paramName, Object paramValue) throws Exception;

	/**
	 * ���ݶ�̬Sql���ơ��������ҷ�ҳ����(û�в���).
	 * 
	 * @param <E>
	 *            ��ҳ�����е�Ԫ�ص�����.
	 * @param querySqlName
	 *            ��ѯSql������.
	 * @param countSqlName
	 *            ͳ������Sql������.
	 * @param pageNo
	 *            �ڼ�ҳ.
	 * @param pageSize
	 *            ÿҳ��ʾ����������.
	 * @param rh
	 *            �д���ӿ�.
	 * @return ��ҳ����.
	 * @throws Exception
	 *             �쳣.
	 */
	public <E> IPage<E> findPage(String querySqlName, String countSqlName,
			final int pageNo, final int pageSize, final IRowHandler<E> rh)
			throws Exception;

	/**
	 * ���ݶ�̬Sql���ơ�������ѯ�����б�.
	 * 
	 * @param <E>
	 *            ��ҳ�����е�Ԫ�ص�����.
	 * @param sqlName
	 *            ��ѯSql������.
	 * @param params
	 *            ����.
	 * @param rm
	 *            ������ӳ�䴦����.
	 * @return �����б�.
	 * @throws Exception
	 *             �쳣.
	 */
	public <E> List<E> findList(String sqlName, Map<String, Object> params,
			RowMapper rm) throws Exception;

	/**
	 * ���ݶ�̬Sql���ơ�������ѯ�����б�.
	 * 
	 * @param <E>
	 *            ��ҳ�����е�Ԫ�ص�����.
	 * @param sqlName
	 *            ��ѯSql������.
	 * @param rm
	 *            ������ӳ�䴦����.
	 * @param paramName
	 *            ������.
	 * @param paramValue
	 *            ����ֵ.
	 * @return �����б�.
	 * @throws Exception
	 *             �쳣.
	 */
	public <E> List<E> findList(String sqlName, RowMapper rm,
			String[] paramName, Object... paramValue) throws Exception;

	/**
	 * ���ݶ�̬Sql���ơ�������ѯ�����б�(��������).
	 * 
	 * @param <E>
	 *            ��ҳ�����е�Ԫ�ص�����.
	 * @param sqlName
	 *            ��ѯSql������.
	 * @param rm
	 *            ������ӳ�䴦����.
	 * @param paramName
	 *            ������.
	 * @param paramValue
	 *            ����ֵ.
	 * @return �����б�.
	 * @throws Exception
	 *             �쳣.
	 */
	public <E> List<E> findList(String sqlName, RowMapper rm, String paramName,
			Object paramValue) throws Exception;

	/**
	 * ���ݶ�̬Sql���ơ�������ѯ�����б�(û�в���).
	 * 
	 * @param <E>
	 *            ��ҳ�����е�Ԫ�ص�����.
	 * @param sqlName
	 *            ��ѯSql������.
	 * @param rm
	 *            ������ӳ�䴦����.
	 * @return �����б�.
	 * @throws Exception
	 *             �쳣.
	 */
	public <E> List<E> findList(String sqlName, RowMapper rm) throws Exception;

}