package org.nestframework.config;

import java.util.List;
import java.util.Map;

import org.nestframework.action.IActionHandler;
import org.nestframework.action.IExceptionHandler;
import org.nestframework.core.IExternalContext;
import org.nestframework.core.IMultipartHandler;
import org.nestframework.core.Stage;
import org.nestframework.core.StageHandler;

/**
 * Nest configuration interface.
 * 
 * @author audin
 *
 */
public interface IConfiguration {
	
	/**
	 * Add an action handler to the life cycle of action process.
	 * 
	 * @param handler Action handler.
	 * @return self.
	 */
	public IConfiguration addLifecycleHandler(IActionHandler handler);
	
	/**
	 * Get stage handler of a stage.
	 * 
	 * @param stage Stage.
	 * @return Stage handler.
	 */
	public StageHandler getStageHandler(Stage stage);
	
	/**
	 * Add an exception handler.
	 * 
	 * @param handler Exception handler.
	 * @return self.
	 */
	public IConfiguration addExceptionHandler(IExceptionHandler handler);
	
	/**
	 * Get all exception handlers.
	 * 
	 * @return List of IExceptionHandler.
	 */
	public List<IExceptionHandler> getExceptionHandlers();
	
	/**
	 * Set the package base path.
	 * 
	 * @param packageBase Package base path.
	 * @return self.
	 */
	public IConfiguration setPackageBase(String packageBase);
	
	/**
	 * Get package base path.
	 * @return package base path.
	 */
	public String getPackageBase();
	
	/**
	 * Get configurations as properties.
	 * 
	 * @return
	 */
	public Map<String, String> getProperties();
	
	/**
	 * Set external context.
	 * @param context External context.
	 * @return
	 */
	public IConfiguration setExternalContext(IExternalContext context);
	
	/**
	 * Get external context.
	 * @return
	 */
	public IExternalContext getExternalContext();
	
	/**
	 * Get multipartHandler.
	 * @return
	 */
	public IMultipartHandler getMultipartHandler();
	
	/**
	 * Initial configuration.
	 */
	public void init();
}
