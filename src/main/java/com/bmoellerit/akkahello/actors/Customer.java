package com.bmoellerit.akkahello.actors;

import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.persistence.AbstractPersistentActor;
import com.bmoellerit.akkahello.domain.Transaction;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Bernd on 31.03.2019.
 *
 * Package com.bmoellerit.akkahello.actors
 */

class CustomerEvent implements Serializable {

  private static final long serialVersionUID = 1L;
  private final String data;


  public CustomerEvent(String data) {
    this.data = data;
  }

  public String getData() {
    return data;
  }
}

class CustomerState implements Serializable {
  private static final long serialVersionUID = 1L;
  private final ArrayList<String> events;

  public CustomerState(){
    this(new ArrayList<String>());
  }

  public CustomerState(ArrayList<String> events) {
    this.events = events;
  }

  public void update(CustomerEvent event){
    events.add(event.getData());
  }
}



public class Customer extends AbstractPersistentActor {

  private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this  );
  private final String id;
  private CustomerState customerState= new CustomerState();

  public static Props props(String id) {

    return Props.create(Customer.class, ()-> new Customer(id));
  }

  public Customer(String id) {
    this.id = id;
  }

  @Override
  public void preStart() throws Exception {
    log.info("new Customer started");
  }

  //TODO: To be implemented
  @Override
  public Receive createReceiveRecover() {

    return receiveBuilder()
        .match(CustomerEvent.class,customerEvent -> {
          log.info("Replay Customer Event");
        }).build();
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(Transaction.class, transaction -> {
          log.info("Received transaction");
          final CustomerEvent customerEvent = new CustomerEvent("Bla");
          persist(customerEvent, (CustomerEvent e) -> {

            getContext().getSystem().getEventStream().publish(e);
          });
        } )
        .build();
  }

  @Override
  public String persistenceId() {
    return this.id;
  }
}
