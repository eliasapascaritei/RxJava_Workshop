package com.iteratrlearning.problems.actors.akkabasics;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class CounterActor extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private int count = 0;

    @Override
    public void onReceive(Object message) throws Throwable {

        // TODO: should record number of times it receives messages
        if(message.equals("Count")) {
            getSender().tell(getCount(), getSelf());
        }
        // TODO: should stop actor when receiving "Stop"
        if(message.equals("Stop")){
            getContext().stop(getSelf());
        }
        // TODO: log all messages
        log.info("Received Message: " + message);
        count++;

    }

    public int getCount() {
        return count;
    }

}
