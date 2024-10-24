package com.saga.orchestration.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<CardDetailInfo> cardDetails;
    private List<AddressInfo> addresses;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardDetailInfo {
        private String cardId;
        private String name;
        private String cardNumber;
        private Integer validUntilMonth;
        private Integer validUntilYear;
        private Integer cvv;
        private String userId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressInfo {
        private String addressId;
        private String city;
        private String district;
        private String street;
        private String userId;
    }
}
