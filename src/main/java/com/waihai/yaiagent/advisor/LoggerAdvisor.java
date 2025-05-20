package com.waihai.yaiagent.advisor;

import com.waihai.common.enums.LogLevelEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

/**
 * 自定义日志 Advisor
 */
@Slf4j
public class LoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    private LogLevelEnum level;

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public int getOrder() {
        return 0;
    }

    public LoggerAdvisor() {}

    public LoggerAdvisor(String level) {
        LogLevelEnum enumByLevel = LogLevelEnum.getEnumByLevel(level);
        if (enumByLevel == null) {
            throw new RuntimeException("无效的日志级别: " + level);
        }
        this.level = enumByLevel;
    }


    private AdvisedRequest before(AdvisedRequest request) {
        switch (level) {
            case DEBUG:
                log.debug("AI Request: {}", request.userText());
                break;
            case INFO:
                log.info("AI Request: {}", request.userText());
                break;
            case WARNING:
                log.warn("AI Request: {}", request.userText());
                break;
            case ERROR:
                log.error("AI Request: {}", request.userText());
                break;
        }
        return request;
    }


    private void observeAfter(AdvisedResponse advisedResponse) {
        switch (level) {
            case DEBUG:
                log.debug("AI Response: {}", advisedResponse.response().getResult().getOutput().getText());
                break;
            case INFO:
                log.info("AI Response: {}", advisedResponse.response().getResult().getOutput().getText());
                break;
            case WARNING:
                log.warn("AI Response: {}", advisedResponse.response().getResult().getOutput().getText());
                break;
            case ERROR:
                log.error("AI Response: {}", advisedResponse.response().getResult().getOutput().getText());
                break;
        }
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        before(advisedRequest);
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);
        observeAfter(advisedResponse);
        return advisedResponse;
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        before(advisedRequest);
        Flux<AdvisedResponse> advisedResponseFlux = chain.nextAroundStream(advisedRequest);
        return (new MessageAggregator().aggregateAdvisedResponse(advisedResponseFlux, this::observeAfter));
    }
}
