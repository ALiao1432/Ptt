package study.ian.ptt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import study.ian.ptt.R;

public class LoginFragment extends Fragment {

    private final String TAG = "LoginFragment";
    private ImageView photoView;
    private TextView nameText;
    private TextView emailText;
    private TextView loginFeatureText;
    private MaterialButton logInOutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_login, container, false);
        
        findViews(v);
        setViews();
        return v;
    }

    private void findViews(View v) {
        photoView = v.findViewById(R.id.photoView);
        nameText = v.findViewById(R.id.nameText);
        emailText = v.findViewById(R.id.emailText);
        loginFeatureText = v.findViewById(R.id.loginFeatureText);
        logInOutButton = v.findViewById(R.id.logInOutButton);
    }

    private void setViews() {

    }
}
