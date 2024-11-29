package com.jspider.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jspider.Enums.PresentationStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Presentation  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pid;

    private String course;
    private String topic;

    @Enumerated(EnumType.STRING)
    private PresentationStatus presentationStatus;

    private Double userTotalScore;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "presentation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ratings> ratings;

}
