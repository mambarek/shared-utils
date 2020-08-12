package com.it2go.util.jpa.search;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchOrder {
  DESC("desc"),
  ASC("asc");

  private final String value;

}
