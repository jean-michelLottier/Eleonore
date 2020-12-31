package com.dashboard.eleonore.element.sonar.dto;

import com.dashboard.eleonore.element.dto.ElementDTO;
import com.dashboard.eleonore.element.repository.entity.ElementType;
import com.dashboard.eleonore.element.sonar.repository.entity.Sonar;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class SonarDTO extends ElementDTO implements Serializable {

    private static final long serialVersionUID = 483528404395832976L;

    private String url;
    private String projectName;
    private String projectKey;
    private Set<SonarMetricDTO> sonarMetrics;

    public SonarDTO() {
        super(null);
    }

    @Override
    public ElementType getType() {
        return ElementType.SONAR;
    }
}
