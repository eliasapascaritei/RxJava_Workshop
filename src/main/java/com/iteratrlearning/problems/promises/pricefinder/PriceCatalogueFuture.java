package com.iteratrlearning.problems.promises.pricefinder;

import com.iteratrlearning.examples.promises.pricefinder.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.iteratrlearning.examples.promises.pricefinder.Currency.USD;

public class PriceCatalogueFuture
{
    private final Catalogue catalogue = new Catalogue();
    private final PriceFinder priceFinder = new PriceFinder();
    private final ExchangeService exchangeService = new ExchangeService();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws InterruptedException, ExecutionException
    {
        new PriceCatalogueFuture().findLocalDiscountedPrice(Currency.CHF, "Nexus7");
    }

    private void findLocalDiscountedPrice(final Currency localCurrency, final String productName) throws InterruptedException, ExecutionException {
            long time = System.currentTimeMillis();

            // TODO: submit to ExecutorService
            Future<Product> product = executorService.submit(() -> catalogue.productByName(productName));

            // TODO: submit to ExecutorService
            Future<Price> price = executorService.submit(() -> priceFinder.findBestPrice(product.get()));

            // TODO: submit to ExecutorService
            Future<Double> exchangeRate = executorService.submit(() -> exchangeService.lookupExchangeRate(USD, localCurrency));

            // TODO: merge two results
            double localPrice = exchange(price.get(), exchangeRate.get());

            System.out.printf("A %s will cost us %f %s\n", productName, localPrice, localCurrency);
            System.out.printf("It took us %d ms to calculate this\n", System.currentTimeMillis() - time);

    }

    private double exchange(Price price, double exchangeRate)
    {
        return Utils.round(price.getAmount() * exchangeRate);
    }

}

