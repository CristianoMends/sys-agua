package edu.pies.sysaguaapp.models;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Estoque {
    private Long id;
    private int initialQuantity;
    private int totalEntries;
    private int totalWithdrawals;
    private int currentQuantity;
    private String createdAt;
    private String updatedAt;
    private Produto product;
}
