package com.it2go.util.jpa.search;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.hibernate.criterion.MatchMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class PredicateBuilder {

  private static final Logger logger = LoggerFactory.getLogger(
      PredicateBuilder.class);

  private static char ESCAPE_CHAR = '!';

  private static String escape(String value) {
    return value
        .replace("!", ESCAPE_CHAR + "!")
        .replace("%", ESCAPE_CHAR + "%")
        .replace("_", ESCAPE_CHAR + "_");
  }

  private Map<String, Object> paramMap = new HashMap<>();
  private List<Predicate> predicates = new ArrayList<>();

  public static <T> PredicateBuilder createPredicates(CriteriaBuilder cb, Root<T> root,
      Group filterGroup) {
    PredicateBuilder pb = new PredicateBuilder();
    Predicate groupPredicate = null;
    List<Predicate> rulesPredicates = new ArrayList<>();
    for (Rule rule : filterGroup.getRules()) {
      Predicate rulePredicate = null;

      // unique operator needs no value
      switch (rule.getOp()) {
        case NULL: {
          rulePredicate = cb.isNull(root.get(rule.getField()));
          break;
        }
        case NOT_NULL: {
          rulePredicate = cb.isNotNull(root.get(rule.getField()));
          break;
        }
      }

      if (rule.getData() != null && !rule.getData().isEmpty()) {

        switch (rule.getType()) {
          case DATE: {
            ParameterExpression<LocalDate> namedParameter = cb.parameter(LocalDate.class,
                rule.getField());
            Expression<LocalDate> path = root.get(rule.getField());
            try {
              LocalDate localDate = LocalDate.parse(rule.getData());
              pb.paramMap.put(rule.getField(), localDate);
              rulePredicate = getPredicate(rule, cb, path, namedParameter, pb);
            } catch (Exception e) {
              logger.error(e.getMessage());
            }
            break;
          }
          case TIMESTAMP: {
            ParameterExpression<OffsetDateTime> namedParameter = cb.parameter(
                OffsetDateTime.class, rule.getField());
            Expression<OffsetDateTime> path = root.get(rule.getField());
            try {
              OffsetDateTime dateTime = OffsetDateTime.parse(rule.getData());
              pb.paramMap.put(rule.getField(), dateTime);
              rulePredicate = getPredicate(rule, cb, path, namedParameter, pb);
            } catch (Exception e) {
              logger.error(e.getMessage());
            }
            break;
          }
          case NUMBER: {
            ParameterExpression<Float> namedParameter = cb.parameter(Float.class,
                rule.getField());
            Expression<Float> path = root.get(rule.getField());
            try {
              Number number = Double.parseDouble(rule.getData());
              pb.paramMap.put(rule.getField(), number);
              rulePredicate = getPredicate(rule, cb, path, namedParameter, pb);
            } catch (Exception e) {
              logger.error(e.getMessage());
            }
            break;
          }
          default: {
            ParameterExpression<String> namedParameter = cb.parameter(String.class,
                rule.getField());
            Expression<String> path = root.get(rule.getField());
            pb.paramMap.put(rule.getField(), rule.getData());
            rulePredicate = getStringPredicate(rule, cb, path, namedParameter, pb);
            break;
          }
        }

      }

      if (rulePredicate != null) {
        rulesPredicates.add(rulePredicate);
      }
    }

    if(rulesPredicates.size() > 1) {
      switch (filterGroup.getGroupOp()) {
        case AND:
          groupPredicate = cb.and(rulesPredicates.toArray(new Predicate[0]));
          break;
        case OR:
          groupPredicate = cb.or(rulesPredicates.toArray(new Predicate[0]));
          break;
      }
      pb.getPredicates().add(groupPredicate);
    }

    return pb;
  }

  private static <Y extends Comparable<? super Y>> Predicate getPredicate(
      Rule rule,
      CriteriaBuilder cb, Expression<Y> path, ParameterExpression<Y> namedParameter,
      PredicateBuilder pb) {
    if (rule == null || rule.getOp() == null) {
      return null;
    }

    Predicate rulePredicate = null;

    switch (rule.getOp()) {
      case EQUAL: {
        rulePredicate = cb.equal(path, namedParameter);
        break;
      }
      case NOT_EQUAL: {
        rulePredicate = cb.notEqual(path, namedParameter);
        break;
      }
      case LESS: {
        rulePredicate = cb.lessThan(path, namedParameter);
        break;
      }
      case LESS_OR_EQUAL: {
        rulePredicate = cb.lessThanOrEqualTo(path, namedParameter);
        break;
      }
      case GREATER: {
        rulePredicate = cb.greaterThan(path, namedParameter);
        break;
      }
      case GREATER_OR_EQUAL: {
        rulePredicate = cb.greaterThanOrEqualTo(path, namedParameter);
        break;
      }
      case BETWEEN: {
        break;
      }
    }

    return rulePredicate;
  }

  private static Predicate getStringPredicate(Rule rule, CriteriaBuilder cb,
      Expression<String> path, ParameterExpression<String> namedParameter, PredicateBuilder pb) {
    if (rule == null || rule.getOp() == null) {
      return null;
    }

    String escapedValue = escape(rule.getData());
    Predicate rulePredicate = null;

    switch (rule.getOp()) {
      case EQUAL: {
        rulePredicate = cb.equal(path, namedParameter);
        break;
      }
      case NOT_EQUAL: {
        rulePredicate = cb.notEqual(path, namedParameter);
        break;
      }
      case LESS: {
        rulePredicate = cb.lessThan(path, namedParameter);
        break;
      }
      case LESS_OR_EQUAL: {
        rulePredicate = cb.lessThanOrEqualTo(path, namedParameter);
        break;
      }
      case GREATER: {
        rulePredicate = cb.greaterThan(path, namedParameter);
        break;
      }
      case GREATER_OR_EQUAL: {
        rulePredicate = cb.greaterThanOrEqualTo(path, namedParameter);
        break;
      }
      case CONTAINS: {
        pb.paramMap.put(rule.getField(), MatchMode.ANYWHERE.toMatchString(escapedValue));
        rulePredicate = cb.like(path, namedParameter, ESCAPE_CHAR);
        break;
      }
      case NOT_CONTAINS: {
        pb.paramMap.put(rule.getField(), MatchMode.ANYWHERE.toMatchString(escapedValue));
        rulePredicate = cb.notLike(path, namedParameter, ESCAPE_CHAR);
        break;
      }
      case ENDS_WITH: {
        pb.paramMap.put(rule.getField(), MatchMode.END.toMatchString(escapedValue));
        rulePredicate = cb.like(path, namedParameter, ESCAPE_CHAR);
        break;
      }
      case NOT_ENDS_WITH: {
        pb.paramMap.put(rule.getField(), MatchMode.END.toMatchString(escapedValue));
        rulePredicate = cb.notLike(path, namedParameter, ESCAPE_CHAR);
        break;
      }
      case BEGINS_WITH: {
        pb.paramMap.put(rule.getField(), MatchMode.START.toMatchString(escapedValue));
        rulePredicate = cb.like(path, namedParameter, ESCAPE_CHAR);
        break;
      }
      case NOT_BEGIN_WITH: {
        pb.paramMap.put(rule.getField(), MatchMode.START.toMatchString(escapedValue));
        rulePredicate = cb.notLike(path, namedParameter, ESCAPE_CHAR);
        break;
      }
      case IN: {
        final Expression<Integer> locate = cb.locate(namedParameter, path);
        rulePredicate = cb.gt(locate, 0);
        break;
      }
      case NOT_IN: {
        final Expression<Integer> locate = cb.locate(namedParameter, path);
        rulePredicate = cb.equal(locate, 0);
        break;
      }
      case BETWEEN: {
        break;
      }
    }

    return rulePredicate;
  }
}
