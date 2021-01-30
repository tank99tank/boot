package org.uushang.boot.auth.context.parser;

import org.uushang.boot.auth.constant.Param;

public interface ParserValue {
	Object getUserValue(Param paramParam, Object paramObject);

	Object getRoleValue(Param paramParam, Object paramObject);

	Object getDeptValue(Param paramParam, Object paramObject);

	Object getAuthValue(Param paramParam, Object paramObject);
}
