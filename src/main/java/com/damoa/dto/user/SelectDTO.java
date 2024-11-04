package com.damoa.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SelectDTO {

    String workType;
    String address;
    String progress;

    public static SelectDTO toSelectDTO(String workType, String address, String progress){
        SelectDTO newSelectDTO = new SelectDTO();
        newSelectDTO.workType = workType;
        newSelectDTO.address = address;
        newSelectDTO.progress = progress;
        return newSelectDTO;
    }
}
