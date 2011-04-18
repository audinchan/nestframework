package ${hss_base_package}.webapp.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import ${hss_base_package}.model.SysDict;
import ${hss_base_package}.service.ISysDictManager;

/**
 * 初始化字典值
 * @author 
 */
public class InitDictionUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(InitDictionUtil.class);

	//字典
	private List<SysDict> sysDictes = new ArrayList<SysDict>();
	/**
	 * 字典缓存
	 */
	private Map<String,SysDict> dictMap = new ConcurrentHashMap<String,SysDict>(); 	
	
	//服务器启动的时候取出所有字典值放在缓存中
	public void findAllSysDict()
	{
		if (sysDictes == null || sysDictes.size() == 0)
		{
			sysDictes = sysDictManager.findAll();
			dictMap = new ConcurrentHashMap<String,SysDict>(); 
			for(SysDict sysDict:sysDictes)
			{
				dictMap.put(sysDict.getDictId().toString(), sysDict);
			}
		}
	}

	public String getDictNameByDictId(String dictId)
	{
		String ret = "";
		try
		{
			if (dictMap != null)
			{
				if(null != dictMap.get(dictId))
				{
					ret = dictMap.get(dictId).getDictName();
				}
			}
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return ret;
	}

	//根据字典类型从缓存中取字典值.
	public List<SysDict> getDictByDictType(Integer dictType)
	{
		List<SysDict> ret = new ArrayList<SysDict>();
		try
		{
			if (sysDictes != null && sysDictes.size() > 0)
			{
				for (SysDict diction : sysDictes)
				{
					if (diction.getDictType().compareTo(dictType) == 0)
					{
						ret.add(diction);
					}
				}
			}
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage() + ex.fillInStackTrace());
		}
		Collections.sort(ret, new DictionComparator());
		return ret;
	}
	public String getDictIdByDictTypeAndDictCode(Integer dictType, String dictCode)
	{
		String dictId = null;
		if (sysDictes != null && sysDictes.size() > 0)
		{
			for (SysDict diction : sysDictes)
			{
				if (diction.getDictType().compareTo(dictType) == 0)
				{
					if (diction.getDictCode().equals(dictCode))
					{
						dictId =  diction.getDictId().toString();
						break;
					}
				}
			}
		}
		return dictId;
	}

	private ISysDictManager sysDictManager;

	public void setSysDictManager(ISysDictManager sysDictManager) {
		this.sysDictManager = sysDictManager;
	}
	public List<SysDict> getSysDictes() {
		return sysDictes;
	}
	public Map<String, SysDict> getDictMap() {
		return dictMap;
	}
	
	class DictionComparator implements Comparator<SysDict>{

		public int compare(SysDict o1, SysDict o2) {
			
			if (new Integer(o1.getDictId()).compareTo(new Integer(o2.getDictId())) > 0)
			{
				return 1;
			}
			if (new Integer(o1.getDictId()).compareTo(new Integer(o2.getDictId())) < 0)
			{
				return -1;
			}
			return 0;
		}
	}
}
