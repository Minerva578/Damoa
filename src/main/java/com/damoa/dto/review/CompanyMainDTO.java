package com.damoa.dto.review;

import com.damoa.repository.model.CompanyReview;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CompanyMainDTO extends CompanyReview {

    private String freelancerName;
}
