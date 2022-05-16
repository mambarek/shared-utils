package com.it2go.util.jpa.search;


public enum Operation {

  EQUAL("eq","equal"),
  NOT_EQUAL("ne","not equal"),
  LESS("lt","less"),
  LESS_OR_EQUAL("le","less or equal"),
  GREATER("gt","greater"),
  GREATER_OR_EQUAL("ge","greater or equal"),
  BEGINS_WITH("bw","begins with"),
  NOT_BEGIN_WITH("bn","does not begin with"),
  IN("in","is in"),
  NOT_IN("ni","is not in"),
  ENDS_WITH("ew","ends with"),
  NOT_ENDS_WITH("en","does not end with"),
  CONTAINS("cn","contains"),
  NOT_CONTAINS("nc","does not contain"),
  NULL("nu","is null"),
  NOT_NULL("nn","is not null"),
  BETWEEN("bt","between");

  private final String id;
  private final String name;

  Operation(String _id){
    id = _id;
    name = "";
  };

  Operation(String _id, String _name){
    id = _id;
    name = _name;
  }

  public String getId(){
    return id;
  }
  public String getName(){
    return name;
  }
}
