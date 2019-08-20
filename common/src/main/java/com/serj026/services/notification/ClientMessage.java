package com.serj026.services.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ClientMessage<T> {

    private final String type;
    private final String channelId;
    private final T payload;

    public ClientMessage(String type, String channelId, T payload) {
        this.type = type;
        this.channelId = channelId;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public String getChannelId() {
        return channelId;
    }

    public T getPayload() {
        return payload;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ClientMessage rhs = (ClientMessage) obj;
        return new EqualsBuilder()
                .append(type, rhs.type)
                .append(payload, rhs.payload)
                .append(channelId, rhs.channelId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(type)
                .append(payload)
                .append(channelId)
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("type", type)
                .append("payload", payload)
                .append("channelId", channelId)
                .toString();
    }
}