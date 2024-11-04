package com.damoa.repository.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Ad {

    private Integer id;
    private String title;
    private String originFileName;
    private Date startTime;
    private Date endTime;
    private int status;

    public String getFormattedStartTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일");
        return formatter.format(startTime);
    }

    public String getFormattedEndTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일");
        return formatter.format(endTime);
    }


    public String getStatusLabel() {
        return status == 1 ? "활성화" : "비활성화";
    }
}
