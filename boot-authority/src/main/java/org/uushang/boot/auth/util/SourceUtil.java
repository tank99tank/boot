package org.uushang.boot.auth.util;

import org.uushang.boot.auth.context.parser.Information;
import org.uushang.boot.auth.context.parser.ParserValue;
import org.uushang.boot.auth.environment.Project;
import org.uushang.boot.auth.environment.RequestInformation;

public class SourceUtil {
	public static Information getInformation(Project source) {
		if (Project.OWINFO_4A == source)
			return (Information) new RequestInformation();
		if (Project.CUSTOMS_H4A == source)
			return (Information) new RequestInformation();
		throw new IllegalArgumentException("source does not match");
	}

	public static ParserValue getParser(Project source) {
		if (Project.OWINFO_4A == source)
			return new OwinfoParser();
		if (Project.CUSTOMS_H4A == source)
			return new CustomsParser();
		throw new IllegalArgumentException("source does not match");
	}
}
