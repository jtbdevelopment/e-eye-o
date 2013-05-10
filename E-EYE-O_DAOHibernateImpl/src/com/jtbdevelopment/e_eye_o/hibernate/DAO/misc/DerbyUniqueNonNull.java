package com.jtbdevelopment.e_eye_o.hibernate.DAO.misc;

import org.hibernate.dialect.DerbyTenSevenDialect;

/**
 * Date: 11/21/12
 * Time: 9:18 PM
 * <p/>
 * Derby variant found necessary
 */
public class DerbyUniqueNonNull extends DerbyTenSevenDialect {
    @Override
    public boolean supportsNotNullUnique() {
        return true;
    }
}
