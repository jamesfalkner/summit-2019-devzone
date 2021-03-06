package org.acme.quarkus.sample;

import io.reactivex.Flowable;

import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * A bean producing random prices every second.
 * The prices are written to a Kafka topic (prices). The Kafka configuration is specified in the application configuration.
 */
@ApplicationScoped
public class PriceGenerator {

    private Random random = new Random();

   @Outgoing("topic-price")
    public Flowable<Integer> generate() {
        return Flowable.interval(1000, TimeUnit.MILLISECONDS)
                .map(tick -> random.nextInt(100));
    }

}
