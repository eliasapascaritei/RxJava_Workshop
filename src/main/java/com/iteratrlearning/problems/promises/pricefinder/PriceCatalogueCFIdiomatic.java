package com.iteratrlearning.problems.promises.pricefinder;

import com.iteratrlearning.examples.promises.pricefinder.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.iteratrlearning.examples.promises.pricefinder.Currency.USD;

public class PriceCatalogueCFIdiomatic
{
    private final Catalogue catalogue = new Catalogue();
    private final PriceFinder priceFinder = new PriceFinder();
    private final ExchangeService exchangeService = new ExchangeService();

    public static void main(String[] args)
    {
        new PriceCatalogueCFIdiomatic().findLocalDiscountedPrice(Currency.CHF, "Nexus7");
    }

    private void findLocalDiscountedPrice(final Currency localCurrency, final String productName)
    {
        long time = System.currentTimeMillis();

        lookupProductByName(productName)

                .thenComposeAsync(this::findBestPrice)

                .thenCombineAsync(lookupExchangeRate(localCurrency), this::exchange)

                .thenAcceptAsync(localAmount ->
                {
                    System.out.printf("A %s will cost us %f %s\n", productName, localAmount, localCurrency);
                    System.out.printf("It took us %d ms to calculate this\n", System.currentTimeMillis() - time);
                }).join();
    }

    private double exchange(Price price, double exchangeRate)
    {
        return Utils.round(price.getAmount() * exchangeRate);
    }


    private CompletableFuture<Price> findBestPrice(Product product)
    {
        return CompletableFuture.supplyAsync(() -> priceFinder.findBestPrice(product));
    }

    private CompletableFuture<Product> lookupProductByName(String productName)
    {
        return CompletableFuture.supplyAsync(() -> catalogue.productByName(productName));
    }

    private CompletableFuture<Double> lookupExchangeRate(Currency localCurrency)
    {
        return CompletableFuture.supplyAsync(() -> exchangeService.lookupExchangeRate(Currency.USD, localCurrency));
    }
}

