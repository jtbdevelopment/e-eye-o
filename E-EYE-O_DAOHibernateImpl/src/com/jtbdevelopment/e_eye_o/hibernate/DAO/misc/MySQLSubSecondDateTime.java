package com.jtbdevelopment.e_eye_o.hibernate.DAO.misc;

import org.hibernate.dialect.MySQL5InnoDBDialect;

import java.sql.Types;

/**
 * Date: 5/11/13
 * Time: 12:28 AM
 * <p/>
 * Optional - not especially useful while mysql prepared statement processor still truncates
 */
public class MySQLSubSecondDateTime extends MySQL5InnoDBDialect {
    public MySQLSubSecondDateTime() {
        super();
        registerColumnType(Types.TIMESTAMP, "timestamp(6)");
    }

    public String getCurrentTimestampSelectString() {
        return "select now(6)";
    }
}
