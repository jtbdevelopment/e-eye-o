package com.jtbdevelopment.e_eye_o.loadbalancers;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.access.channel.ChannelDecisionManagerImpl;

import java.util.Arrays;

/**
 * Date: 10/15/13
 * Time: 9:31 PM
 * <p/>
 * From:
 * http://stackoverflow.com/questions/8002272/offloading-https-to-load-balancers-with-spring-security
 * slightly modified
 */
@Configuration
public class LoadBalancerHack implements BeanPostProcessor {

    @Autowired
    SecureChannelProcessorHack secureChannelProcessorHack;

    @Autowired
    InsecureChannelProcessorHack insecureChannelProcessorHack;

    @Value("${loadbalancer.terminateshttps}")
    boolean behindLoadBalancer;

    @Value("${url.root}")
    private String urlRoot;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (behindLoadBalancer && bean instanceof ChannelDecisionManagerImpl && !urlRoot.contains("localhost")) {
            ((ChannelDecisionManagerImpl) bean).setChannelProcessors(Arrays.asList(
                    insecureChannelProcessorHack,
                    secureChannelProcessorHack
            ));
        }
        return bean;
    }

}
