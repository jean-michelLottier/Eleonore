package com.dashboard.eleonore.element.repository.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dashboard_id", nullable = false)
    private Long dashboardId;

    @Column(name = "element_id", nullable = false)
    private Long elementId;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ElementType type;
}
