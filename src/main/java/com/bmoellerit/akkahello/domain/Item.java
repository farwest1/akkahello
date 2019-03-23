package com.bmoellerit.akkahello.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

/**
 * Created by Bernd on 23.03.2019.
 *
 * Package com.bmoellerit.akkahello.domain
 */
public class Item {

  private UUID id;
  private String name;

  @JsonCreator
  public Item(@JsonProperty("id") UUID id,@JsonProperty("name") String name) {
    this.id = id;
    this.name = name;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
