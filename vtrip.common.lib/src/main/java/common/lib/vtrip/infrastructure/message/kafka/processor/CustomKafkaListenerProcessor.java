package common.lib.vtrip.infrastructure.message.kafka.processor;

import common.lib.vtrip.infrastructure.message.kafka.annotation.CustomKafkaListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Properties;

@Slf4j
@Component
public class CustomKafkaListenerProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        for (Method method : bean.getClass().getMethods()) {
            CustomKafkaListener ann = AnnotationUtils.findAnnotation(method, CustomKafkaListener.class);
            if (ann != null) {
                adjustContainer(beanName, method, ann);
            }
        }
        return bean;
    }

    private void adjustContainer(String beanName, Method method, CustomKafkaListener ann) {
        KafkaListenerEndpointRegistry registry = context.getBean(KafkaListenerEndpointRegistry.class);

        String targetId = ann.id().isEmpty()
                ? beanName + "." + method.getName()
                : ann.id();

        MessageListenerContainer container = registry.getListenerContainer(targetId);
        if (container instanceof ConcurrentMessageListenerContainer<?, ?> concurrentContainer) {

            // Override pollTimeout
            if (ann.pollTimeout() > 0) {
                concurrentContainer.getContainerProperties().setPollTimeout(ann.pollTimeout());
            }

            // Override AckMode (manual commit only)
            if (!ann.containerFactory().equals("autoCommitListenerFactory")) {
                concurrentContainer.getContainerProperties().setAckMode(ann.ackMode());
            }

            // Merge additional properties
            if (ann.properties().length > 0) {
                Properties existingProps =
                        concurrentContainer.getContainerProperties().getKafkaConsumerProperties();
                if (existingProps == null) existingProps = new Properties();

                for (String prop : ann.properties()) {
                    String[] split = prop.split("=", 2);
                    if (split.length == 2) {
                        existingProps.put(split[0], split[1]);
                    }
                }
                concurrentContainer.getContainerProperties().setKafkaConsumerProperties(existingProps);
            }
        }
    }
}

