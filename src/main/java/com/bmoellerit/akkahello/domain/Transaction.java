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
  private final long customerId;



  public static Transaction getTransaction(long customerId, long price){
    return new Transaction(customerId,price);
  }

  private Transaction(long customerId, long price) {
    this.uuid = UUID.randomUUID();
    this.price = price;
    this.customerId = customerId;
  }

  public UUID getUuid() {
    return uuid;
  }

  public long getPrice() {
    return price;
  }

  public long getCustomerId() {
    return customerId;
  }
}
