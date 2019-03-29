package com.dutch.hdh.dutchpayapp.ui.register.password;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dutch.hdh.dutchpayapp.R;
import com.dutch.hdh.dutchpayapp.data.db.User;
import com.dutch.hdh.dutchpayapp.ui.register.success.Register_SuccessFragment;

import java.util.ArrayList;
import java.util.Collections;

public class Register_PaymentPasswordPresenter implements Register_PaymentPasswordContract.Presenter {

    private Register_PaymentPasswordContract.View mView;
    private Context mContext;
    private FragmentManager mFragmentManager;

    //비밀번호 확인
    private String mPassword;
    private String mPasswordCheck;

    // false = 비밀번호 1차입력 , true = 비밀번호 2차 입력
    private boolean isPasswordCheck;

    Register_PaymentPasswordPresenter(Register_PaymentPasswordContract.View mView, Context mContext, FragmentManager mFragmentManager) {
        this.mView = mView;
        this.mContext = mContext;
        this.mFragmentManager = mFragmentManager;
        mPassword = "";
        isPasswordCheck = false;
    }

    @Override
    public void initRandomNumber() {
        //난수생성
        ArrayList<Integer> randomNumber = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            randomNumber.add(i);
        }

        Collections.shuffle(randomNumber);
        for (int i = 0; i < 10; i++) {
            mView.showRandomNumber(i, randomNumber.get(i).toString());
        }
    }

    @Override
    public void clickNumber(String numberText) {
        if (!isPasswordCheck) {
            if (mPassword.length() < 6) {
                mPassword += numberText;
            }

            for (int i = 0; i < 6; i++) {
                if (i < mPassword.length()) {
                    mView.dotImagesUpdate(i, true);
                } else {
                    mView.dotImagesUpdate(i, false);
                }
            }
        } else {
            if (mPasswordCheck.length() < 6) {
                mPasswordCheck += numberText;
            }

            for (int i = 0; i < 6; i++) {
                if (i < mPasswordCheck.length()) {
                    mView.dotImagesUpdate(i, true);
                } else {
                    mView.dotImagesUpdate(i, false);
                }
            }
        }
    }

    @Override
    public void clickDeleteButton() {
        if (!isPasswordCheck) {
            mPassword = "";
        } else {
            mPasswordCheck = "";
        }

        for (int i = 0; i < 6; i++) {
            mView.dotImagesUpdate(i, false);
        }
    }

    @Override
    public void clickOKButton() {

        if (!isPasswordCheck) {
            if (mPassword.length() == 6) {
                isPasswordCheck = true;
                mView.updateView();
                mView.changeTitle("결제시 이용할 암호를 한번 더 입력해주세요.");
                mView.changeMiddleTitle("결제 비밀번호 확인");
                clickDeleteButton();

            } else {
                mView.showFailDialog("결제 비밀번호는 6자리 입니다.");
            }
        } else {
            if (mPasswordCheck.length() == 6) {
                if (isSame()) {
                    mView.showSuccessDialog("회원가입이 완료 되었습니다.");
                    //프래그먼트 이동
                    User user = User.getInstance();
                    user.setUserState(true);
                } else {
                    mView.showFailDialog("비밀번호가 맞지 않습니다.");
                    clickDeleteButton();
                }
            } else {
                mView.showFailDialog("결제 비밀번호는 6자리 입니다.");
            }
        }
    }

    @Override
    public void clickSuccessDialog() {
        mFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        Register_SuccessFragment register_successFragment = new Register_SuccessFragment();

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out);
        fragmentTransaction.replace(R.id.fragment_main, register_successFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        mFragmentManager.executePendingTransactions();
    }

    private boolean isSame() {
        if (mPassword.equals(mPasswordCheck)) {
            return true;
        }
        return false;
    }
}