package com.jtbdevelopment.e_eye_o.hibernate.DAO.misc;

import org.hibernate.dialect.MySQL5InnoDBDialect;

import java.sql.Types;

/**
 * Date: 5/11/13
 * Time: 12:28 AM
 * <p/>
 * Optional - not especially useful while mysql prepared statement processor still truncates
 */
public class MySQL5InnoFixes extends MySQL5InnoDBDialect {
    public MySQL5InnoFixes() {
        super();
        registerColumnType(Types.BOOLEAN, "bit");
    }
}
