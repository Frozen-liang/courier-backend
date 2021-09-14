package com.sms.courier.initialize.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Convert<T> {

    private String className;
    private List<T> data;
}
