package com.visita.models;



import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;

public class Payment {
    public static void main(String[] args) {
// Set your secret key here
        Stripe.apiKey = "sk_test_51PBEhAJ8wVFBGMuI9cID3FZiOxrdb0LHvJp5iimPTRYzfLY3ea4OA7RdS9txTvBC7B55Wm0TCyHyZ5GbzcsaobYQ00HGioMKCF";

        try {
// Retrieve your account information
            Account account = Account.retrieve();
            System.out.println("Account ID: " + account.getId());
// Print other account information as needed
        } catch (StripeException e) {
            e.printStackTrace();
        }
    }
}