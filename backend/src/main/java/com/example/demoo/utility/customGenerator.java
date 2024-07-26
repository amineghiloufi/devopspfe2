package com.example.demoo.utility;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

import static java.lang.String.*;

public class customGenerator implements IdentifierGenerator {
    private static final String REFERENCE_PREFIX = "#0";
    private static final int REFERENCE_LENGTH = 8;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        return REFERENCE_PREFIX + format("%0" + (REFERENCE_LENGTH - REFERENCE_PREFIX.length()) + "d", 1);
    }
}