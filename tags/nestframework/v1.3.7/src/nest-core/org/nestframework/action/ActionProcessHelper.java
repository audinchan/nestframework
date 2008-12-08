package org.nestframework.action;

import java.util.List;
import java.util.Locale;

import org.nestframework.config.IConfiguration;
import org.nestframework.core.Constant;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.Stage;
import org.nestframework.core.StageHandler;
import org.nestframework.localization.LocalizationUtil;
import org.nestframework.utils.NestUtil;


public class ActionProcessHelper implements Constant {
	public static void process(ExecuteContext context, IConfiguration config) throws Exception {
		try {
			
			// locale
			Locale currentLocale = null;
			// If user want't to set locale
			String localeParamName = config.getProperties().get("localeParamName");
			if (NestUtil.isEmpty(localeParamName)) {
				localeParamName = "locale";
			}
			String localeString = context.getRequest().getParameter(localeParamName);
			if (!NestUtil.isEmpty(localeString)) {
                currentLocale = getLocale(localeString);
				context.getRequest().getSession().setAttribute(LOCALE_KEY, currentLocale);
			} else {
				// First, check if we have set locale in session.
				currentLocale = (Locale) context.getRequest().getSession().getAttribute(LOCALE_KEY);
			}
			
			if (currentLocale == null) { // If not, then detect by header.
				String acceptLanguage = context.getRequest().getHeader("Accept-Language");
				Locale[] locales = LocalizationUtil.detectLocale(acceptLanguage);
				for (Locale locale : locales) {
					// If we have resource file relate to this locale?
					if (LocalizationUtil.isLocaleResourceExists(locale)) {
						currentLocale = locale;
						break;
					}
				}
				// If not find any match, use default.
				if (currentLocale == null) {
					String defaultLocale = config.getProperties().get("defaultLocale");
					// If not a default locale specified in configuration, then use system default locale.
					if (NestUtil.isEmpty(defaultLocale)) {
						currentLocale = Locale.getDefault();
					} else {
						currentLocale = getLocale(defaultLocale);
					}
				}
				
				context.getRequest().getSession().setAttribute(LOCALE_KEY, currentLocale);
			}
			context.setLocale(currentLocale);
			
			for (Stage stage: Stage.values()) {
				context.setStage(stage);
				// If next stage is not null, we should jump to that stage directly.
				// But if next stage is equal to current one, then reset to null, to
				// handle the left stage.
				if (context.getNextStage() != null) {
					if (context.getNextStage() != stage) {
						continue;
					} else {
						context.setNextStage(null);
					}
				}
				
				StageHandler s = config.getStageHandler(stage);
				try {
					processHandlers(context, s.getHandlers(), s.isSupportStop());
				} catch (Exception e) {
					// handle stage exception
					if (!context.handleException(e)) {
						throw e;
					}
				}
			}

		} catch (Exception e) {
			// handle process exception
			if (!context.handleException(e)) {
				throw e;
			}
		}
	}
	
	private static void processHandlers(ExecuteContext context, List<IActionHandler> handlers, boolean supportBreak) throws Exception {
		for (IActionHandler handler: handlers) {
			boolean ret = handler.process(context);
			if (supportBreak && ret) {
				break;
			}
		}
	}
    
    private static Locale getLocale(String localeString) {
        if (NestUtil.isEmpty(localeString)) {
            return null;
        } else {
            String[] ls = localeString.split("_");
            if (ls.length == 2) {
                return new Locale(ls[0], ls[1]);
            } else if (ls.length == 3) {
                return new Locale(ls[0], ls[1], ls[3]);
            } else {
                return new Locale(localeString);
            }
        }
    }
}
