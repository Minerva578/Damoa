package com.damoa.dto.user;

import com.damoa.repository.model.ProjectWait;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectWaitDTO {
    private int projectId;
    private int userId;
    private int freelancerId;
    private String freelancerName;
}


