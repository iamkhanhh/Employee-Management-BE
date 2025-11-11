package com.tlu.EmployeeManagement.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetPresignedUrlForUploadDto {
  String fileName;

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String fileName;

    public Builder fileName(String fileName) {
      this.fileName = fileName;
      return this;
    }

    public GetPresignedUrlForUploadDto build() {
      GetPresignedUrlForUploadDto dto = new GetPresignedUrlForUploadDto();
      dto.fileName = this.fileName;
      return dto;
    }
  }
}
