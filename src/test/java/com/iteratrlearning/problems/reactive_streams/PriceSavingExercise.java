package com.iteratrlearning.problems.reactive_streams;

import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PriceSavingExercise
{
    private static final double MIN_PRICE = 120;
    private static final double BID_RANGE = 50;
    private static final double MAX_SPREAD = 10;

    private final Random random = new Random();
    private final PrintStream saveTo;

    public static void main(String[] args) throws FileNotFoundException
    {
        new PriceSavingExercise().run();
    }

    public PriceSavingExercise() throws FileNotFoundException
    {
        final File file = new File("prices");
        if (file.exists())
        {
            file.delete();
        }

        saveTo = new PrintStream(new FileOutputStream(file));
    }

    private void run()
    {
        writeTitle();

        // TODO: Switch Milliseconds to Nanoseconds
        // TODO: Re-implement with reactive pull back-pressure in order to enable prices to be dragged through the system
        // Hint: Create a custom publisher and use subscriber rather than blocking subscribe.
        /*Flowable.interval(500, TimeUnit.MILLISECONDS)
                .map(i -> nextPrice())
                .blockingSubscribe(this::write, Throwable::printStackTrace);*/
        Flowable.interval(500, TimeUnit.NANOSECONDS)
                .fromPublisher(nextPricePublisher())
                .subscribe(this::write, Throwable::printStackTrace);
    }

    private Publisher<PriceUpdate> nextPricePublisher()
    {
        return subscriber -> subscriber.onSubscribe(new Subscription()
        {
            @Override
            public void request(final long count)
            {
                for (long i = 0; i < count; i++)
                {
                    subscriber.onNext(nextPrice());
                }
            }

            @Override
            public void cancel()
            {
                subscriber.onComplete();
            }
        });
    }

    private PriceUpdate nextPrice()
    {
        final double bid = MIN_PRICE + BID_RANGE * random.nextDouble();
        final double ask = bid + MAX_SPREAD * random.nextDouble();
        return new PriceUpdate(bid, ask);
    }

    private void write(final PriceUpdate priceUpdate)
    {
        writeRow(String.valueOf(priceUpdate.bid), String.valueOf(priceUpdate.ask));
    }

    private void writeTitle()
    {
        writeRow("Bid", "Ask");
    }

    private void writeRow(final String bid, final String ask)
    {
        saveTo.append(bid);
        saveTo.append(", ");
        saveTo.append(ask);
        saveTo.append("\n");
    }

    private static class PriceUpdate
    {
        private final double bid;
        private final double ask;

        private PriceUpdate(final double bid, final double ask)
        {
            this.bid = bid;
            this.ask = ask;
        }
    }
}
