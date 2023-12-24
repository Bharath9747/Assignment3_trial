package com.accolite.app.aop;

import com.accolite.app.aop.entity.LogEntity;
import com.accolite.app.handler.ExceptionHandler;
import com.accolite.app.repository.LogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ResponseStatusException;


@Aspect
@Configuration
public class AspectConfig {

    @Autowired
    private LogRepository logRepository;
    private Logger log=LoggerFactory.getLogger(AspectConfig.class);

    @Before(value = "execution(* com.accolite.app.controller.*.*(..))")
    public void logStatementBefore(JoinPoint joinPoint) {

        LogEntity entity = new LogEntity().builder()
                .message("Executing "+joinPoint)
                .build();
        logRepository.save(entity);
    }

    @After(value = "execution(* com.accolite.app.controller.*.*(..))")
    public void logStatementAfter(JoinPoint joinPoint) {
        LogEntity entity = new LogEntity().builder()
                .message("Complete exceution of "+joinPoint)
                .build();
        logRepository.save(entity);

    }

    @Around(value = "execution(* com.accolite.app.service.*.*(..))")
    public Object timeTracker(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime =System.currentTimeMillis();

        try {
            Object obj=joinPoint.proceed();
            long timeTaken=System.currentTimeMillis()- startTime;
            LogEntity entity = new LogEntity().builder()
                    .message("Time taken by "+joinPoint+" is "+timeTaken)
                    .build();
            logRepository.save(entity);
            return obj;
        }
        catch(ExceptionHandler e) {
            LogEntity entity = new LogEntity().builder()
                    .message("Exception Handler Message "+e.getMessage()+" Status Code : "+e.getHttpStatus().value())
                    .build();
            logRepository.save(entity);
            throw new ResponseStatusException(e.getHttpStatus(), e.getMessage());
        }
    }


}