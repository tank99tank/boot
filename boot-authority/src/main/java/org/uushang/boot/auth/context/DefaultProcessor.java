package org.uushang.boot.auth.context;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.uushang.boot.auth.annotation.Required;
import org.uushang.boot.auth.annotation.SessionParam;
import org.uushang.boot.auth.annotation.SessionParams;
import org.uushang.boot.auth.constant.Param;
import org.uushang.boot.auth.context.parser.GetValue;
import org.uushang.boot.auth.environment.Project;
import org.uushang.boot.auth.exception.MissingSessionParameterException;
import org.uushang.boot.auth.util.Values;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DefaultProcessor implements Processor {
	private String typeMap;

	private String typeString;

	private GetValue values;

	public DefaultProcessor() {
	}

	public DefaultProcessor(Project project) {
		this.typeMap = Map.class.getSimpleName();
		this.typeString = String.class.getSimpleName();
		this.values = (GetValue) new Values(project);
	}

	public Object[] singleParameter(Parameter parameter, Object[] args, Object info)
			throws MissingSessionParameterException, IllegalAccessException, NoSuchFieldException {
		String typeName = parameter.getType().getSimpleName();
		if (typeName.equals(this.typeString)) {
			SessionParam sessionParam = parameter.<SessionParam>getAnnotation(SessionParam.class);
			Assert.notNull(sessionParam, "Unable to process because @SessionParam was not found");
			args[0] = this.values.getValue(sessionParam.value(), info, sessionParam.required());
			return args;
		}
		SessionParams sessionParams = parameter.<SessionParams>getAnnotation(SessionParams.class);
		if (!verify(sessionParams))
			throw new IllegalArgumentException("Unable to process because @SessionParams was not found");
		if (typeName.equals(this.typeMap)) {
			Map<String, Object> params = (Map<String, Object>) args[0];
			for (Param param : sessionParams.values()) {
				Object value = this.values.getValue(param, info, true);
				params.put(param.getName(), value);
			}
			return args;
		}
		beanProcess(args[0], sessionParams, info);
		return args;
	}

	public Object[] multipleParameter(Parameter[] parameters, Object[] args, Object info)
			throws MissingSessionParameterException, IllegalAccessException {
		for (int i = 0; i < parameters.length; i++) {
			SessionParam sessionParam = parameters[i].<SessionParam>getAnnotation(SessionParam.class);
			SessionParams sessionParams = parameters[i].<SessionParams>getAnnotation(SessionParams.class);
			if (Objects.nonNull(sessionParam) && Objects.nonNull(sessionParams))
				throw new IllegalArgumentException(
						"SessionParam and SessionParams cannot be used on the same parameter");
			if (Objects.nonNull(sessionParam)) {
				Param param = sessionParam.value();
				Object value = this.values.getValue(param, info, sessionParam.required());
				args[i] = value;
			} else if (Objects.nonNull(sessionParams)) {
				beanProcess(args[i], sessionParams, info);
			}
		}
		return args;
	}

	private void beanProcess(Object obj, SessionParams sessionParams, Object info)
			throws IllegalAccessException, MissingSessionParameterException {
		Class<?> clazz = obj.getClass();
		Field[] declaredFields = clazz.getDeclaredFields();
		if (sessionParams.different()) {
			List<Map<SessionParam, Field>> params = new ArrayList<>(declaredFields.length / 2);
			for (Field declaredField : declaredFields) {
				if (declaredField.isAnnotationPresent((Class) SessionParam.class)) {
					SessionParam sessionParam = declaredField.<SessionParam>getAnnotation(SessionParam.class);
					Map<SessionParam, Field> fields = new HashMap<>(1);
					fields.put(sessionParam, declaredField);
					params.add(fields);
					if (!declaredField.isAccessible())
						declaredField.setAccessible(true);
				}
			}
			Assert.notEmpty(params,
					"No eligible fields found, When the difference is true, you need to specify SessionParam on the field");
			for (Map<SessionParam, Field> fields : params) {
				for (Map.Entry<SessionParam, Field> entry : fields.entrySet()) {
					SessionParam sessionParam = entry.getKey();
					Field field = entry.getValue();
					Object value = this.values.getValue(sessionParam.value(), info, sessionParam.required());
					field.set(obj, value);
				}
			}
		} else {
			boolean expression = ((sessionParams.values()).length > declaredFields.length);
			if (expression)
				throw new IllegalArgumentException(
						"The number of fields to be injected is greater than the number of fields in the entity");
			for (Param param : sessionParams.values()) {
				Field field;
				String fieldName = param.getName();
				try {
					field = clazz.getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					throw new IllegalArgumentException(
							"This field does not exist in the object that need to inject data, fieldName -> "
									+ fieldName + ", entity -> " + clazz.toString());
				}
				Required required = field.<Required>getAnnotation(Required.class);
				Object value = Objects.isNull(required) ? this.values.getValue(param, info, true)
						: this.values.getValue(param, info, required.value());
				if (!field.isAccessible())
					field.setAccessible(true);
				field.set(obj, value);
			}
		}
	}

	private boolean verify(SessionParams sessionParams) {
		if (Objects.nonNull(sessionParams)) {
			boolean expression = (ObjectUtils.isEmpty((Object[]) sessionParams.values()) && !sessionParams.different());
			if (expression)
				throw new IllegalArgumentException("Unable to process because value is empty and different is false");
			return true;
		}
		return false;
	}
}
