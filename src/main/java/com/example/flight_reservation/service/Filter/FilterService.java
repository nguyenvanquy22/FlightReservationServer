package com.example.flight_reservation.service.Filter;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FilterService<T>{

    public Specification<T> getSearchSpecification(List<SearchDetailRequest> searchDetailRequest, SearchRequest.GlobalOperator globalOperator) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            for(SearchDetailRequest requestDto : searchDetailRequest){

                switch (requestDto.getOperation()) {
                    case EQUAL -> {
                        Predicate equal = criteriaBuilder.equal(root.get(requestDto.getColumn()), requestDto.getValue());
                        predicates.add(equal);
                    }
                    case LIKE -> {
                        Predicate like = criteriaBuilder.like(root.get(requestDto.getColumn()), "%" + requestDto.getValue() + "%");
                        predicates.add(like);
                    }
                    case IN -> {
                        String[] split = requestDto.getValue().split(",");
                        Predicate in = root.get(requestDto.getColumn()).in(Arrays.asList(split));
                        predicates.add(in);
                    }
                    case GREATER_THAN -> {
                        Predicate greaterThan = criteriaBuilder.greaterThan(root.get(requestDto.getColumn()), requestDto.getValue());
                        predicates.add(greaterThan);
                    }
                    case LESS_THAN -> {
                        Predicate lessThan = criteriaBuilder.lessThan(root.get(requestDto.getColumn()), requestDto.getValue());
                        predicates.add(lessThan);
                    }
                    case BETWEEN -> {
                        String[] split1 = requestDto.getValue().split(",");
                        Predicate between = criteriaBuilder.between(root.get(requestDto.getColumn()), Long.parseLong(split1[0]), Long.parseLong(split1[1]));
                        predicates.add(between);
                    }
                    case JOIN -> {
                        Predicate join = criteriaBuilder.equal(root.join(requestDto.getJoinTable()).get(requestDto.getColumn()), requestDto.getValue());
                        predicates.add(join);
                    }
                    case COUNT -> {
                        assert query != null;
                        Subquery<Long> subQuery = query.subquery(Long.class);
                        Root<T> subRoot = (Root<T>) subQuery.from(root.getJavaType());
                        Join<Object, Object> subJoin = subRoot.join(requestDto.getJoinTable(), JoinType.LEFT);
                        subQuery.select(criteriaBuilder.count(subJoin))
                                .where(criteriaBuilder.equal(subRoot, root));
                        String[] countRange = requestDto.getValue().split(",");
                        int minCount = Integer.parseInt(countRange[0]);
                        int maxCount = Integer.parseInt(countRange[1]);
                        predicates.add(criteriaBuilder.between(subQuery, (long) minCount, (long) maxCount));
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + "");
                }

            }

            if(globalOperator.equals(SearchRequest.GlobalOperator.AND)) {
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }else{
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
