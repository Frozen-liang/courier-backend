package com.sms.courier.chat.modal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    private String from;
    @Builder.Default
    private List<String> to = new ArrayList<>();
    @Builder.Default
    private List<String> cc = new ArrayList<>();
    private String subject;
    private String content;
    private Map<String, String> attachments;
    private Map<String, String> inlines;
}
