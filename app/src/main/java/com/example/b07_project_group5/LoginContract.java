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
        void loginUser();
    }

    interface Model {
        void findUserWithEmail(String email, LoginContract.Model.findUserWithEmailCallback callback);
        void getUserIdByEmailAndAccountType(String email, String accountType, LoginContract.Model.getUserIdByEmailAndAccountTypeCallback callback);
        void checkUserPasswordIsCorrect(String userId, String password, LoginContract.Model.checkUserPasswordIsCorrectCallback callback);
        void getUsernameById(String userId, LoginContract.Model.getUsernameByIdCallback callback);
        void createStoreForOwner(String userId, String username, LoginContract.Model.createStoreForOwnerCallback callback);

        interface findUserWithEmailCallback {
            void onSuccess();
            void onFailure();
        }

        interface getUserIdByEmailAndAccountTypeCallback {
            void onSuccess(String userId);
            void onFailure();
        }

        interface checkUserPasswordIsCorrectCallback {
            void onSuccess();
            void onFailure();
        }

        interface getUsernameByIdCallback {
            void onSuccess(String username);
        }

        interface createStoreForOwnerCallback {
            void onSuccess(String storeId);
        }
    }
}
