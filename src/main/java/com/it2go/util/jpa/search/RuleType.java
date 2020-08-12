package com.it2go.util.jpa.search;

public enum RuleType {
  STRING,
  NUMBER,
  DATE,
  TIMESTAMP;

  public boolean equalsString(String value){
    if(value == null) return false;
    return this.name().equalsIgnoreCase(value) ;
  }
}
