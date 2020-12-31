package com.dashboard.eleonore.element.sonar.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SonarMetricDTO implements Serializable {

    private static final long serialVersionUID = 3678203658045976600L;

    private Long id;
    private String metric;
}
