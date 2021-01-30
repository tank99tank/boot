package org.uushang.boot.auth.util;

import org.uushang.boot.auth.constant.Param;
import org.uushang.boot.auth.context.parser.ParserValue;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("incomplete-switch")
public class CustomsParser implements ParserValue {
	public Object getUserValue(Param name, Object info) {
		Object value = null;
		JSONObject ifs = (JSONObject) info;
		switch (name) {
		case USERID:
			value = ifs.get("userId");
			break;
		case USERNAME:
			value = ifs.get("userName");
			break;
		case FULLPATH:
			value = ifs.get("allPathName");
			break;
		}
		return value;
	}

	public Object getRoleValue(Param name, Object info) {
		throw new UnsupportedOperationException("Getting role information is not supported");
	}

	public Object getDeptValue(Param name, Object info) {
		Object value = null;
		JSONObject ifs = (JSONObject) info;
		switch (name) {
		case ORGNO:
			value = ifs.get("orgId");
			break;
		case ORGNAME:
			value = ifs.get("orgName");
			break;
		}
		return value;
	}

	public Object getAuthValue(Param name, Object info) {
		throw new UnsupportedOperationException("Getting permissions information is not supported");
	}
}
