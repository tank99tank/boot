package org.uushang.boot.auth.exception;

import org.springframework.web.bind.ServletRequestBindingException;

public class MissingSessionParameterException extends ServletRequestBindingException {

	private static final long serialVersionUID = -5978352785613240618L;

	private final String parameterName;

	private final String parameterType;

	public MissingSessionParameterException(String parameterName, String parameterType) {
		super("");
		this.parameterName = parameterName;
		this.parameterType = parameterType;
	}

	public String getMessage() {
		return "Required " + this.parameterType + " parameter '" + this.parameterName + "' is not present";
	}

	public final String getParameterName() {
		return this.parameterName;
	}

	public final String getParameterType() {
		return this.parameterType;
	}
}
