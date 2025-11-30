package com.tlu.EmployeeManagement.dto.request;

import com.tlu.EmployeeManagement.enums.UploadFolderType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Data transfer object for requesting a presigned URL for file upload to S3")
public class GetPresignedUrlForUploadDto {

  @NotBlank(message = "File name is required")
  @Schema(
    description = "Name of the file to be uploaded (including extension)",
    example = "profile-picture.jpg",
    required = true
  )
  String fileName;

  @NotNull(message = "User ID is required")
  @Schema(
    description = "ID of the user uploading the file",
    example = "1",
    required = true
  )
  Integer userId;

  @NotNull(message = "Folder type is required")
  @Schema(
    description = "Type of folder/directory where the file should be stored in S3",
    example = "PROFILE",
    required = true
  )
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
