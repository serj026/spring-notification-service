package com.serj026.services.notification.websocket;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.security.Principal;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class User implements Principal {

    private final long userId;
    private final long sessionId;
    private final long createdTs;

    @JsonCreator
    public User(@JsonProperty("userId") long userId,
                @JsonProperty("sessionId") long sessionId,
                @JsonProperty("createdTs") long createdTs) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.createdTs = createdTs;
    }

    public long getUserId() {
        return userId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public long getCreatedTs() {
        return createdTs;
    }

    @Override
    public String getName() {
        return userId + "_" + sessionId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User rhs = (User) obj;
        return new EqualsBuilder()
                .append(userId, rhs.userId)
                .append(sessionId, rhs.sessionId)
                .append(createdTs, rhs.createdTs)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(userId)
                .append(sessionId)
                .append(createdTs)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("userId", userId)
                .append("sessionId", sessionId)
                .append("createdTs", createdTs)
                .toString();
    }
}