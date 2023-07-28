package com.example.b07_project_group5;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private LoginContract.Model model;
    public LoginPresenter(LoginContract.View view, LoginContract.Model model) {
        this.view = view;
        this.model = model;
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
                view.showToastMessage();

                if (accountType.equals("owner")) {
                    view.navigateToStoreActivity(userId, storeId);
                } else {
                    view.navigateToBrowseStoreActivity(userId);
                }
            }

            @Override
            public void onLoginFailure(int warningId) {
                view.setWarningText(view.getStringFromResource(warningId));
            }
        });
    }
}
