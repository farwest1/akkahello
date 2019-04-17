package com.bmoellerit.akkahello.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by Bernd on 17.04.2019.
 *
 * Package com.bmoellerit.akkahello.actors
 */
public class CustomerSupervisor extends AbstractActor {

  private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this  );

  public static Props props() {
    return Props.create(CustomerSupervisor.class, CustomerSupervisor::new);
  }

  @Override
  public void preStart() throws Exception {
    log.info("Starting CustomerSupervisor");
  }

  @Override
  public void postStop() throws Exception {
    log.info("CustomerSupervisor stopped");
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder().build();
  }
}
