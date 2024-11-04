package com.damoa.dto.review;

import com.damoa.repository.model.FreelancerReview;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FreelancerMainDTO extends FreelancerReview {
    private String companyName;
}