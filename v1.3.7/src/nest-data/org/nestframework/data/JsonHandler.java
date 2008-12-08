package org.nestframework.data;

import java.io.PrintWriter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import org.nestframework.action.IActionHandler;
import org.nestframework.annotation.Intercept;
import org.nestframework.config.IConfiguration;
import org.nestframework.core.ExecuteContext;
import org.nestframework.core.IInitable;
import org.nestframework.core.Stage;
import org.nestframework.utils.NestUtil;

/**
 * @author wanghai
 *
 */
@Intercept(Stage.HANDLE_VIEW)
public class JsonHandler implements IInitable,IActionHandler
{
    private JsonConfig jsonConfig = new JsonConfig() ;
    
    public void init(IConfiguration config) {
        JsonPropertyFilter filter = new JsonPropertyFilter();
        String ignoreValues=NestUtil.trimAll(config.getProperties().get("json.ignoreValue"));
        if (NestUtil.isNotEmpty(ignoreValues)) {
            String[] vs = ignoreValues.split(",");
            for (String v : vs) {
                String[] value = v.split("=");
                filter.setIgnoreValue(value[0].trim(),value[1].trim());
            }
        }
        jsonConfig.setJsonPropertyFilter(filter);
    }
    
    public boolean process(ExecuteContext context) throws Exception
    {
        Json json = context.getAction().getAnnotation(Json.class);
        if (json != null)
        {
            String jsonstr="";
            Object obj= (Object) context.getForward();
            try
            {
                if (JSONUtils.isObject(obj))
                {
                    jsonstr=JSONObject.fromObject(obj,jsonConfig).toString(0);
                }else if(JSONUtils.isArray(obj)){
                    jsonstr = JSONArray.fromObject(obj,jsonConfig).toString(0);
                }
            }
            catch (RuntimeException e)
            {
                e.printStackTrace();
            }            
            context.getResponse().setContentType(json.value());
            PrintWriter writer = context.getResponse().getWriter();
            writer.write(jsonstr);
            writer.close();
            return true;
        }
        return false;
    }
}