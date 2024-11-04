package com.damoa.dto.admin;

import com.damoa.repository.model.Faq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaqSaveDTO {

    private int id;
    private int type;
    private String title;
    private String content;
    private Timestamp createdAt;


    public Faq toFaq(){
        return Faq.builder()
                .type(this.type)
                .title(this.title)
                .content(this.content)
                .build();
    }


}
