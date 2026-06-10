package com.example.project.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "courts")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private String description;
    private Double price;
    @OneToMany(mappedBy = "court", fetch = FetchType.LAZY)
    private List<CourtImage> images;
    private Boolean enabled;
}
