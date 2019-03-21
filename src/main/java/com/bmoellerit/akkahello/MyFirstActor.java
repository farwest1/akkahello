package com.bmoellerit.akkahello;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by Bernd on 17.02.2019.
 *
 * Package com.bmoellerit.akkahello
 */
public class MyFirstActor extends AbstractActor {

  LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  static Props props() {

    return Props.create(MyFirstActor.class);
  }

  @Override
  public void preStart() throws Exception {
    log.info("Creating MyFirstActor");
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .matchEquals(
            "printit",
            p -> {
              ActorRef secondRef = getContext().actorOf(Props.empty(), "second-actor");
              System.out.println("Second: " + secondRef);
            })
        .build();
  }
}
