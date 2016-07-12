package app.study.nick.com.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.study.nick.com.demo.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yangjun1 on 2016/5/3.
 */
public class SDTextInputLayoutFragment extends BaseFragment {
    public static String TAG = SDTextInputLayoutFragment.class.getSimpleName();


    @Bind(R.id.login)
    TextInputEditText mName;
    @Bind(R.id.code)
    TextInputEditText mCode;


    public static SDTextInputLayoutFragment getInstance() {
        SDTextInputLayoutFragment f = new SDTextInputLayoutFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_support_design_textinputlayout, container, false);
        ButterKnife.bind(this, v);

        mCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    if ('0' > c || c > '9') {
                        mCode.setError("必须输入0~9之间的数字");
                    }
                }
            }
        });
        return v;
    }


    @Override
    public boolean onBackPressed() {
        return true;
    }

}
