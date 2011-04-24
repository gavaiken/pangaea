package aiken.gav.appengine.pangaea.client;

import java.io.Serializable;

public class IdInUseException extends Exception implements Serializable {

  private String id;

  public IdInUseException() {
  }

  public IdInUseException(String symbol) {
    this.id = symbol;
  }

  public String getId() {
    return this.id;
  }
}