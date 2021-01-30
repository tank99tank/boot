package org.uushang.boot.auth.context.parser;

import org.uushang.boot.auth.constant.Param;
import org.uushang.boot.auth.exception.MissingSessionParameterException;

public interface GetValue {
	Object getValue(Param paramParam, Object paramObject, boolean paramBoolean) throws MissingSessionParameterException;
}
