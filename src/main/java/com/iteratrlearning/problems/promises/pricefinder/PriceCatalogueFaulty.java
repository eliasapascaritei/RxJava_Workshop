package com.iteratrlearning.problems.promises.pricefinder;

import com.iteratrlearning.examples.promises.pricefinder.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.iteratrlearning.examples.promises.pricefinder.Currency.USD;

public class PriceCatalogueFaulty {
        private final Catalogue catalogue = new Catalogue();
        private final PriceFinder priceFinder = new PriceFinder();
        private final AsyncExchangeServiceFaulty exchangeService = new AsyncExchangeServiceFaulty();
        private final ExecutorService executorService = Executors.newFixedThreadPool(10);

        public static void main(String[] args) throws InterruptedException {
            new PriceCatalogueFaulty().findLocalDiscountedPrice(Currency.CHF, "Nexus7");
        }

    private void findLocalDiscountedPrice(final Currency localCurrency, final String productName)
    {

        long time = System.currentTimeMillis();

        lookupProductByName(productName)
                .thenComposeAsync(this::findBestPrice, executorService)
                .thenCombineAsync(exchangeService.lookupExchangeRateAsync(USD, localCurrency), this::exchange)
                .thenApply(localPrice -> {
                    String output = String.format("A %s will cost us %f %s\n", productName, localPrice,
                            localCurrency);
                    output += String.format("It took us %d ms to calculate this\n", System.currentTimeMillis() - time);
                    return output;
                })
                .exceptionally(ex -> "Err: " + ex.getCause().getMessage())
                .thenAccept(System.out::println);

    }

    private double exchange(Price price, double exchangeRate)
    {
        return Utils.round(price.getAmount() * exchangeRate);
    }



    private CompletableFuture<Product> lookupProductByName(String productName)
    {
        return CompletableFuture.supplyAsync(() -> catalogue.productByName(productName), executorService);
    }


    private CompletableFuture<Price> findBestPrice(Product product) {
        return CompletableFuture.supplyAsync(() -> priceFinder.findBestPrice
                (product));
    }


}
