package com.it2go.util.jpa.search;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SearchFilter<T,Y> {

  private final Class<T> itemType;
  private final Class<Y> rootType;

  public List<T> search(EntityManager em, CompoundSelection<T> compoundSelection, SearchTemplate searchTemplate){
    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<T> criteriaQuery = cb.createQuery(itemType);
    Root<Y> employeeRoot = criteriaQuery.from(rootType);

    final CriteriaQuery<T> select = criteriaQuery.select(compoundSelection);

    PredicateBuilder predicateBuilder = null;
    if(searchTemplate.getFilters() != null) {
      predicateBuilder = PredicateBuilder
          .createPredicates(cb, employeeRoot, searchTemplate.getFilters());

      select.where(predicateBuilder.getPredicates().toArray(new Predicate[0]));
    }

    // Order by
    Order orderBy = null;

    if (searchTemplate.getOrderBy() != null && !searchTemplate.getOrderBy()
        .isEmpty()) {
      switch (searchTemplate.getOrderDirection()) {
        case "asc":
          orderBy = cb.asc(employeeRoot.get(searchTemplate.getOrderBy()));
          break;
        case "desc":
          orderBy = cb.desc(employeeRoot.get(searchTemplate.getOrderBy()));
          break;
      }
    }

    if (orderBy != null) {
      select.orderBy(orderBy);
    }

    final TypedQuery<T> query = em.createQuery(select);

    // set query parameter if exists
    if(predicateBuilder != null) {
      predicateBuilder.getParamMap().forEach(query::setParameter);
    }

    if (searchTemplate.getMaxResult() > 0) {
      query.setMaxResults(searchTemplate.getMaxResult());
    }

    query.setFirstResult(searchTemplate.getOffset());

    final List<T> resultList = query.getResultList();
    System.out.println("resultList = " + resultList.size());

    return resultList;
  }
}
