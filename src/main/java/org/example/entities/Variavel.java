package org.example.entities;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
@Data
@Builder
public class Variavel implements Serializable {
    private String tag;
    private String name;
    private String mfgName;
    private String plataforma;
}
