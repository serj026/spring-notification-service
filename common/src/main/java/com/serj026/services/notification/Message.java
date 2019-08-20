package com.serj026.services.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@JsonDeserialize(builder = Message.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Message {

    private final Long userId;
    private final String senderServiceId;
    private final String senderInstanceId;
    private final String type;
    private final String channelId;
    private final Object payload;
    private final long createdTs;

    private Message(Builder builder) {
        this.userId = builder.userId;
        this.senderServiceId = builder.senderServiceId;
        this.senderInstanceId = builder.senderInstanceId;
        this.type = builder.type;
        this.channelId = builder.channelId;
        this.payload = builder.payload;
        this.createdTs = builder.createdTs;
    }

    public Long getUserId() {
        return userId;
    }

    public String getSenderServiceId() {
        return senderServiceId;
    }

    public String getSenderInstanceId() {
        return senderInstanceId;
    }

    public String getType() {
        return type;
    }

    public String getChannelId() {
        return channelId;
    }

    public Object getPayload() {
        return payload;
    }

    public long getCreatedTs() {
        return createdTs;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Message rhs = (Message) obj;
        return new EqualsBuilder()
                .append(userId, rhs.userId)
                .append(senderServiceId, rhs.senderServiceId)
                .append(senderInstanceId, rhs.senderInstanceId)
                .append(type, rhs.type)
                .append(channelId, rhs.channelId)
                .append(payload, rhs.payload)
                .append(createdTs, rhs.createdTs)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(userId)
                .append(senderServiceId)
                .append(senderInstanceId)
                .append(type)
                .append(channelId)
                .append(payload)
                .append(createdTs)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("userId", userId)
                .append("senderServiceId", senderServiceId)
                .append("senderInstanceId", senderInstanceId)
                .append("type", type)
                .append("channelId", channelId)
                .append("payload", payload)
                .append("createdTs", createdTs)
                .toString();
    }

    @JsonPOJOBuilder(withPrefix = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        private Long userId;
        private String senderServiceId;
        private String senderInstanceId;
        private String type;
        private String channelId;
        private Object payload;
        private long createdTs;

        private Builder() {
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder senderServiceId(String senderServiceId) {
            this.senderServiceId = senderServiceId;
            return this;
        }

        public Builder senderInstanceId(String senderInstanceId) {
            this.senderInstanceId = senderInstanceId;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder channelId(String channelId) {
            this.channelId = channelId;
            return this;
        }

        public Builder payload(Object payload) {
            this.payload = payload;
            return this;
        }

        public Builder createdTs(long createdTs) {
            this.createdTs = createdTs;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}