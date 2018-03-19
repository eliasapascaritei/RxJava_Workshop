package com.iteratrlearning.examples.reactive_streams;

import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

public class ErrorHandledPriceApiClient
{
    public static Flowable<String> pricesAtInterval()
    {
        return Flowable
            .interval(300, TimeUnit.MILLISECONDS)
            .map(x -> PriceApiService.getPriceLimited());
    }

    public static void main(String[] args)
    {
        pricesAtInterval()
            .blockingSubscribe(System.out::println, Throwable::printStackTrace);
    }
}
