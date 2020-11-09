package com.dashboard.eleonore.element.actuator.dto;

import com.dashboard.eleonore.element.dto.ElementDTO;
import com.dashboard.eleonore.element.actuator.repository.entity.Actuator;
import com.dashboard.eleonore.element.repository.entity.ElementType;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.stream.Collectors;

public class ActuatorDTO extends ElementDTO {
    private static final long serialVersionUID = -8951788847806501051L;

    private String url;
    private Set<ActuatorMetricDTO> actuatorMetrics;

    public ActuatorDTO() {
        super(null);
    }

    public ActuatorDTO(Actuator actuator) {
        super(actuator.getId());
        this.url = actuator.getUrl();
        if (!CollectionUtils.isEmpty(actuator.getMetrics())) {
            this.actuatorMetrics = actuator.getMetrics().stream().map(ActuatorMetricDTO::new).collect(Collectors.toSet());
        }
    }

    @Override
    public ElementType getType() {
        return ElementType.ACTUATOR;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<ActuatorMetricDTO> getActuatorMetrics() {
        return actuatorMetrics;
    }

    public void setActuatorMetrics(Set<ActuatorMetricDTO> actuatorMetrics) {
        this.actuatorMetrics = actuatorMetrics;
    }
}
