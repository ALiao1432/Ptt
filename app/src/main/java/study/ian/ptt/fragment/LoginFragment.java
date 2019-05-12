package study.ian.ptt.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import study.ian.networkstateutil.ConnectionType;
import study.ian.networkstateutil.RxNetworkStateUtil;
import study.ian.ptt.R;
import study.ian.ptt.util.GlideApp;
import study.ian.ptt.util.OnSyncEmailFinishedListener;

public class LoginFragment extends Fragment implements OnSyncEmailFinishedListener {

    private final String TAG = "LoginFragment";
    private final int REQUEST_CODE_SIGN_IN = 0;

    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private GoogleSignInClient googleSignInClient;
    private FirebaseFirestore firestore;
    private ImageView photoView;
    private TextView nameText;
    private TextView emailText;
    private TextView loginFeatureText;
    private MaterialButton logInOutButton;
    private List<String> emailList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

        initFirestore();
        syncServerEmails();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_login, container, false);

        findViews(v);
        setViews(v);
        return v;
    }

    private void initFirebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(firebaseAuth -> {
            firebaseUser = firebaseAuth.getCurrentUser();
            updateUI(firebaseUser);
        });

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);
    }

    private void initFirestore() {
        firestore = FirebaseFirestore.getInstance();
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            photoView.setImageDrawable(getResources().getDrawable(R.drawable.vd_login_default, null));
            nameText.setVisibility(View.INVISIBLE);
            emailText.setVisibility(View.INVISIBLE);
            loginFeatureText.setVisibility(View.VISIBLE);

            logInOutButton.setText(R.string.login);
        } else {
            GlideApp.with(context)
                    .load(user.getPhotoUrl())
                    .transition(new DrawableTransitionOptions().crossFade(250))
                    .circleCrop()
                    .into(photoView);
            nameText.setVisibility(View.VISIBLE);
            emailText.setVisibility(View.VISIBLE);
            loginFeatureText.setVisibility(View.INVISIBLE);

            nameText.setText(user.getDisplayName());
            emailText.setText(user.getEmail());
            logInOutButton.setText(R.string.logout);
        }
    }

    private void findViews(View v) {
        photoView = v.findViewById(R.id.photoView);
        nameText = v.findViewById(R.id.nameText);
        emailText = v.findViewById(R.id.emailText);
        loginFeatureText = v.findViewById(R.id.loginFeatureText);
        logInOutButton = v.findViewById(R.id.logInOutButton);
    }

    private void setViews(View view) {
        final Observable<ConnectionType> networkStateObservable = new RxNetworkStateUtil(context).getNetworkStateObservable();
        final Observable<ConnectionType> connectionTypeObservable = RxView.clicks(logInOutButton)
                .throttleFirst(1500, TimeUnit.MILLISECONDS)
                .withLatestFrom(networkStateObservable, (unit, connectionType) -> connectionType)
                .share();
        connectionTypeObservable.filter(type -> type == ConnectionType.NO_NETWORK)
                .doOnNext(type -> Snackbar.make(view, "can't operate without network", Snackbar.LENGTH_SHORT).show())
                .subscribe();
        connectionTypeObservable.filter(type -> type != ConnectionType.NO_NETWORK)
                .doOnNext(type -> {
                    if (firebaseUser == null) {
                        signIn();
                    } else {
                        signOut();
                    }
                })
                .subscribe();
    }

    private void signIn() {
        googleSignInClient.signOut();
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, REQUEST_CODE_SIGN_IN);
    }

    private void signOut() {
        firebaseAuth.signOut();
        googleSignInClient.signOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = signInAccountTask.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    Log.d(TAG, "onActivityResult: google signIn fail : " + e);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "onActivityResult: google signIn canceled");
            }
        }
    }

    private void firebaseAuthWithGoogle(@Nullable GoogleSignInAccount account) {
        if (account == null) {
            return;
        }

        AuthCredential authcredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(authcredential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: signIn success");
                    } else {
                        Log.d(TAG, "onComplete: signIn failure : " + task.getException());
                    }
                });
    }

    private void syncServerEmails() {
        List<String> list = new ArrayList<>();
        firestore.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        task.getResult().forEach(snapshot -> list.add(snapshot.getId()));
                        onSyncEmailFinished(list);
                    }
                });
    }

    @Override
    public void onSyncEmailFinished(List<String> resultList) {
        emailList = resultList;

        initFirebaseAuth();
    }
}
