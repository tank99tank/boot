package org.uushang.boot.auth.util;

import org.uushang.boot.auth.constant.Param;
import org.uushang.boot.auth.context.parser.ParserValue;

import com.alibaba.fastjson.JSONObject;

public class OwinfoParser implements ParserValue {
	public Object getUserValue(Param name, Object info) {
		JSONObject ifs = (JSONObject) info;
		return ifs.get(name.getName());
	}

	public Object getRoleValue(Param name, Object info) {
		throw new UnsupportedOperationException("Getting role information is not supported");
	}

	public Object getDeptValue(Param name, Object info) {
		JSONObject ifs = (JSONObject) info;
		return ifs.get(name.getName());
	}

	public Object getAuthValue(Param name, Object info) {
		throw new UnsupportedOperationException("Getting permissions information is not supported");
	}
}
