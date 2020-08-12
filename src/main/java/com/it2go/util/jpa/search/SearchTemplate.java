package com.it2go.util.jpa.search;

import java.io.Serializable;
import lombok.Data;

@Data
public class SearchTemplate implements Serializable {

  private String orderBy;
  private String orderDirection = SearchOrder.ASC.getValue();
  private int maxResult = -1;
  private int offset = 0;
  private Group filters;
}
