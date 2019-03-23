package com.bmoellerit.akkahello;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.RequestEntity;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import com.bmoellerit.akkahello.domain.Item;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

/**
 * Created by Bernd on 17.02.2019.
 *
 * Package com.bmoellerit.akkahello
 */

//TODO: Replace this by HttpApp
public class AkkaHelloApp extends AllDirectives {

  public static void main(String[] args) throws java.io.IOException{
    ActorSystem system = ActorSystem.create();
    final Http http = Http.get(system);
    Materializer materializer = ActorMaterializer.create(system);
    final Source<Integer, NotUsed> source = Source.range(1,1000);

    ActorRef myFirstActorRef = system.actorOf(MyFirstActor.props());

    //In order to access all directives we need an instance where the routes are define.
    AkkaHelloApp app = new AkkaHelloApp();

    final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
    final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
        ConnectHttp.toHost("localhost", 8080), materializer);

    System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
    System.in.read(); // let it run until user presses return

    binding
        .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
        .thenAccept(unbound -> system.terminate()); // and shutdown when done

  }

  private Route createRoute() {
//    return concat(
//        path("item", ()-> get(()-> completeOK(new Item(UUID.randomUUID(),"Test"),
//            Jackson.marshaller()))));
    return concat(
        get(
          ()->pathPrefix(
            "item",()->completeOK(
                new Item(UUID.randomUUID(),"Bernd"), Jackson.marshaller()))),
        get(
            ()->path("hello",()->complete("Hola"))));
  }
}
