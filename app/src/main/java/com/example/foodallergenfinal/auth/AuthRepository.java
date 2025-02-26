package com.example.foodallergenfinal.auth;

import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.CompletableFuture;

public class AuthRepository {

    private final String tag = "AuthRepository: ";
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

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
}

