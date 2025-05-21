package com.waihai.yaiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.MessageAggregator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * 自定义日志 Advisor
 * DEBUG 级别提供更详细的信息，INFO 级别提供简洁信息
 */
@Slf4j
@Component
public class LoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    private static final String REQUEST_TEMPLATE = "AI Request: {}";
    private static final String RESPONSE_TEMPLATE = "AI Response: {}";

    private static final String DEBUG_REQUEST_TEMPLATE = "AI Request [{}]: {}";
    private static final String DEBUG_RESPONSE_TEMPLATE = "AI Response [{}]: {}";

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public int getOrder() {
        return 0;
    }

    private void logRequest(AdvisedRequest request) {
        if (request == null || request.userText() == null) {
            return;
        }

        if (log.isDebugEnabled()) {
            // DEBUG 级别记录更详细的信息
            log.debug(DEBUG_REQUEST_TEMPLATE,
                    request.advisorParams(),
                    request.userText());
        } else if (log.isInfoEnabled()) {
            // INFO 级别只记录简要信息
            log.info(REQUEST_TEMPLATE,
                    truncateIfTooLong(request.userText(), 100));
        }
    }

    private void logResponse(AdvisedResponse response) {
        if (response == null ||
                response.response() == null ||
                response.response().getResult() == null ||
                response.response().getResult().getOutput() == null ||
                response.response().getResult().getOutput().getText() == null) {
            return;
        }

        String responseText = response.response().getResult().getOutput().getText();

        if (log.isDebugEnabled()) {
            // DEBUG 级别记录详细响应
            log.debug(DEBUG_RESPONSE_TEMPLATE,
                    response.adviseContext(),
                    responseText);
        } else if (log.isInfoEnabled()) {
            // INFO 级别只记录简要信息
            log.info(RESPONSE_TEMPLATE,
                    truncateIfTooLong(responseText, 100));
        }
    }

    // 如果文本过长则截断
    private String truncateIfTooLong(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }

    private AdvisedRequest before(AdvisedRequest request) {
        logRequest(request);
        return request;
    }

    private void observeAfter(AdvisedResponse advisedResponse) {
        logResponse(advisedResponse);
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