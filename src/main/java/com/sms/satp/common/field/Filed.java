package com.sms.satp.common.field;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

public interface Filed {

    String getFiled();

    default Optional<Criteria> is(Object value) {
        return Objects.nonNull(value) ? Optional.of(Criteria.where(getFiled()).is(value)) : Optional.empty();
    }

    default Optional<Criteria> is(String value) {
        return StringUtils.isNotEmpty(value) ? Optional.of(Criteria.where(getFiled()).is(value)) : Optional.empty();
    }

    default Optional<Criteria> in(Collection<?> values) {
        return Objects.isNull(values) || values.isEmpty() ? Optional.empty()
            : Optional.of(Criteria.where(getFiled()).in(values));
    }

    default Optional<Criteria> nin(Collection<?> values) {
        return Objects.isNull(values) || values.isEmpty() ? Optional.empty()
            : Optional.of(Criteria.where(getFiled()).nin(values));
    }

    default Optional<Criteria> lte(Object value) {
        return Objects.isNull(value) ? Optional.empty()
            : Optional.of(Criteria.where(getFiled()).lte(value));
    }

    default Optional<Criteria> gte(Object value) {
        return Objects.isNull(value) ? Optional.empty()
            : Optional.of(Criteria.where(getFiled()).gte(value));
    }

    default Optional<Criteria> lteAndGte(Object ltValue, Object gtValue) {
        if (Objects.isNull(ltValue) && Objects.isNull(gtValue)) {
            return Optional.empty();
        }
        if (Objects.nonNull(ltValue) && Objects.nonNull(gtValue)) {
            return Optional.of(Criteria.where(getFiled()).gte(ltValue).lte(gtValue));
        }
        if (Objects.nonNull(ltValue)) {
            return Optional.of(Criteria.where(getFiled()).gte(ltValue));
        }
        return Optional.of(Criteria.where(getFiled()).lte(gtValue));
    }
}
