package common.lib.vtrip.infrastructure.message.kafka.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ContainerProperties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@KafkaListener
public @interface CustomKafkaListener {
    @AliasFor(annotation = KafkaListener.class, attribute = "id")
    String id() default "";
    @AliasFor(annotation = KafkaListener.class, attribute = "topics")
    String topics();
    @AliasFor(annotation = KafkaListener.class, attribute = "groupId")
    String groupId() default "";
    String description() default "";
    String[] properties() default {};
    @AliasFor(annotation = KafkaListener.class, attribute = "containerFactory")
    String containerFactory() default "autoCommitListenerFactory";
    long pollTimeout() default 5000;
    ContainerProperties.AckMode ackMode() default ContainerProperties.AckMode.BATCH;
    String concurrency() default "1";
}
