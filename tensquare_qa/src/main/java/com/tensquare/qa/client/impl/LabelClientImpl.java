package com.tensquare.qa.client.impl;

import com.tensquare.qa.client.LabelClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

/**
 * feign自带的hystrix熔断器
 */
@Component
public class LabelClientImpl implements LabelClient {

    @Override
    public Result findById(String labelId) {
        return new Result(false, StatusCode.ERROR, "服务发生熔断");
    }
}