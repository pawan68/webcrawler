//package com.learnnbuild.webcrawler;
//
//import com.learnnbuild.webcrawler.kafka.Payload;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.stereotype.Component;
//import org.springframework.util.concurrent.ListenableFuture;
//import org.springframework.util.concurrent.ListenableFutureCallback;
//
//@Component
//public class Producer {
//
//    @Autowired
//    private KafkaTemplate<String, Payload> kafkaTemplate;
//
//    public void sendMessage(Payload payload, String topic) {
//        ListenableFuture<SendResult<String, Payload>> future =
//                kafkaTemplate.send(topic, payload);
//
//        future.addCallback(new ListenableFutureCallback<SendResult<String, Payload>>() {
//
//            @Override
//            public void onSuccess(SendResult<String, Payload> result) {
//                System.out.println("Sent message=[" + payload +
//                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
//            }
//
//            @Override
//            public void onFailure(Throwable ex) {
//                System.out.println("Unable to send message=["
//                        + payload + "] due to : " + ex.getMessage());
//            }
//        });
//    }
//}

package com.learnnbuild.webcrawler;
import com.learnnbuild.webcrawler.kafka.Payload;
import org.apache.kafka.clients.producer.internals.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


public class Producer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

//    @Value("${kafka.topic.json}")
//    private String jsonTopic;

    @Autowired
    private KafkaTemplate<String, Payload> kafkaTemplate;

    public void send(Payload payload) {
        LOGGER.info("sending payload='{}'", payload.toString());
        kafkaTemplate.send("webCrawler", payload);
    }
}
