package com.example.demoo.enumeratation;

import static com.example.demoo.constants.Authority.*;

public enum Role {
    ROLE_CUSTOMER(CUSTOMER_AUTHORITIES),
    ROLE_AGENCY(AGENCY_AUTHORITIES);
    private String[] authorities;
    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }


}
