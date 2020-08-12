package com.it2go.util.jpa.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Group implements Serializable {

  private GroupOperation groupOp;
  private List<Rule> rules = new ArrayList<>();
  private List<Group> groups = new ArrayList<>();
}
