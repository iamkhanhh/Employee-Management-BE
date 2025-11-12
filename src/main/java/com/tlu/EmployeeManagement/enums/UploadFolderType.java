package com.tlu.EmployeeManagement.enums;

public enum UploadFolderType {
    DOCUMENTS("documents"),
    CONTRACTS("contracts"),
    PAYROLLS("payrolls");

    private final String folderName;

    UploadFolderType(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    @Override
    public String toString() {
        return folderName;
    }
}
