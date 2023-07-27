package com.example.b07_project_group5;

public interface LoginView {
    void setWarningText(String warning);
    void clearInputFields();
    void showToastMessage();
    void navigateToStoreActivity(String userId, String accountType, String storeId);
    void navigateToBrowseStoreActivity(String userId);
    String getStringFromResource(int resourceId);
}
