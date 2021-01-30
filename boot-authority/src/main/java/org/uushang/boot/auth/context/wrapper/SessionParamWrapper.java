package org.uushang.boot.auth.context.wrapper;

import java.lang.reflect.Parameter;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.uushang.boot.auth.context.DefaultProcessor;
import org.uushang.boot.auth.context.Processor;
import org.uushang.boot.auth.context.parser.Information;
import org.uushang.boot.auth.environment.InformationSource;
import org.uushang.boot.auth.environment.JUnitInformation;
import org.uushang.boot.auth.environment.Project;
import org.uushang.boot.auth.util.SourceUtil;

@Aspect
@Component
public class SessionParamWrapper {
	private static final Logger log = LoggerFactory.getLogger(SessionParamWrapper.class);

	private Processor processor;

	private Information information;

	@Autowired
	private Environment environment;

	@Autowired
	private InformationSource source;

	@PostConstruct
	public void construct() throws Exception {
		this.processor = (Processor) new DefaultProcessor(this.source.getProject());
		String[] profiles = this.environment.getActiveProfiles();
		Assert.notEmpty((Object[]) profiles, "Unable to get profile from current environment");
		String profile = profiles[0];
		if ("dev".equals(profile)) {
			String property = this.environment.getProperty("authority.information");
			Boolean res = Boolean
					.valueOf(this.environment.getProperty("authority.dev-source.customs-h4a.enable", "false"));
			if (res.booleanValue()) {
				this.information = (Information) new JUnitInformation(Project.CUSTOMS_H4A, property);
			} else {
				this.information = (Information) new JUnitInformation(Project.OWINFO_4A, property);
			}
			log.warn("SessionParam initializing, Test information will be loaded, detail:{}",
					this.information.getInformation(null));
		} else {
			this.information = SourceUtil.getInformation(this.source.getProject());
		}
	}

	@Pointcut("@within(org.uushang.boot.auth.annotation.SessionParamAutowired) || @annotation(org.uushang.boot.auth.annotation.SessionParamAutowired)")
	public void annotationWrapper() {
	}

	@Around("annotationWrapper()")
	public Object annotationHandler(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		Objects.requireNonNull(request, "Cannot retrieve the current request from the context");
		Parameter[] parameters = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameters();
		Object[] args = joinPoint.getArgs();
		Assert.notEmpty((Object[]) parameters, "The parameter list of the method is empty");
		Object info = this.information.getInformation(request);
		if (parameters.length == 1) {
			Parameter parameter = parameters[0];
			this.processor.singleParameter(parameter, args, info);
		} else {
			this.processor.multipleParameter(parameters, args, info);
		}
		return joinPoint.proceed(args);
	}
}
