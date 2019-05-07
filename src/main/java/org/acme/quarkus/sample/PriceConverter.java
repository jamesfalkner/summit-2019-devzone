package org.acme.quarkus.sample;

import io.smallrye.reactive.messaging.annotations.Broadcast;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;

/**
 * A bean consuming data from the "prices" Kafka topic and applying some conversion.
 * The result is pushed to the "my-data-stream" stream which is an in-memory stream.
 */
@ApplicationScoped
public class PriceConverter {

    private static double highestPriceSoFar = 0.0;
    private static final double CONVERSION_RATE = 0.88;

    @Counted(name = "convertedPrices", monotonic = true, description = "How many prices have been converted.")
    @Timed(name = "converter", description = "A measure how long it takes to convert prices.", unit = MetricUnits.MILLISECONDS)
    @Incoming("prices")
    @Outgoing("my-data-stream")
    @Broadcast
    public double process(int priceInUsd) {
        double price = priceInUsd * CONVERSION_RATE;
        highestPriceSoFar = Math.max(highestPriceSoFar, price);
        return price;
    }

    @Gauge(name = "highestPriceSoFar", unit = MetricUnits.NONE, description = "Highest price so far.")
    public Double highestPriceSoFar() {
        return highestPriceSoFar;
    }

}
