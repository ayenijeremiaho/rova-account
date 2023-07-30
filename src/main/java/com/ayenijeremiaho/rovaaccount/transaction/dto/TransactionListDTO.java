package com.ayenijeremiaho.rovaaccount.transaction.dto;

import com.ayenijeremiaho.rovaaccount.transaction.dto.other.PageableResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransactionListDTO extends PageableResponseDTO {

    private List<TransactionDTO> transactions;
}
