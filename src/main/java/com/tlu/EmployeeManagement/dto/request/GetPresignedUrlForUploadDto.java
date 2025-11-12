package com.tlu.EmployeeManagement.dto.request;

import com.tlu.EmployeeManagement.enums.UploadFolderType;

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

  Integer userId;

  UploadFolderType folderType;

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String fileName;
    private Integer userId;
    private UploadFolderType folderType;

    public Builder fileName(String fileName) {
      this.fileName = fileName;
      return this;
    }

    public Builder userId(Integer userId) {
      this.userId = userId;
      return this;
    }

    public Builder folderType(UploadFolderType folderType) {
      this.folderType = folderType;
      return this;
    }

    public GetPresignedUrlForUploadDto build() {
      GetPresignedUrlForUploadDto dto = new GetPresignedUrlForUploadDto();
      dto.fileName = this.fileName;
      dto.userId = this.userId;
      dto.folderType = this.folderType;
      return dto;
    }
  }
}
