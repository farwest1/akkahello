package com.bmoellerit.akkahello.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.bmoellerit.akkahello.domain.Transaction;
import com.bmoellerit.akkahello.domain.Transaction.TTYPE;
import java.util.UUID;

/**
 * Created by Bernd on 17.02.2019.
 *
 * Package com.bmoellerit.akkahello
 */
public class MyFirstActor extends AbstractActor {

  private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  public static Props props() {

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
              //ActorRef secondRef = getContext().actorOf(Props.empty(), "second-actor");
              getSender().tell("Rhabarb", getSelf());
              log.info("Message {} processed", "printit");
            })
        .matchEquals(
            "trans",
            p -> {
              getSender().tell(Transaction.getTransaction(UUID.randomUUID(), 100L, TTYPE.ENTRY), getSelf());
            })
        .build();
  }
}
