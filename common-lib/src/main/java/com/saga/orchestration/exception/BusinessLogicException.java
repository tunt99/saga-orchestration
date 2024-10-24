package com.saga.orchestration.exception;

import com.saga.orchestration.constants.enums.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BusinessLogicException extends RuntimeException {

    private ErrorCode errorCode;
    private Object[] params;

    public BusinessLogicException(String message) {
        super(message);
    }

    public BusinessLogicException(ErrorCode errorCode, Object... params){
        super(errorCode.getMessageCode());
        this.errorCode = errorCode;
        this.params = params;

    }
}
