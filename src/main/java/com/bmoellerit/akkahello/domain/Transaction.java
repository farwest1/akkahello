package com.bmoellerit.akkahello.domain;

import java.util.UUID;

/**
 * Created by Bernd on 30.03.2019.
 *
 * Package com.bmoellerit.akkahello.domain
 */
public final class Transaction {

  public enum TTYPE {ENTRY,EXIT}

  private final UUID uuid;
  private final long price;



  public static Transaction getTransaction(UUID uuid, long price){
    return new Transaction(uuid,price);
  }

  private Transaction(UUID uuid, long price) {
    this.uuid = uuid;
    this.price = price;
  }

  public UUID getUuid() {
    return uuid;
  }

  public long getPrice() {
    return price;
  }

}
