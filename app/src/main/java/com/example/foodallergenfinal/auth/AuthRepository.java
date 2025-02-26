package com.example.foodallergenfinal.auth;

import static android.content.ContentValues.TAG;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AuthRepository {

    private final String tag = "AuthRepository: ";
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final Executor executor = Executors.newSingleThreadExecutor(); // Background executor


    public boolean isLoggedIn() {
        if (firebaseAuth.getCurrentUser() != null) {
            System.out.println(tag + "User is logged in");
            return true;
        }
        return false;
    }

    public CompletableFuture<Boolean> signUp(String email, String password) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println(tag + "signup success");
                        signIn(email, password);
                        future.complete(true);
                    } else {
                        System.out.println(tag + "signup failure");
                        future.complete(false);
                    }
                });

        return future;
    }

    public CompletableFuture<Boolean> signIn(String email, String password) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println(tag + "signin success");
                        future.complete(true);
                    } else {
                        System.out.println(tag + "signin failure");
                        future.complete(false);
                    }
                });

        return future;
    }

    public void logout() {
        firebaseAuth.signOut();
        System.out.println(tag + "logout success");
    }

    public CompletableFuture<Boolean> forgotPassword(String email) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println(tag + "Password reset email sent");
                        future.complete(true);
                    } else {
                        System.out.println(tag + "Password reset failed");
                        future.complete(false);
                    }
                });
        return future;
    }

    /*public CompletableFuture<Boolean> updatePassword(String newPassword) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user == null) {
            System.err.println(TAG + "Update password failed: User not logged in");
            future.complete(false);
            return future;
        }

        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println(TAG + "Password update success");
                        future.complete(true);
                    } else {
                        System.err.println(TAG + "Password update failed: " + task.getException());
                        future.complete(false);
                    }
                });

        return future;
    }*/

    public CompletableFuture<Boolean> updatePassword(String oldPassword, String newPassword) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user == null || user.getEmail() == null) {
            System.err.println(TAG + "Update password failed: User not logged in");
            future.complete(false);
            return future;
        }

        // Reauthenticate with old password
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(credential)
                .addOnCompleteListener(executor, authTask -> {
                    if (authTask.isSuccessful()) {
                        // If reauthentication succeeds, update password
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(executor, updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        System.out.println(TAG + "Password update success");
                                        future.complete(true);
                                    } else {
                                        System.err.println(TAG + "Password update failed: " + updateTask.getException());
                                        future.complete(false);
                                    }
                                });
                    } else {
                        System.err.println(TAG + "Reauthentication failed: " + authTask.getException());
                        future.complete(false);
                    }
                });

        return future;
    }

}

