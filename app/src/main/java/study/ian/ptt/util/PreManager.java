package study.ian.ptt.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class PreManager {

    private final String TAG = "PreManager";
    private static final String PREF_NAME = "study.ian.ptt";
    public static final String FAV_BOARD = "favBoard";
    public static final String BLACKLIST = "blackList";
    public static final String COLLECTION_USERS = "users";
    public static final String DOCUMENT_USER_EMAIL_LIST = "USER_EMAIL_LIST";
    public static final String FIELD_EMAILS = "EMAILS";

    private static final int FAV_ACTION_ADD = 0;
    private static final int FAV_ACTION_REMOVE = 1;

    private static Set<String> favSet = new HashSet<>();
    private static Set<String> blacklistSet;
    private static PreManager preManager;
    private final SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser;
    private final List<OnFavActionListener> onFavActionListenerList = new ArrayList<>();
    private final List<OnFavSyncFinishedListener> onFavSyncFinishedListenerList = new ArrayList<>();
    private final List<OnBlacklistSyncFinishedListener> onBlacklistSyncFinishedListenerList = new ArrayList<>();

    public interface OnFavActionListener {
        void onFavAction(String b, int action);
    }

    public interface OnFavSyncFinishedListener {
        void onFavSyncFinished();
    }

    public interface OnBlacklistSyncFinishedListener {
        void onBlacklistSyncFinished();
    }

    public void addOnFavActionListener(OnFavActionListener listener) {
        onFavActionListenerList.add(listener);
    }

    public void addOnFavSyncFinishedListenerList(OnFavSyncFinishedListener listener) {
        onFavSyncFinishedListenerList.add(listener);
    }

    public void addOnBlacklistSyncFinishedListener(OnBlacklistSyncFinishedListener listener) {
        onBlacklistSyncFinishedListenerList.add(listener);
    }

    private PreManager(@NotNull Context cxt) {
        sharedPreferences = cxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        initFavSet();
        initBlacklistSet();
    }

    private void initFavSet() {
        favSet = new HashSet<>();

        String boards = sharedPreferences.getString(FAV_BOARD, "");
        StringTokenizer tokenizer = new StringTokenizer(boards);
        while (tokenizer.hasMoreTokens()) {
            favSet.add(tokenizer.nextToken());
        }
    }

    private void initBlacklistSet() {
        blacklistSet = new HashSet<>();

        String blacklist = sharedPreferences.getString(BLACKLIST, "");
        StringTokenizer tokenizer = new StringTokenizer(blacklist);
        while (tokenizer.hasMoreTokens()) {
            blacklistSet.add(tokenizer.nextToken());
        }
    }

    private void initFirestoreSnapshotListener(FirebaseUser user) {
        if (user != null && user.getEmail() != null) {
            firestore.collection(COLLECTION_USERS)
                    .document(user.getEmail())
                    .addSnapshotListener((snapshot, e) -> {
                        if (e != null) {
                            Log.d(TAG, "onEvent: listen failed : " + e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            String currentFavBoard = sharedPreferences.getString(FAV_BOARD, "");
                            String favSnapshot = snapshot.getString(FAV_BOARD);
                            if (currentFavBoard == null || !currentFavBoard.equals(favSnapshot)) {
                                syncFavBoards(favSnapshot);
                            }

                            String currentBlacklist = sharedPreferences.getString(BLACKLIST, "");
                            String blacklistSnapshot = snapshot.getString(BLACKLIST);
                            if (currentBlacklist == null || !currentBlacklist.equals(blacklistSnapshot)) {
                                syncBlacklists(blacklistSnapshot);
                            }
                        }
                    });
        }
    }

    static synchronized void initPreManager(Context cxt) {
        if (preManager == null) {
            preManager = new PreManager(cxt);
        }
    }

    public static synchronized PreManager getInstance() {
        return preManager;
    }

    public void toggleFavBoard(String board) {
        if (isFavBoard(board)) {
            // if it is fav board, then remove it
            removeFavBoard(board);
        } else {
            // if it is not fav board, then add it
            addFavBoard(board);
        }

        updateFavToFirestore();
    }

    private void removeFavBoard(String board) {
        editor = sharedPreferences.edit();
        String temp = sharedPreferences.getString(FAV_BOARD, "");
        if (temp != null && temp.contains(board)) {
            temp = temp.replace(board, "").trim();
            editor.putString(FAV_BOARD, temp);
            editor.apply();
        }
        favSet.remove(board);
        onFavActionListenerList.forEach(l -> l.onFavAction(board, FAV_ACTION_REMOVE));
    }

    private void addFavBoard(String board) {
        editor = sharedPreferences.edit();
        String temp = board + " " + sharedPreferences.getString(FAV_BOARD, "");
        editor.putString(FAV_BOARD, temp);
        editor.apply();

        favSet.add(board);
        onFavActionListenerList.forEach(l -> l.onFavAction(board, FAV_ACTION_ADD));
    }

    private void updateFavToFirestore() {
        if (firebaseUser != null && firebaseUser.getEmail() != null) {
            firestore.collection(COLLECTION_USERS)
                    .document(firebaseUser.getEmail())
                    .update(FAV_BOARD, sharedPreferences.getString(FAV_BOARD, ""));
        }
    }

    public void syncFavBoards(String boards) {
        editor = sharedPreferences.edit();
        favSet.clear();

        StringTokenizer tokenizer = new StringTokenizer(boards, " ");
        while (tokenizer.hasMoreTokens()) {
            favSet.add(tokenizer.nextToken());
        }
        editor.putString(FAV_BOARD, boards);
        editor.apply();
        onFavSyncFinishedListenerList.forEach(OnFavSyncFinishedListener::onFavSyncFinished);
    }

    public Set<String> getFavBoardSet() {
        return favSet;
    }

    public String getFavBoard() {
        return sharedPreferences.getString(FAV_BOARD, "");
    }

    public void addBlacklist(String black) {
        editor = sharedPreferences.edit();
        String b = sharedPreferences.getString(BLACKLIST, "");
        if (b != null && !b.contains(black)) {
            editor.putString(BLACKLIST, black + " " + b);

            blacklistSet.add(black);
        }
        editor.apply();
    }

    public void syncBlacklists(String blacks) {
        updateBlacklist(blacks);
        onBlacklistSyncFinishedListenerList.forEach(OnBlacklistSyncFinishedListener::onBlacklistSyncFinished);
    }

    public void updateBlacklist(String blacks) {
        editor = sharedPreferences.edit();
        editor.putString(BLACKLIST, blacks);
        editor.apply();

        initBlacklistSet();
    }

    public void updateBlacklistToFirestore() {
        if (firebaseUser != null && firebaseUser.getEmail() != null) {
            firestore.collection(COLLECTION_USERS)
                    .document(firebaseUser.getEmail())
                    .update(BLACKLIST, sharedPreferences.getString(BLACKLIST, ""));
        }
    }

    public String getBlacklist() {
        return sharedPreferences.getString(BLACKLIST, "");
    }

    public boolean isFavBoard(String targetBoard) {
        return favSet.contains(targetBoard);
    }

    public boolean isBlacklist(String black) {
        return blacklistSet.contains(black);
    }

    public void setCurrentUser(FirebaseUser user) {
        firebaseUser = user;
        initFirestoreSnapshotListener(firebaseUser);
    }
}
