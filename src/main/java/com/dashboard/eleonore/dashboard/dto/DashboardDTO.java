package com.dashboard.eleonore.dashboard.dto;

import com.dashboard.eleonore.element.dto.ElementDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class DashboardDTO {
    private Long id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<ElementDTO> elements;
}
