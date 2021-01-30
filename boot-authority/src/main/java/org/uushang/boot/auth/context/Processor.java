package org.uushang.boot.auth.context;

import java.lang.reflect.Parameter;

public interface Processor {
	Object[] singleParameter(Parameter paramParameter, Object[] paramArrayOfObject, Object paramObject)
			throws Exception;

	Object[] multipleParameter(Parameter[] paramArrayOfParameter, Object[] paramArrayOfObject, Object paramObject)
			throws Exception;
}
