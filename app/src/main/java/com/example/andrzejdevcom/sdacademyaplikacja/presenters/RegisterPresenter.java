package com.example.andrzejdevcom.sdacademyaplikacja.presenters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.andrzejdevcom.sdacademyaplikacja.MainActivity;
import com.example.andrzejdevcom.sdacademyaplikacja.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Katarzyna on 2017-05-27.
 */

public class RegisterPresenter {

    //firebaseAuth
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    //construktor bezargumentowy RegisterPresenter
    public RegisterPresenter() {
    }

    public void registerUser(final Activity activity, String email, String password) {
        //inicjalizacja firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //tworzymy obiekt progress dialog
        progressDialog = new ProgressDialog(activity);
//sprawdzamy czy email posiada znaki tzn nie jest pusty
        if (TextUtils.isEmpty(email)) {
            Utils.showToast(activity, "Enter the email..");
            return;
            //sprawdzamy czy email jest poprawny tzn czy zawiera @
        } else if (!Utils.isEmailVavlid(email)) {
            Utils.showToast(activity, "Enter the valid email..");
            return;
        }

        //sprawdzamy czy telefon ma podlaczenie do internetu
        if (!Utils.checkInternetConnection(activity)) {
            Utils.showToast(activity, "Check your internet connection..");
            return;
        }

        //podobnie jak w email
        if (TextUtils.isEmpty(password)) {
            Utils.showToast(activity, "Enter the password..");
            return;
            //podobniej jak w email
        } else if (!Utils.isPasswordValid(password)) {
            Utils.showToast(activity, "Password is to short..");
            return;
        }

//ustawiany message do progressDialog
        //wyswietlamy za pomoca show()
        progressDialog.setMessage("Registration...");
        progressDialog.show();

        //tworzenie konta za pomoca firebase
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //dismissuje progressDialog
                            progressDialog.dismiss();
                            //tutaj logika odpowiedzialna za to co sie stanie gdy konto uda sie tworzyc
                            //stworzymy sobie intent do nastepnej aktywnosci odpowiedzialnej za ekran z gra
                            Toast.makeText(activity, "Account created", Toast.LENGTH_SHORT).show();
                            activity.finish();
                            activity.startActivity(new Intent(activity, MainActivity.class));
                        } else {
                            progressDialog.dismiss();
                            //tutaj problematyczne niestworzenie konta w firebase
                            //wyswietlamy jednynie informacje, ze coś poszło nie tak
                            Toast.makeText(activity, "Something went wrong... try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
