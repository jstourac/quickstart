/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.helloworld;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

/**
 * A simple CDI service which is able to say hello to someone
 *
 * @author Pete Muir
 *
 */
public class HelloService {

    String createHelloMessage(String name) {
        return "Hello " + name + "!";
    }

    private Random random = new Random();

    @Outgoing("generated-price")
    public Flowable<Integer> generate() {
        return Flowable.interval(5, TimeUnit.SECONDS)
                .map(tick -> random.nextInt(100));
    }

    @Incoming("generated-price")
    @Outgoing("to-kafka")
    public Integer process(int priceInUsd) {
        System.out.println("This is generated number: " + priceInUsd);
        return Integer.valueOf(priceInUsd);
    }

    @Incoming("from-kafka")
    public void read(int priceInUsd) {
        System.out.println("This is read number: " + priceInUsd);
    }

    // TODO - try with some custom (de)serializer !!!

//    @GET
//    @Path("/prices")
//    @Produces(MediaType.SERVER_SENT_EVENTS) // denotes that server side events (SSE) will be produced
//    @SseElementType(MediaType.TEXT_PLAIN) // denotes that the contained data, within this SSE, is just regular text/plain data
//    public Publisher<Double> readThreePrices() {
//        // get the next three prices from the price stream
//        return ReactiveStreams.fromPublisher(prices)
//                .limit(3)
//                .buildRs();
//    }

    // Now let's try with Kafka
    // 1/ https://kafka.apache.org/downloads
    // 2/ https://kafka.apache.org/quickstart:
    //  a/ https://kafka.apache.org/quickstart#quickstart_startserver:
    //    I/ $ bin/zookeeper-server-start.sh config/zookeeper.properties
    //    II/ $ bin/kafka-server-start.sh config/server.properties
    //  b/ https://kafka.apache.org/quickstart#quickstart_createtopic
    //    I/ $ bin/kafka-topics.sh --create --topic helloworld-events --bootstrap-server localhost:9092
    //  c/ https://kafka.apache.org/quickstart#quickstart_send
    //    I/ $ bin/kafka-console-producer.sh --topic helloworld-events --bootstrap-server localhost:9092
    //  d/ https://kafka.apache.org/quickstart#quickstart_consume
    //    I/ $ bin/kafka-console-consumer.sh --topic helloworld-events --from-beginning --bootstrap-server localhost:9092
}
