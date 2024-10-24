package com.saga.orchestration.services;

import com.saga.orchestration.models.UserInfo;
import com.saga.orchestration.queries.GetUserPaymentDetailsQuery;

public interface UserService {

    UserInfo getUserPaymentDetails(GetUserPaymentDetailsQuery query);
}
