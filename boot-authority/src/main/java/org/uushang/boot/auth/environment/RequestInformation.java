package org.uushang.boot.auth.environment;

import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

import org.uushang.boot.auth.context.parser.Information;

public class RequestInformation implements Information {
	public Object getInformation(Object source) throws UnsupportedEncodingException {
		HttpServletRequest request = (HttpServletRequest) source;
		String info = request.getHeader("UserInformation");
		Objects.requireNonNull(info, "Unable to get identity information from request");
		info = URLDecoder.decode(info, "UTF-8");
		return JSONObject.parseObject(info);
	}
}
