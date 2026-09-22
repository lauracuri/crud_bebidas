package com.bebidas.cadastro_bebidas.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "bebidas")
@Entity

public class Bebidas {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "marca")
    private String marca;

    @Column(name = "sabor")
    private String sabor;

}
