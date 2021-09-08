package com.sms.courier.dto.response;

import com.sms.courier.chat.modal.TemplateField;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationTemplateTypeResponse extends IdNameResponse<Integer> {

    private List<TemplateField> fields;
}