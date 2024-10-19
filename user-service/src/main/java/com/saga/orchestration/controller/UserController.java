package com.saga.orchestration.controller;

import com.saga.orchestration.model.UserInfo;
import com.saga.orchestration.queries.GetUserPaymentDetailsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private transient QueryGateway queryGateway;

    public UserController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("{userId}")
    public UserInfo getUserPaymentDetails(@PathVariable String userId){
        GetUserPaymentDetailsQuery getUserPaymentDetailsQuery
                = new GetUserPaymentDetailsQuery(userId);
        return queryGateway.query(getUserPaymentDetailsQuery,
                        ResponseTypes.instanceOf(UserInfo.class)).join();
    }
}
