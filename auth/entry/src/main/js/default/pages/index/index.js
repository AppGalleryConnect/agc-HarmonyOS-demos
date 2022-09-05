import prompt from '@system.prompt';
import agconnect from '@agconnect/api';
import '@agconnect/instance';
import '@agconnect/auth-harmony';
import fetch from '@system.fetch';
import storage from '@system.storage';
export default {
    data: {
        authType: "phone",
        account: "",
        password: "",
        verifyCode: "",
        userInfo: ''
    },
    onInit() {
        this.configInstance();
    },
    changeMode(value) {
        this.authType = value.newValue;
    },
    changeAccount(value) {
        this.account = value.value;
    },
    changePassword(value) {
        this.password = value.value;
    },
    changeVerifyCode(value) {
        this.verifyCode = value.value;
    },

    login() {
        if (!this.account) {
            console.error('account must input.');
            return Promise.reject('err: need account');
        }
        switch (this.authType) {
            case "phone":
                return this.loginWithPhone(this.account, this.password, this.verifyCode).then(() => {
                    this.getUserInfo();
                    prompt.showToast({
                        message: 'login ok',
                        duration: 2000,
                    });
                }).catch((error) => {
                    console.error(error);
                    prompt.showToast({
                        message: 'login failed',
                        duration: 2000,
                    });
                });
            case "email":
                return this.loginWithEmail(this.account, this.password, this.verifyCode).then(() => {
                    this.getUserInfo();
                    prompt.showToast({
                        message: 'login ok',
                        duration: 2000,
                    });
                }).catch((error) => {
                    console.error(error);
                    prompt.showToast({
                        message: 'login failed',
                        duration: 2000,
                    });
                });
            default:
                console.error('unsupported auth type for login');
                return Promise.resolve();
        }
    },
    createUser() {
        if (!this.account) {
            console.error('account must input.');
            return Promise.reject('err: need account');
        }
        switch (this.authType) {
            case "phone":
                return this.createPhoneUser(this.account, this.password, this.verifyCode).then(() => {
                    this.getUserInfo();
                    prompt.showToast({
                        message: 'login ok',
                        duration: 2000,
                    });
                }).catch((error) => {
                    console.error(error);
                    prompt.showToast({
                        message: 'login failed',
                        duration: 2000,
                    });
                });
            case "email":
                return this.createEmailUser(this.account, this.password, this.verifyCode).then(() => {
                    this.getUserInfo();
                    prompt.showToast({
                        message: 'login ok',
                        duration: 2000,
                    });
                }).catch((error) => {
                    console.error(error);
                    prompt.showToast({
                        message: 'login failed',
                        duration: 2000,
                    });
                });
            default:
                console.error('unsupported auth type for createUser');
                return Promise.resolve();
        }
    },
    loginAnonymously() {
        this.agcloginAnonymously().then(() => {
            this.getUserInfo();
            prompt.showToast({
                message: 'login ok',
                duration: 2000,
            });
        }).catch((error) => {
            console.error(error);
            prompt.showToast({
                message: 'login failed',
                duration: 2000,
            });
        });
    },
    getVerifyCode() {
        switch (this.authType) {
            case "phone":
                console.log(this.account)
                this.getPhoneVerifyCode(this.account).then(() => {
                    prompt.showToast({
                        message: 'send ok',
                        duration: 2000,
                    });
                }).catch((error) => {
                    console.error(error.message);
                    prompt.showToast({
                        message: 'send failed',
                        duration: 2000,
                    });
                });
                break;
            case "email":
                this.getEmailVerifyCode(this.account).then(() => {
                    prompt.showToast({
                        message: 'send ok',
                        duration: 2000,
                    });
                }).catch((error) => {
                    console.error(error);
                    prompt.showToast({
                        message: 'send failed',
                        duration: 2000,
                    });
                });
                break;
            default:
                console.error('unsupported auth type for getVerifyCode');
                break;
        }
    },
    updatePwd() {
        switch (this.authType) {
            case "phone":
                this.updatePhonePwd(this.password,this.verifyCode).then(() => {
                    prompt.showToast({
                        message: 'updatePwd OK',
                        duration: 2000,
                    });
                }).catch((error) => {
                    console.error(error);
                    prompt.showToast({
                        message: 'updatePwd failed',
                        duration: 2000,
                    });
                });
                break;
            case "email":
                this.updateEmailPwd(this.password,this.verifyCode).then(() => {
                    prompt.showToast({
                        message: 'updatePwd OK',
                        duration: 2000,
                    });
                }).catch((error) => {
                    console.error(error);
                    prompt.showToast({
                        message: 'updatePwd failed',
                        duration: 2000,
                    });
                });
                break;
            default:
                console.error('unsupported auth type');
                break;
        }
    },
    reauthenticate() {
        switch (this.authType) {
            case "phone":
                this.userReauthenticateByPhone("86", this.account, this.password, this.verifyCode).then(() => {
                    prompt.showToast({
                        message: 'Reauthenticate OK',
                        duration: 2000,
                    });
                }).catch((error) => {
                    console.error(error);
                    prompt.showToast({
                        message: 'Reauthenticate failed',
                        duration: 2000,
                    });
                });
                break;
            case "email":
                this.userReauthenticateByEmail(this.account, this.password, this.verifyCode).then(() => {
                    prompt.showToast({
                        message: 'Reauthenticate OK',
                        duration: 2000,
                    });
                }).catch((error) => {
                    console.error(error);
                    prompt.showToast({
                        message: 'Reauthenticate failed',
                        duration: 2000,
                    });
                });
                break;
            default:
                console.error('unsupported auth type');
                break;
        }
    },
    updateAccount() {
        switch (this.authType) {
            case "phone":
                this.updatePhone(this.account, this.verifyCode, "zh_CN").then(() => {
                    this.getUserInfo();
                    prompt.showToast({
                        message: 'updateAccount OK',
                        duration: 2000,
                    });
                }).catch((error) => {
                    console.error(error);
                    prompt.showToast({
                        message: 'updateAccount failed',
                        duration: 2000,
                    });
                });
                break;
            case "email":
                this.updateEmail(this.account, this.verifyCode, "zh_CN").then(() => {
                    this.getUserInfo();
                    prompt.showToast({
                        message: 'updateAccount OK',
                        duration: 2000,
                    });
                }).catch((error) => {
                    console.error(error);
                    prompt.showToast({
                        message: 'updateAccount failed',
                        duration: 2000,
                    });
                });
                break;
            default:
                console.error('unsupported auth type');
                break;
        }
    },
    getVerifyCodeForPWD() {
        switch (this.authType) {
            case "phone":
                this.getPhoneVerifyCode(this.account, false).then(() => {
                    prompt.showToast({
                        message: 'send OK',
                        duration: 2000,
                    });
                }).catch((error) => {
                    console.error(error);
                    prompt.showToast({
                        message: 'send failed',
                        duration: 2000,
                    });
                });
                break;
            case "email":
                this.getEmailVerifyCode(this.account, false).then(() => {
                    prompt.showToast({
                        message: 'send OK',
                        duration: 2000,
                    });
                }).catch((error) => {
                    console.error(error);
                    prompt.showToast({
                        message: 'send failed',
                        duration: 2000,
                    });
                });
                break;
            default:
                console.error('unsupported auth type');
                break;
        }
    },
    getUserInfo() {
        agconnect.auth().getCurrentUser().then((user) => {
            if (user) {
                this.userInfo = JSON.stringify({
                    uid: user.getUid(),
                    anonymous: user.isAnonymous(),
                    displayName: user.getDisplayName(),
                    email: user.getEmail(),
                    phone: user.getPhone(),
                    photoUrl: user.getPhotoUrl(),
                    providerId: user.getProviderId(),
                })
            } else {
                this.resetAccountInfo();
            }
        }).catch((err) => {
            this.resetAccountInfo();
            console.error(err);
        });
    },
    updateProfile() {
        let profile = {
            displayName: 'HW AGC',
            photoUrl: 'a url',
        };
        this.agcupdateProfile(profile).then(() => {
            this.getUserInfo();
            prompt.showToast({
                message: 'updateProfile ok',
                duration: 2000,
            });
        }).catch((err) => {
            console.error(err);
            prompt.showToast({
                message: 'updateProfile failed',
                duration: 2000,
            });
        });
    },
    logOut() {
        agconnect.auth().signOut().then(() => {
            this.resetAccountInfo();
            console.log('logout');
            prompt.showToast({
                message: 'logOut ok',
                duration: 2000,
            });
        }).catch((err) => {
            console.error(err);
            prompt.showToast({
                message: 'logOut failed',
                duration: 2000,
            });
        });
    },
    deleteUser() {
        agconnect.auth().deleteUser().then(() => {
            console.log('deleteUser');
            this.resetAccountInfo();
            prompt.showToast({
                message: 'deleteUser ok',
                duration: 2000,
            });
        }).catch((err) => {
            console.error(err);
            prompt.showToast({
                message: 'deleteUser failed',
                duration: 2000,
            });
        });
    },
    resetAccountInfo() {
        this.userInfo = '';
    },
    configInstance() {
        var agConnectConfig =
            {
            };
        agconnect.instance().configInstance(agConnectConfig);
        agconnect.instance().setFetch(fetch);
        agconnect.instance().setStorage(storage);
    },
    createPhoneUser(account, password, verifyCode) {
        return agconnect.auth().createPhoneUser(new agconnect.auth.PhoneUser('86', account, password, verifyCode));
    },
    createEmailUser(account, password, verifyCode) {
        return agconnect.auth().createEmailUser(new agconnect.auth.EmailUser(account, password, verifyCode));
    },
    getPhoneVerifyCode(account, isRegist = true, sendInterval = 30) {
        return agconnect.auth.PhoneAuthProvider.requestVerifyCode('86', account,
                isRegist ? agconnect.auth.Action.ACTION_REGISTER_LOGIN : agconnect.auth.Action.ACTION_RESET_PASSWORD,
            'zh_CN', sendInterval);
    },
    getEmailVerifyCode(account, isRegist = true, sendInterval = 30) {
        return agconnect.auth.EmailAuthProvider.requestVerifyCode(account,
                isRegist ? agconnect.auth.Action.ACTION_REGISTER_LOGIN : agconnect.auth.Action.ACTION_RESET_PASSWORD,
            'zh_CN', sendInterval);
    },
    agclogin(credential) {
        return agconnect.auth().signIn(credential);
    },
    loginWithPhone(account, password, verifyCode) {
        let credential;
        if (verifyCode) {
            credential = agconnect.auth.PhoneAuthProvider.credentialWithVerifyCode('86', account, password, verifyCode);
        } else {
            credential = agconnect.auth.PhoneAuthProvider.credentialWithPassword('86', account, password);
        }
        if (!credential) {
            return Promise.reject('credential is undefined');
        }
        return this.agclogin(credential);
    },
    loginWithEmail(account, password, verifyCode) {
        let credential;
        if (verifyCode) {
            credential = agconnect.auth.EmailAuthProvider.credentialWithVerifyCode(account, password, verifyCode);
        } else {
            credential = agconnect.auth.EmailAuthProvider.credentialWithPassword(account, password);
        }
        if (!credential) {
            return Promise.reject('credential is undefined');
        }
        return this.agclogin(credential);
    },
    agcloginAnonymously() {
        console.log(agconnect.instance().config().client.project_id)
        return agconnect.auth().signInAnonymously();
    },
    userReauthenticateByEmail(email, password, verifyCode) {
        let credential;
        if (verifyCode) {
            credential = agconnect.auth.EmailAuthProvider.credentialWithVerifyCode(email, password, verifyCode);
        } else {
            credential = agconnect.auth.EmailAuthProvider.credentialWithPassword(email, password);
        }
        return agconnect.auth().getCurrentUser().then(async (user) => {
            if (user) {
                await user.userReauthenticate(credential);
                return;
            } else {
                return Promise.reject("no user login");
            }
        })
    },
    userReauthenticateByPhone(countryCode, phoneNumber, password, verifyCode) {
        let credential;
        if (verifyCode) {
            credential = agconnect.auth.PhoneAuthProvider.credentialWithVerifyCode(countryCode, phoneNumber, password, verifyCode);
        } else {
            credential = agconnect.auth.PhoneAuthProvider.credentialWithPassword(countryCode, phoneNumber, password);
        }
        return agconnect.auth().getCurrentUser().then(async (user) => {
            if (user) {
                await user.userReauthenticate(credential);
                return;
            } else {
                return Promise.reject("no user login");
            }
        })
    },
    updatePhonePwd(newPassword, verifyCode) {
        return agconnect.auth().getCurrentUser().then(async (user) => {
            if (user) {
                console.log(newPassword);
                console.log(verifyCode);
                await user.updatePassword(newPassword, verifyCode, 11);
                return;
            } else {
                return Promise.reject("no user login");
            }
        });
    },
    updateEmailPwd(newPassword, verifyCode) {
        return agconnect.auth().getCurrentUser().then(async (user) => {
            if (user) {
                await user.updatePassword(newPassword, verifyCode, 12);
                return;
            } else {
                return Promise.reject("no user login");
            }
        });
    },
    updatePhone(newPhone, verifyCode, lang) {
        return agconnect.auth().getCurrentUser().then(async (user) => {
            if (user) {
                await user.updatePhone("86", newPhone, verifyCode, lang);
                return;
            } else {
                return Promise.reject("no user login");
            }
        });
    },
    updateEmail(newEmail, verifyCode, lang) {
        return agconnect.auth().getCurrentUser().then(async (user) => {
            if (user) {
                await user.updateEmail(newEmail, verifyCode, lang);
                return;
            } else {
                return Promise.reject("no user login");
            }
        });
    },
    agcupdateProfile(profile) {
        return agconnect.auth().getCurrentUser().then(async (user) => {
            if (user) {
                await user.updateProfile(profile);
                return;
            } else {
                return Promise.reject("no user login");
            }
        });
    }
}
