package com.iteratrlearning.problems.promises.pricefinder;

import com.iteratrlearning.examples.promises.pricefinder.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.iteratrlearning.examples.promises.pricefinder.Currency.USD;
import static java.util.stream.Collectors.toList;

public class PriceCatalogueAll {

    private final PriceFinder priceFinder = new PriceFinder();
    private final ExchangeService exchangeService = new ExchangeService();

    public static void main(String[] args)
    {

        new PriceCatalogueAll().findAllDiscountedPrice(Currency.CHF, Catalogue.products);
    }

    private void findAllDiscountedPrice(final Currency localCurrency, List<Product> products)
    {
        long time = System.currentTimeMillis();

        // calculates total price for list of products
        CompletableFuture<Double> exchangeRate = CompletableFuture.supplyAsync(() ->exchangeService.lookupExchangeRate(USD, localCurrency));
        CompletableFuture<List<Price>> priceList = sequence(products
                .stream()
                .map(p -> CompletableFuture.supplyAsync(() -> priceFinder.findBestPrice(p)))
                .collect(toList()));

        priceList.thenCombine(exchangeRate, (list, exgR)->
                list
                .stream()
                .mapToDouble(price -> exchange(price, exgR))
                .sum())
                .thenAccept(totalPrice ->
                        System.out.printf("Total price is %f %s\n", totalPrice, localCurrency))
                .join();

        System.out.printf("It took us %d ms to calculate this\n", System.currentTimeMillis() - time);
    }

    private double exchange(Price price, double exchangeRate)
    {
        return Utils.round(price.getAmount() * exchangeRate);
    }

    private <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allFuturesDone =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allFuturesDone.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(toList()));
    }
}
