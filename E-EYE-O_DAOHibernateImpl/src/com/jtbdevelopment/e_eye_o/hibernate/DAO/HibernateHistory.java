package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.hibernate.entities.impl.HibernateAppUser;
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Date: 7/29/13
 * Time: 1:32 AM
 */
@Entity(name = "HistoricalFeed")
public class HibernateHistory {
    private String id;
    private AppUser appUser;
    private DateTime modificationTimestamp;
    private String serializedVersion;

    public HibernateHistory() {

    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof HibernateHistory) && id.equals(((HibernateHistory) o).getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }


    @ManyToOne(targetEntity = HibernateAppUser.class, optional = false)
    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(final AppUser appUser) {
        this.appUser = appUser;
    }

    //
    //  Unfortunately, despite adding sub-second support in MySQL in 5.6.4
    //  prepared statements via hibernate are still truncated in the driver
    //  as of version 5.1.24
    //
    //  To manage maximum flexibility, storing modificationTimestamp as instant for now
    //
    @Transient
    public DateTime getModificationTimestamp() {
        return modificationTimestamp;
    }

    public void setModificationTimestamp(final DateTime modificationTimestamp) {
        this.modificationTimestamp = modificationTimestamp;
    }

    @Column(name = "modificationTimestamp", nullable = false)
    @SuppressWarnings("unused")
    private long getModificationTimestampInstant() {
        return modificationTimestamp.getMillis();
    }

    @SuppressWarnings("unused")
    private void setModificationTimestampInstant(final long instant) {
        modificationTimestamp = new DateTime(instant);
    }

    @Column(nullable = false)
    @Lob
    public String getSerializedVersion() {
        return serializedVersion;
    }

    public void setSerializedVersion(final String serializedVersion) {
        this.serializedVersion = serializedVersion;
    }
}
