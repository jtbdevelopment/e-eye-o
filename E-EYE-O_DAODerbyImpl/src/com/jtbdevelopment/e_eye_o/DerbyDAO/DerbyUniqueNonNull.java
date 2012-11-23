package com.jtbdevelopment.e_eye_o.DerbyDAO;

import org.hibernate.dialect.DerbyTenSevenDialect;

/**
* Date: 11/21/12
* Time: 9:18 PM
*/
public class DerbyUniqueNonNull extends DerbyTenSevenDialect {
    @Override
    public boolean supportsNotNullUnique() {
        return true;
    }
}
