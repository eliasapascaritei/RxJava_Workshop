package com.iteratrlearning.examples.reactive_streams;

import reactor.core.publisher.Flux;

import java.util.ArrayList;

public class SpringReactorOperations
{
    public static void main(String[] args)
    {
        // 0. Find the names of tracks over 5 mins
        // 1. check we have a track over 5 mins (all/allMatch, doOnNext/peek)
        // 2. contains Stairway to Heaven
        // 3. Total length of all tracks (reduce)
        // 4. create a list of the values (collect)
        // 5. Dot product of two Flowables (zip)

        Flux.fromArray(Tracks.allTracks)
            .filter(track -> track.getLengthInSeconds() > 300)
            .map(Track::getName)
            .subscribe(System.out::println);

        Flux.fromArray(Tracks.allTracks)
            .doOnNext(System.out::println)
            .all(track -> track.getLengthInSeconds() > 300)
            .subscribe(System.out::println);

        System.out.println();

        Flux.fromArray(Tracks.allTracks)
            .any(track -> track == Tracks.stairwayToHeaven)
            // .contains(Tracks.stairwayToHeaven)
            .subscribe(System.out::println);

        System.out.println();

        Flux.fromArray(Tracks.allTracks)
            .reduce(0, (acc, track) -> acc + track.getLengthInSeconds())
            .subscribe(System.out::println);

        System.out.println();

        Flux.fromArray(Tracks.allTracks)
            .collect(ArrayList::new, ArrayList::add)
            .subscribe(System.out::println);

        System.out.println();

        final Flux<Integer> left = Flux.just(1, 2, 3);
        final Flux<Integer> right = Flux.just(4, -5, 6);

        left.zipWith(right, (x, y) -> x * y)
            .reduce(0, Integer::sum)
            .subscribe(System.out::println);
    }

}
