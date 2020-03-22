package com.wirvsvirus.backend.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Option {
    private String name;
    private Integer next;
    private String color;
}
