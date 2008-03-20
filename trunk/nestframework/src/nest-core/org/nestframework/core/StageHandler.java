package org.nestframework.core;

import java.util.ArrayList;
import java.util.List;

import org.nestframework.action.IActionHandler;

public class StageHandler {
	
	private Stage stage;
	
	private boolean supportStop;
	
	private List<IActionHandler> handlers = new ArrayList<IActionHandler>();
	
	public StageHandler(Stage stage) {
		this.stage = stage;
	}
	
	public StageHandler(Stage stage, boolean canStop) {
		this.stage = stage;
		this.supportStop = canStop;
	}
	
	public Stage getStage() {
		return stage;
	}

	public List<IActionHandler> getHandlers() {
		return handlers;
	}
	
	public StageHandler setHandlers(List<IActionHandler> handlers) {
		this.handlers = handlers;
		return this;
	}
	
	public StageHandler addHandler(IActionHandler handler) {
//		handlers.add(0, handler);
		handlers.add(handler);
		return this;
	}
	
	public boolean isSupportStop() {
		return supportStop;
	}
}
