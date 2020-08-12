package com.it2go.util.jpa.search;

import java.util.List;
import lombok.Data;

@Data
public class SearchResult<T> {

  private int total;
  private int page;
  private Long records;
  private List<T> rows;
  private Object userdata;
}
