package com.bmoellerit.akkahello;

import static akka.pattern.Patterns.ask;
import static akka.event.Logging.InfoLevel;
import static akka.http.javadsl.server.PathMatchers.longSegment;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Source;
import com.bmoellerit.akkahello.actors.Customer;
import com.bmoellerit.akkahello.actors.CustomerSupervisor;
import com.bmoellerit.akkahello.actors.MyFirstActor;
import com.bmoellerit.akkahello.domain.Item;
import com.bmoellerit.akkahello.domain.Transaction;
import com.bmoellerit.akkahello.domain.Transaction.TTYPE;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by Bernd on 17.02.2019.
 *
 * Package com.bmoellerit.akkahello
 */

//TODO: Replace this by HttpApp
public class AkkaHelloApp extends AllDirectives {

  private static ActorSystem system;



  public static void main(String[] args) throws java.io.IOException{
    system = ActorSystem.create("CustomerSystem");
    final Http http = Http.get(system);
    Materializer materializer = ActorMaterializer.create(system);
    final Source<Integer, NotUsed> source = Source.range(1,1000);

    ActorRef myFirstActorRef = system.actorOf(MyFirstActor.props());
    ActorRef customerSupervisorRef = system.actorOf(CustomerSupervisor.props());

    //In order to access all directives we need an instance where the routes are define.
    AkkaHelloApp app = new AkkaHelloApp();

    final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute(myFirstActorRef).flow(system, materializer);
    final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
        ConnectHttp.toHost("localhost", 8080), materializer);

    System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
    System.in.read(); // let it run until user presses return

    binding
        .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
        .thenAccept(unbound -> system.terminate()); // and shutdown when done

  }

  private Route createRoute(ActorRef myFirstActor) {
//    return concat(
//        path("item", ()-> get(()-> completeOK(new Item(UUID.randomUUID(),"Test"),
//            Jackson.marshaller()))));
    return concat(
        get(
            () -> logRequest("Logged", InfoLevel(),
              ()->pathPrefix(
              "item",()-> {
                    CompletionStage<Item> futureItem = getItem();
                    return onSuccess(futureItem, it -> completeOK(it, Jackson.marshaller()));
                  }
              )
            )
        ),
        get(
            ()->path("hello",()->complete("Hola"))),
        get(
            ()->path("future",()->{
              CompletionStage<Object> futureString = askActor(myFirstActor);
              return onSuccess(futureString, rsp -> completeOK(rsp,Jackson.marshaller()));
            } )
        ),
        get(
            ()->path("trans", ()->{
              CompletionStage<Object> futureTrans = getTransaction();
              return onSuccess(futureTrans, tr -> completeOK((Transaction) tr, Jackson.marshaller()) );
            })
        ),
        post(
            ()-> pathPrefix("trans", () ->
              path(longSegment(), (Long id) ->{
                //TODO create customer actors depending on the id and register them in an supervisor
                ActorRef customer = system.actorOf(Customer.props("customer-" + id.toString()));
                customer.tell(Transaction.getTransaction(UUID.randomUUID(),100L), ActorRef.noSender());
                return complete("Transaction received");
            }))
        )
    );
  }



  private CompletionStage<Object> askActor(ActorRef act){
    return ask(act,"printit",Duration.ofMillis(1000L)).toCompletableFuture();
  }

  private CompletionStage<Item> getItem(){
    return CompletableFuture.completedFuture(new Item(UUID.randomUUID(), "Future"));
  }

  private CompletionStage<Object> getTransaction(){
    return ask(system.actorOf(MyFirstActor.props()),"trans", Duration.ofMillis(1000L)).toCompletableFuture();
  }
}
