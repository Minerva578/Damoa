package com.damoa.dto.admin;

import com.damoa.repository.model.Ad;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class AdDTO {

    private String title;
    private String originFileName;
    private Date startTime;
    private Date endTime;
    private int status;

    public Ad toAd() {
        return Ad.builder()
                .title(this.title)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .status(this.status)
                .build();


    }




}
