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
  private final TTYPE ttype;


  public static Transaction getTransaction(UUID uuid, long price, TTYPE ttype){
    return new Transaction(uuid,price,ttype);
  }

  private Transaction(UUID uuid, long price,TTYPE ttype) {
    this.uuid = uuid;
    this.price = price;
    this.ttype = ttype;
  }

  public UUID getUuid() {
    return uuid;
  }

  public long getPrice() {
    return price;
  }

  public TTYPE getTtype() {
    return ttype;
  }
}
