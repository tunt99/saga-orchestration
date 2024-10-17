package com.saga.orchestration.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRestModel {
    private String userId;
    private String firstName;
    private String lastName;
    private CardDetails cardDetails;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardDetails {
        private String name;
        private String cardNumber;
        private Integer validUntilMonth;
        private Integer validUntilYear;
        private Integer cvv;
    }
}
