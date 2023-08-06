package com.example.b07_project_group5;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
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
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginContract.Model.findUserWithEmailCallback findUserWithEmailCallback = invocation.getArgument(1);
                findUserWithEmailCallback.onFailure();
                return null;
            }
        }).when(model).findUserWithEmail(eq("usernotfound@gmail.com"), any(LoginContract.Model.findUserWithEmailCallback.class));
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).setWarningText(view.getStringFromResource(R.string.login_cannot_find_user_warning));
    }

    @Test
    public void testIncorrectAccountType() {
        when(view.getEmailInput()).thenReturn("vincent@gmail.com");
        when(view.getPasswordInput()).thenReturn("vincent");
        when(view.getAccountTypeInput()).thenReturn("shopper");
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginContract.Model.findUserWithEmailCallback findUserWithEmailCallback = invocation.getArgument(1);
                findUserWithEmailCallback.onSuccess();
                return null;
            }
        }).when(model).findUserWithEmail(eq("vincent@gmail.com"), any(LoginContract.Model.findUserWithEmailCallback.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginContract.Model.getUserIdByEmailAndAccountTypeCallback getUserIdByEmailAndAccountTypeCallback = invocation.getArgument(2);
                getUserIdByEmailAndAccountTypeCallback.onFailure();
                return null;
            }
        }).when(model).getUserIdByEmailAndAccountType(eq("vincent@gmail.com"), eq("shopper"), any(LoginContract.Model.getUserIdByEmailAndAccountTypeCallback.class));
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).setWarningText(view.getStringFromResource(R.string.login_account_type_incorrect_warning));
    }

    @Test
    public void testIncorrectPassword() {
        when(view.getEmailInput()).thenReturn("edison@gmail.com");
        when(view.getPasswordInput()).thenReturn("12345");
        when(view.getAccountTypeInput()).thenReturn("owner");
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginContract.Model.findUserWithEmailCallback findUserWithEmailCallback = invocation.getArgument(1);
                findUserWithEmailCallback.onSuccess();
                return null;
            }
        }).when(model).findUserWithEmail(eq("edison@gmail.com"), any(LoginContract.Model.findUserWithEmailCallback.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginContract.Model.getUserIdByEmailAndAccountTypeCallback getUserIdByEmailAndAccountTypeCallback = invocation.getArgument(2);
                getUserIdByEmailAndAccountTypeCallback.onSuccess("-NaKjb3W2PIULK3_yjjW");
                return null;
            }
        }).when(model).getUserIdByEmailAndAccountType(eq("edison@gmail.com"), eq("owner"), any(LoginContract.Model.getUserIdByEmailAndAccountTypeCallback.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginContract.Model.checkUserPasswordIsCorrectCallback checkUserPasswordIsCorrectCallback = invocation.getArgument(2);
                checkUserPasswordIsCorrectCallback.onFailure();
                return null;
            }
        }).when(model).checkUserPasswordIsCorrect(eq("-NaKjb3W2PIULK3_yjjW"), eq("12345"), any(LoginContract.Model.checkUserPasswordIsCorrectCallback.class));
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).setWarningText(view.getStringFromResource(R.string.login_password_incorrect_warning));
    }

    @Test
    public void testValidShopper() {
        when(view.getEmailInput()).thenReturn("edison@gmail.com");
        when(view.getPasswordInput()).thenReturn("edison");
        when(view.getAccountTypeInput()).thenReturn("shopper");
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginContract.Model.findUserWithEmailCallback findUserWithEmailCallback = invocation.getArgument(1);
                findUserWithEmailCallback.onSuccess();
                return null;
            }
        }).when(model).findUserWithEmail(eq("edison@gmail.com"), any(LoginContract.Model.findUserWithEmailCallback.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginContract.Model.getUserIdByEmailAndAccountTypeCallback getUserIdByEmailAndAccountTypeCallback = invocation.getArgument(2);
                getUserIdByEmailAndAccountTypeCallback.onSuccess("-NaKnQ_i7VUDQVW__YQ2");
                return null;
            }
        }).when(model).getUserIdByEmailAndAccountType(eq("edison@gmail.com"), eq("shopper"), any(LoginContract.Model.getUserIdByEmailAndAccountTypeCallback.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginContract.Model.checkUserPasswordIsCorrectCallback checkUserPasswordIsCorrectCallback = invocation.getArgument(2);
                checkUserPasswordIsCorrectCallback.onSuccess();
                return null;
            }
        }).when(model).checkUserPasswordIsCorrect(eq("-NaKnQ_i7VUDQVW__YQ2"), eq("edison"), any(LoginContract.Model.checkUserPasswordIsCorrectCallback.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginContract.Model.getUsernameByIdCallback getUsernameByIdCallback = invocation.getArgument(1);
                getUsernameByIdCallback.onSuccess("edison");
                return null;
            }
        }).when(model).getUsernameById(eq("-NaKnQ_i7VUDQVW__YQ2"), any(LoginContract.Model.getUsernameByIdCallback.class));
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).clearInputFields();
        verify(view).setWarningText("");
        verify(view).showToastMessage();
        verify(view).navigateToBrowseStoreActivity("-NaKnQ_i7VUDQVW__YQ2");
    }

    @Test
    public void testValidStoreOwner() {
        when(view.getEmailInput()).thenReturn("edison@gmail.com");
        when(view.getPasswordInput()).thenReturn("edison");
        when(view.getAccountTypeInput()).thenReturn("owner");
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginContract.Model.findUserWithEmailCallback findUserWithEmailCallback = invocation.getArgument(1);
                findUserWithEmailCallback.onSuccess();
                return null;
            }
        }).when(model).findUserWithEmail(eq("edison@gmail.com"), any(LoginContract.Model.findUserWithEmailCallback.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginContract.Model.getUserIdByEmailAndAccountTypeCallback getUserIdByEmailAndAccountTypeCallback = invocation.getArgument(2);
                getUserIdByEmailAndAccountTypeCallback.onSuccess("-NaKjb3W2PIULK3_yjjW");
                return null;
            }
        }).when(model).getUserIdByEmailAndAccountType(eq("edison@gmail.com"), eq("owner"), any(LoginContract.Model.getUserIdByEmailAndAccountTypeCallback.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginContract.Model.checkUserPasswordIsCorrectCallback checkUserPasswordIsCorrectCallback = invocation.getArgument(2);
                checkUserPasswordIsCorrectCallback.onSuccess();
                return null;
            }
        }).when(model).checkUserPasswordIsCorrect(eq("-NaKjb3W2PIULK3_yjjW"), eq("edison"), any(LoginContract.Model.checkUserPasswordIsCorrectCallback.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginContract.Model.getUsernameByIdCallback getUsernameByIdCallback = invocation.getArgument(1);
                getUsernameByIdCallback.onSuccess("edisonlol");
                return null;
            }
        }).when(model).getUsernameById(eq("-NaKjb3W2PIULK3_yjjW"), any(LoginContract.Model.getUsernameByIdCallback.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginContract.Model.createStoreForOwnerCallback createStoreForOwnerCallback = invocation.getArgument(2);
                createStoreForOwnerCallback.onSuccess("-NaKjfc7qHvl0pgOt-uB");
                return null;
            }
        }).when(model).createStoreForOwner(eq("-NaKjb3W2PIULK3_yjjW"), eq("edisonlol"), any(LoginContract.Model.createStoreForOwnerCallback.class));
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).clearInputFields();
        verify(view).setWarningText("");
        verify(view).showToastMessage();
        verify(view).navigateToStoreActivity("-NaKjb3W2PIULK3_yjjW", "-NaKjfc7qHvl0pgOt-uB");
    }
}