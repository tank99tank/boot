package org.uushang.boot.auth.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uushang.boot.auth.constant.Param;
import org.uushang.boot.auth.context.parser.GetValue;
import org.uushang.boot.auth.context.parser.ParserValue;
import org.uushang.boot.auth.environment.Project;
import org.uushang.boot.auth.exception.MissingSessionParameterException;

public class Values implements GetValue {
	private static final Logger log = LoggerFactory.getLogger(Values.class);

	private ParserValue parser;

	public Values() {
	}

	public Values(Project project) {
		this.parser = SourceUtil.getParser(project);
	}

	public Object getValue(Param param, Object info, boolean required) throws MissingSessionParameterException {
		Object value = null;
		switch (param.type()) {
		case 1:
			value = this.parser.getUserValue(param, info);
			break;
		case 2:
			value = this.parser.getRoleValue(param, info);
			break;
		case 3:
			value = this.parser.getDeptValue(param, info);
			break;
		case 4:
			value = this.parser.getAuthValue(param, info);
			break;
		}
		if (value == null && required) {
			log.error("Unable to get the specified value because value is null, Param:{}", param.toString());
			throw new MissingSessionParameterException(param.getName(), "String");
		}
		return value;
	}
}
