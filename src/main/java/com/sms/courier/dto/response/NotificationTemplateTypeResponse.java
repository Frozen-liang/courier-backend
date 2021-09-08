package com.sms.courier.dto.response;

import com.sms.courier.chat.modal.TemplateField;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationTemplateTypeResponse extends IdNameResponse<Integer> {

    private List<TemplateField> fields;
}