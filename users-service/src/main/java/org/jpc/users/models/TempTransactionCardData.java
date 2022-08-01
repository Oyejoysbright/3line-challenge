package org.jpc.users.models;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class TempTransactionCardData {
    private Integer transactionId;
    private boolean saveCard = false;

    public TempTransactionCardData(Object transactionId, boolean saveCard) {
        this.transactionId = (Integer) transactionId;
        this.saveCard = saveCard;
    }
}
