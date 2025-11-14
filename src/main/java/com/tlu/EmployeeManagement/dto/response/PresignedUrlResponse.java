package com.tlu.EmployeeManagement.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PresignedUrlResponse {
    String presignedUrl;
    String objectKey;
    String contentType;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String presignedUrl;
        private String objectKey;
        private String contentType;

        public Builder presignedUrl(String presignedUrl) {
            this.presignedUrl = presignedUrl;
            return this;
        }

        public Builder objectKey(String objectKey) {
            this.objectKey = objectKey;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public PresignedUrlResponse build() {
            PresignedUrlResponse response = new PresignedUrlResponse();
            response.presignedUrl = this.presignedUrl;
            response.objectKey = this.objectKey;
            response.contentType = this.contentType;
            return response;
        }
    }
}
