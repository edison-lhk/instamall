package com.example.b07_project_group5;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private LoginContract.Model model;
    public LoginPresenter(LoginContract.View view, LoginContract.Model model) {
        this.view = view;
        this.model = model;
    }

    public void loginUser() {
        String email = view.getEmailInput();
        String password = view.getPasswordInput();
        String accountType = view.getAccountTypeInput();

        if (email.isEmpty() || password.isEmpty() || accountType.isEmpty()) {
            view.setWarningText(view.getStringFromResource(R.string.login_empty_fields_warning));
            return;
        }

        model.findUserWithEmail(email, new LoginContract.Model.findUserWithEmailCallback() {
            @Override
            public void onSuccess() {
                model.getUserIdByEmailAndAccountType(email, accountType, new LoginContract.Model.getUserIdByEmailAndAccountTypeCallback() {
                    @Override
                    public void onSuccess(String userId) {
                        model.checkUserPasswordIsCorrect(userId, password, new LoginContract.Model.checkUserPasswordIsCorrectCallback() {
                            @Override
                            public void onSuccess() {
                                model.getUsernameById(userId, new LoginContract.Model.getUsernameByIdCallback() {
                                    @Override
                                    public void onSuccess(String username) {
                                        view.clearInputFields();
                                        view.setWarningText("");
                                        view.showToastMessage();
                                        if (accountType.equals("owner")) {
                                            model.createStoreForOwner(userId, username, new LoginContract.Model.createStoreForOwnerCallback() {
                                                @Override
                                                public void onSuccess(String storeId) {
                                                    view.navigateToStoreActivity(userId, storeId);
                                                }
                                            });
                                        } else {
                                            view.navigateToBrowseStoreActivity(userId);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFailure() {
                                view.setWarningText(view.getStringFromResource(R.string.login_password_incorrect_warning));
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        view.setWarningText(view.getStringFromResource(R.string.login_account_type_incorrect_warning));
                    }
                });
            }

            @Override
            public void onFailure() {
                view.setWarningText(view.getStringFromResource(R.string.login_cannot_find_user_warning));
            }
        });
    }
}
