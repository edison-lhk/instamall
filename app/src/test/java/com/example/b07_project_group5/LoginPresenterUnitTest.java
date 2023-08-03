package com.example.b07_project_group5;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterUnitTest {
    @Mock
    LoginActivity view;

    @Mock
    LoginModel model;

    @Test
    public void testAllEmptyInputFields() {
        when(view.getEmailInput()).thenReturn("");
        when(view.getPasswordInput()).thenReturn("");
        when(view.getAccountTypeInput()).thenReturn("");
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).setWarningText(view.getStringFromResource(R.string.login_empty_fields_warning));
    }

    @Test
    public void testEmptyEmail() {
        when(view.getEmailInput()).thenReturn("");
        when(view.getPasswordInput()).thenReturn("12345");
        when(view.getAccountTypeInput()).thenReturn("shopper");
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).setWarningText(view.getStringFromResource(R.string.login_empty_fields_warning));
    }

    @Test
    public void testEmptyPassword() {
        when(view.getEmailInput()).thenReturn("edison@gmail.com");
        when(view.getPasswordInput()).thenReturn("");
        when(view.getAccountTypeInput()).thenReturn("shopper");
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).setWarningText(view.getStringFromResource(R.string.login_empty_fields_warning));
    }

    @Test
    public void testEmptyAccountType() {
        when(view.getEmailInput()).thenReturn("edison@gmail.com");
        when(view.getPasswordInput()).thenReturn("edison");
        when(view.getAccountTypeInput()).thenReturn("");
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).setWarningText(view.getStringFromResource(R.string.login_empty_fields_warning));
    }

    @Test
    public void testUserNotFound() {
        when(view.getEmailInput()).thenReturn("usernotfound@gmail.com");
        when(view.getPasswordInput()).thenReturn("12345");
        when(view.getAccountTypeInput()).thenReturn("shopper");
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).setWarningText(view.getStringFromResource(R.string.login_cannot_find_user_warning));
    }

    @Test
    public void testIncorrectPassword() {
        when(view.getEmailInput()).thenReturn("edison@gmail.com");
        when(view.getPasswordInput()).thenReturn("12345");
        when(view.getAccountTypeInput()).thenReturn("owner");
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).setWarningText(view.getStringFromResource(R.string.login_password_incorrect_warning));
    }

    @Test
    public void testIncorrectAccountType() {
        when(view.getEmailInput()).thenReturn("vincent@gmail.com");
        when(view.getPasswordInput()).thenReturn("vincent");
        when(view.getAccountTypeInput()).thenReturn("shopper");
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).setWarningText(view.getStringFromResource(R.string.login_account_type_incorrect_warning));
    }

    @Test
    public void testValidStoreOwner() {
        when(view.getEmailInput()).thenReturn("edison@gmail.com");
        when(view.getPasswordInput()).thenReturn("edison");
        when(view.getAccountTypeInput()).thenReturn("owner");
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).clearInputFields();
        verify(view).setWarningText("");
        verify(view).showToastMessage();
    }

    @Test
    public void testValidShopper() {
        when(view.getEmailInput()).thenReturn("edison@gmail.com");
        when(view.getPasswordInput()).thenReturn("edison");
        when(view.getAccountTypeInput()).thenReturn("shopper");
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).clearInputFields();
        verify(view).setWarningText("");
        verify(view).showToastMessage();
    }
}