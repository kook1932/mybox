package com.mybox.common.aspect;

import com.mybox.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Aspect
@Component
public class ApiAspect {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private final String LOG_FORMAT = "[uri : %s] %s 시간 : %s";

	@Around("@annotation(io.swagger.annotations.ApiOperation)")
	public Object loggingApi(ProceedingJoinPoint proceedingJoinPoint) {
		Object proceed = null;
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			String uri = request.getRequestURI();

			log.info(String.format(LOG_FORMAT, uri, "요청", formatter.format(LocalDateTime.now())));

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			proceed = proceedingJoinPoint.proceed();
			log.info(String.format(LOG_FORMAT, uri, "응답", formatter.format(LocalDateTime.now())));
			stopWatch.stop();

			long totalTimeMillis = stopWatch.getTotalTimeMillis();
			log.info(String.format(LOG_FORMAT, uri, "소요", totalTimeMillis) + "ms");

		} catch (BaseException baseException) {
			log.error("[ApiAspect.loggingApi] BaseException error message : {}", baseException.getMessage());
			throw baseException;

		} catch (Throwable throwable) {
			log.error("[ApiAspect.loggingApi] error message : {}", throwable.getMessage());
		}

		return proceed;
	}

}
