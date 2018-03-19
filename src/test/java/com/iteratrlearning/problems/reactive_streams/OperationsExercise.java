package com.iteratrlearning.problems.reactive_streams;

import com.iteratrlearning.examples.reactive_streams.Track;
import com.iteratrlearning.examples.reactive_streams.Tracks;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.junit.Ignore;
import org.junit.Test;

@Ignore // TODO: remove Ignore
public class OperationsExercise
{

    // The tracks can be found in the field:
    // com.iteratrlearning.examples.reactive_streams.old.Tracks.allTracks

    @Test
    public void canFindAllArtistsWithTracksOver200Seconds()
    {
        // TODO: find all artist names with tracks longer than 200 seconds.
        final Flowable<String> names = Flowable.fromArray(Tracks.allTracks)
                .filter(t -> t.getLengthInSeconds() > 200)
                .map(Track::getArtist)
                .distinct();

        names.test().assertResult(Tracks.LED_ZEPPELIN, Tracks.THE_BEATLES);
    }

    @Test
    public void isThereATrackByPinkFloyd()
    {
        // TODO: check if there is a track by pink floyd
        final Single<Boolean> names = Flowable.fromArray(Tracks.allTracks)
                .map(t -> t.getArtist())
                .any(Tracks.PINK_FLOYD::equals);

        names.test().assertResult(true);
    }

    @Test
    public void canFindTheNameOfTheShortestTrackByLedZeppelin()
    {
        // TODO: find the name of the shortest track by Led Zeppelin
        final Maybe<String> names = Flowable.fromArray(Tracks.allTracks)
                .filter(t -> t.getArtist().equals(Tracks.LED_ZEPPELIN))
                .reduce((ac, t) -> {
                    if(ac.getLengthInSeconds() < t.getLengthInSeconds()){
                        return ac;
                    } else {
                        return t;
                    }
                })
                .map(Track::getName);

        names.test().assertResult("Rock and Roll");
    }
}
