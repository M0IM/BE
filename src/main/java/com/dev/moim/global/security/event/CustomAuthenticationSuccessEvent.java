package com.dev.moim.global.security.event;

import com.dev.moim.global.security.principal.PrincipalDetails;
import org.springframework.context.ApplicationEvent;

public class CustomAuthenticationSuccessEvent extends ApplicationEvent {
    private final PrincipalDetails principalDetails;
    private final String fcmToken;

    public CustomAuthenticationSuccessEvent(PrincipalDetails principalDetails,  String fcmToken) {
        super(principalDetails);
        this.principalDetails = principalDetails;
        this.fcmToken = fcmToken;
    }

    public PrincipalDetails getPrincipalDetails() {
        return principalDetails;
    }

    public String getFcmToken() {
        return fcmToken;
    }
}

