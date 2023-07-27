package com.example.b07_project_group5;

public class LoginPresenter {
    private LoginView view;
    private LoginModel model;

    public LoginPresenter(LoginView view) {
        this.view = view;
        this.model = new LoginModel();
    }


    public void loginUser(String email, String password, String accountType) {
        if (email.isEmpty() || password.isEmpty() || accountType.isEmpty()) {
            view.setWarningText(view.getStringFromResource(R.string.login_empty_fields_warning));
            return;
        }
        model.loginUser(email, password, accountType.toLowerCase(), new LoginModel.LoginCallback() {
            @Override
            public void onLoginSuccess(String userId, String accountType, String storeId) {
                view.clearInputFields();
                view.setWarningText("");

                if (accountType.equals("owner")) {
                    view.navigateToStoreActivity(userId, accountType, storeId);
                } else {
                    view.navigateToBrowseStoreActivity(userId);
                }
            }

            @Override
            public void onLoginFailure(String error) {
                view.setWarningText(error);
            }
        });
    }
}
