package com.sms.courier.common.field;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

public interface Field {

    String getName();

    List<String> charArr = List.of("\\", "(", ")", ".", "[", "?", "^", "{", "}", "|");


    default Optional<Criteria> is(Object value) {
        return Objects.nonNull(value) ? Optional.of(Criteria.where(getName()).is(value)) : Optional.empty();
    }

    default Optional<Criteria> is(String value) {
        return StringUtils.isNotBlank(value) ? Optional.of(Criteria.where(getName()).is(value)) : Optional.empty();
    }

    default Optional<Criteria> ne(Object value) {
        return Objects.nonNull(value) ? Optional.of(Criteria.where(getName()).ne(value)) : Optional.empty();
    }

    default Optional<Criteria> ne(String value) {
        return StringUtils.isNotBlank(value) ? Optional.of(Criteria.where(getName()).ne(value)) : Optional.empty();
    }

    default Optional<Criteria> in(Collection<?> values) {
        return Objects.isNull(values) || values.isEmpty() ? Optional.empty()
            : Optional.of(Criteria.where(getName()).in(values));
    }

    default Optional<Criteria> nin(Collection<?> values) {
        return Objects.isNull(values) || values.isEmpty() ? Optional.empty()
            : Optional.of(Criteria.where(getName()).nin(values));
    }

    default Optional<Criteria> lte(Object value) {
        return Objects.isNull(value) ? Optional.empty()
            : Optional.of(Criteria.where(getName()).lte(value));
    }

    default Optional<Criteria> gte(Object value) {
        return Objects.isNull(value) ? Optional.empty()
            : Optional.of(Criteria.where(getName()).gte(value));
    }

    default Optional<Criteria> gt(Object value) {
        return Objects.isNull(value) ? Optional.empty()
            : Optional.of(Criteria.where(getName()).gt(value));
    }

    default Optional<Criteria> lteAndGte(Object ltValue, Object gtValue) {
        if (Objects.isNull(ltValue) && Objects.isNull(gtValue)) {
            return Optional.empty();
        }
        if (Objects.nonNull(ltValue) && Objects.nonNull(gtValue)) {
            return Optional.of(Criteria.where(getName()).gte(ltValue).lte(gtValue));
        }
        if (Objects.nonNull(ltValue)) {
            return Optional.of(Criteria.where(getName()).gte(ltValue));
        }
        return Optional.of(Criteria.where(getName()).lte(gtValue));
    }

    default Optional<Criteria> exists(boolean isExists) {
        return Optional.of(Criteria.where(getName()).exists(isExists));
    }

    default Optional<Criteria> like(String value) {
        if (Objects.isNull(value)) {
            return Optional.empty();
        }
        Pattern pattern = Pattern.compile("^.*" + converterSpecialChar(value) + ".*$", Pattern.CASE_INSENSITIVE);
        return Optional.of(Criteria.where(getName()).regex(pattern));
    }

    private String converterSpecialChar(String value) {
        for (String key : charArr) {
            if (value.contains(key)) {
                value = value.replace(key, "\\" + key);
            }
        }
        return value;
    }
}
