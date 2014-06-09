package com.jtbdevelopment.e_eye_o.hibernate.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Date: 4/6/13
 * Time: 12:39 AM
 */
@Entity(name = "PersistentToken")
public class HibernatePersistentToken {
    private String username;
    private String series;
    private String token;
    private Date timestamp;

    @Id
    @Column(nullable = false, length = 64)
    public String getSeries() {
        return series;
    }

    public void setSeries(final String series) {
        this.series = series;
    }

    @Column(nullable = false)
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

    @Column(nullable = false, length = 64)
    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    @Column(nullable = false, length = 64)
    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }
}
