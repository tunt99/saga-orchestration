package com.saga.orchestration.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saga.orchestration.entities.Address;
import com.saga.orchestration.entities.CardDetail;
import com.saga.orchestration.entities.User;
import com.saga.orchestration.exception.BaseResponseException;
import com.saga.orchestration.model.UserInfo;
import com.saga.orchestration.queries.GetUserPaymentDetailsQuery;
import com.saga.orchestration.repositories.AddressRepository;
import com.saga.orchestration.repositories.CardDetailRepository;
import com.saga.orchestration.repositories.UserRepository;
import com.saga.orchestration.services.UserService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CardDetailRepository cardDetailRepository;
    private final ObjectMapper objectMapper;

    @Override
    @QueryHandler
    public UserInfo getUserPaymentDetails(GetUserPaymentDetailsQuery query) {

        User user = userRepository.findById(query.getUserId())
                .orElseThrow(() -> new BaseResponseException("User not found!"));

        List<Address> addresses = addressRepository.findAll();
        List<CardDetail> cardDetails = cardDetailRepository.findAll();

        return UserInfo.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .addresses(objectMapper.convertValue(addresses, new TypeReference<>() {}))
                .cardDetails(objectMapper.convertValue(cardDetails, new TypeReference<>() {}))
                .build();
    }
}
