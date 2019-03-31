package com.bmoellerit.akkahello.actors;

import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.persistence.AbstractPersistentActor;
import akka.persistence.PersistentActor;
import com.bmoellerit.akkahello.domain.Transaction;
import com.bmoellerit.akkahello.domain.Transaction.TTYPE;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Bernd on 31.03.2019.
 *
 * Package com.bmoellerit.akkahello.actors
 */
public class Customer extends AbstractPersistentActor {

  private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this  );

  public static Props props() {

    return Props.create(Customer.class);
  }

  class CustomerEvent implements Serializable {
    private final TTYPE ttype;

    public CustomerEvent(TTYPE ttype){
      this.ttype = ttype;
    }

    public TTYPE getTtype() {
      return ttype;
    }
  }

  //TODO: To be implemented
  @Override
  public Receive createReceiveRecover() {
    return null;
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(Transaction.class, transaction -> {
          log.info("Received transaction");
          final TTYPE ttype = transaction.getTtype();
          final CustomerEvent customerEvent = new CustomerEvent(ttype);
          persist(customerEvent, (CustomerEvent e) -> {
            getContext().getSystem().getEventStream().publish(e);
          });
        } )
        .build();
  }

  @Override
  public String persistenceId() {
    return UUID.randomUUID().toString();
  }
}
