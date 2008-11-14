package org.nestframework.localization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ActionMessages {

	public static final String GLOBAL_MESSAGE = "org.nestframework.validation.ActionMessages.global_message";
	
	private Map<String, List<ActionMessage>> msgMap = new HashMap<String, List<ActionMessage>>();
	
	public ActionMessages() {
		
	}
	
	/**
	 * Add global message from string.
	 * @param msg
	 * @return
	 */
	public ActionMessages add(String msg) {
		return add(GLOBAL_MESSAGE, msg);
	}
	
	/**
	 * Add global message from string, and indicate whether to use as resource key.
	 * @param msg
	 * @param resource
	 * @return
	 */
	public ActionMessages add(String msg, boolean resource) {
		return add(GLOBAL_MESSAGE, msg, resource);
	}
	
	/**
	 * Add global messsage with replace values.
	 * @param msg
	 * @param values
	 * @return
	 */
	public ActionMessages add(String msg, Object[] values) {
		return add(GLOBAL_MESSAGE, msg, values);
	}
	
	/**
	 * Add property message. 
	 * @param property
	 * @param msg
	 * @return
	 */
	public ActionMessages add(String property, String msg) {
		return add(property, msg, true);
	}
	
	/**
	 * Add property message with replace values.
	 * @param property
	 * @param msg
	 * @param values
	 * @return
	 */
	public ActionMessages add(String property, String msg, Object... values) {
		return add(property, msg, true, values);
	}
	
	/**
	 * Add property message, and indicate whether to use as resoruce key.
	 * @param property
	 * @param msg
	 * @param resource
	 * @return
	 */
	public ActionMessages add(String property, String msg, boolean resource) {
		return add(property, msg, resource, new Object[] {});
	}
	
	/**
	 * Add property message, indicate whether to use as resource key, with replace values.
	 * @param property
	 * @param msg
	 * @param resource
	 * @param values
	 * @return
	 */
	public ActionMessages add(String property, String msg, boolean resource, Object... values) {
		return add(property, new ActionMessage(msg, resource, values));
	}
	
	/**
	 * Add global message.
	 * @param msg ActionMessage.
	 * @return
	 */
	public ActionMessages add(ActionMessage msg) {
		return add(GLOBAL_MESSAGE, msg);
	}
	
	/**
	 * Add a message bound to key.
	 * @param key
	 * @param aMsg
	 * @return
	 */
	public ActionMessages add(String property, ActionMessage msg) {
		List<ActionMessage> msgList = msgMap.get(property);
		if (msgList == null) {
			msgList = new ArrayList<ActionMessage>();
			msgMap.put(property, msgList);
		}
		msgList.add(msg);
		return this;
	}
	
	public ActionMessages remove(String property) {
		msgMap.remove(property);
		return this;
	}
	
	public int size() {
		int size = 0;
		for (List<ActionMessage> list: msgMap.values()) {
			if (list != null) {
				size += list.size();
			}
		}
		return size;
	}
	
	public Iterator<ActionMessage> get() {
		List<ActionMessage> msgList = new ArrayList<ActionMessage>();
		for (List<ActionMessage> list: msgMap.values()) {
			if (list != null) {
				msgList.addAll(list);
			}
		}
		return msgList.iterator();
	}
	
	public Iterator<ActionMessage> get(String property) {
		List<ActionMessage> list = msgMap.get(property);
		if (list != null) {
			return list.iterator();
		} else {
			return Collections.<ActionMessage>emptyList().iterator();
		}
	}
	
}
