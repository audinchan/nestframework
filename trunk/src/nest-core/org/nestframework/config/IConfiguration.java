package org.nestframework.config;

import java.util.List;
import java.util.Map;

import org.nestframework.action.IActionHandler;
import org.nestframework.action.IExceptionHandler;
import org.nestframework.core.IExternalContext;
import org.nestframework.core.Stage;
import org.nestframework.core.StageHandler;


public interface IConfiguration {
	/**
	 * 添加生命周期处理器。
	 * @param stage 时期。
	 * @param handler 处理器。
	 * @return
	 */
	public IConfiguration addLifecycleHandler(Stage stage, IActionHandler handler);
	public IConfiguration addLifecycleHandler(IActionHandler handler);
	public StageHandler getStageHandler(Stage stage);
	public IConfiguration addExceptionHandler(IExceptionHandler handler);
	public List<IExceptionHandler> getExceptionHandlers();
	public IConfiguration setPackageBase(String packageBase);
	public String getPackageBase();
	
	public Map<String, String> getProperties();
	
	public IConfiguration setExternalContext(IExternalContext context);
	public IExternalContext getExternalContext();
	
	public void init();
}
