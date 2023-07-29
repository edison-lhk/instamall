package com.example.b07_project_group5;

public interface LoginContract {
    interface View {
        String getStringFromResource(int resourceId);
        void displayAccountTypeDropdown();
        String getEmailInput();
        String getPasswordInput();
        String getAccountTypeInput();
        void setWarningText(String warning);
        void clearInputFields();
        void showToastMessage();
        void navigateToStoreActivity(String userId, String storeId);
        void navigateToBrowseStoreActivity(String userId);
        void navigateToRegisterActivity(android.view.View v);
        void loginUser(android.view.View v);
    }

    interface Presenter {
        void loginUser(String email, String password, String accountType);
    }

    interface Model {
        void loginUser(String email, String password, String accountType, LoginModel.LoginCallback callback);
    }
}
