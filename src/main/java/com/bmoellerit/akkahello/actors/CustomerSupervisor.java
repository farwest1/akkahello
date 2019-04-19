package com.bmoellerit.akkahello.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.bmoellerit.akkahello.domain.Transaction;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bernd on 17.04.2019.
 *
 * Package com.bmoellerit.akkahello.actors
 */
public class CustomerSupervisor extends AbstractActor {

  private LoggingAdapter log = Logging.getLogger(getContext().getSystem(),this  );
  private HashMap<Long,ActorRef> customerActors = new HashMap<>();


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
    return receiveBuilder().match(
        Transaction.class, transaction -> {
          log.info("Received Transaction");
          Long id = Long.valueOf(transaction.getCustomerId());
          if (!customerActors.containsKey(id)){
            log.info("Create new Customer Actor");
            customerActors.put(Long.valueOf(transaction.getCustomerId()), getContext().actorOf(Customer.props("customer-" + transaction.getCustomerId())));
          }
          customerActors.get(id).tell(transaction,getSelf());
        }
    ).build();
  }
}
