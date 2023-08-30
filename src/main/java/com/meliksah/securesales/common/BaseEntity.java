package com.meliksah.securesales.common;

import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    public abstract Long getId();
}