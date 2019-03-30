package com.bmoellerit.akkahello.domain;

import java.util.UUID;

/**
 * Created by Bernd on 30.03.2019.
 *
 * Package com.bmoellerit.akkahello.domain
 */
public class Transaction {
  private UUID uuid;
  private long price;

  public Transaction(UUID uuid, long price) {
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
