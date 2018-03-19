package com.iteratrlearning.problems.actors.movie;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static akka.testkit.JavaTestKit.duration;
import static org.junit.Assert.fail;

@Ignore
public class StorageActorTest {

    public static final String MOVIE1 = "StarWars";
    public static final String MOVIE2 = "JamesBond";
    ActorSystem system;

    @Test
    public void testNotWatchedMovie() {
        JavaTestKit probe = new JavaTestKit(system);
        final Props props = Props.create(StorageActor.class);
        final ActorRef storageActor = system.actorOf(props);
        storageActor.tell(new InfoMovieMessage(MOVIE1), probe.getRef());

        //TODO: assert using the probe that the reply is InfoReplyMovieMessage(MOVIE1, 0)
        probe.expectMsgEquals(duration("3 second"), new InfoReplyMovieMessage(MOVIE1, 0));
    }

    @Test
    public void testWatchMovieOnce() {
        JavaTestKit probe = new JavaTestKit(system);
        final Props props = Props.create(StorageActor.class);
        final ActorRef storageActor = system.actorOf(props);

        // TODO: send one ViewMovieMessage to storageActor
        storageActor.tell(new ViewMovieMessage(MOVIE1), probe.getRef());
        // TODO: send one InfoMovieMessage to storageActor
        storageActor.tell(new InfoMovieMessage(MOVIE1), probe.getRef());
        // TODO: assert using the probe that the reply is InfoReplyMovieMessage(MOVIE1, 1)
        probe.expectMsgEquals(duration("3 second"), new InfoReplyMovieMessage(MOVIE1, 1));
    }

    @Test
    public void testWatchSeveralMovies() {
        JavaTestKit probe = new JavaTestKit(system);
        final Props props = Props.create(StorageActor.class);
        final ActorRef storageActor = system.actorOf(props);

        // TODO: send two ViewMovieMessage(MOVIE1)
        storageActor.tell(new ViewMovieMessage(MOVIE1), probe.getRef());
        storageActor.tell(new ViewMovieMessage(MOVIE1), probe.getRef());
        // TODO: send three ViewMovieMessage(MOVIE2)
        storageActor.tell(new ViewMovieMessage(MOVIE2), probe.getRef());
        storageActor.tell(new ViewMovieMessage(MOVIE2), probe.getRef());
        storageActor.tell(new ViewMovieMessage(MOVIE2), probe.getRef());
        // TODO: send one InfoMovieMessage(MOVIE1)
        storageActor.tell(new InfoMovieMessage(MOVIE1), probe.getRef());
        // TODO: send one InfoMovieMessage(MOVIE2)
        storageActor.tell(new InfoMovieMessage(MOVIE2), probe.getRef());
        // TODO: assert using the probe that correct replies are returned
        // Note: you can use probe.expectMsgAllOf
        probe.expectMsgAllOf(duration("3 second"),
                new InfoReplyMovieMessage(MOVIE1, 2),
                new InfoReplyMovieMessage(MOVIE2, 3));
    }

    @Before
    public void setup() {
        system = ActorSystem.create();
    }

    @After
    public void teardown() {
        JavaTestKit.shutdownActorSystem(system);
    }

}
