
package com.visita.controllers;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioSMS
{
    /*
    // Vos identifiants Twilio
    public static final String ACCOUNT_SID = "Account SID";
    public static final String AUTH_TOKEN = "Auth Token";

    public static void main(String[] args) {
        // Initialiser Twilio
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // Envoyer un message
        Message message = Message.creator(
                        new PhoneNumber("Phone Number"),
                        new PhoneNumber("Phone Number"),
                        "Votre rendezvous est confirmé \nMerci pour votre confiance \nBonne journée !")
                .create();

        // Afficher l'identifiant du message
        System.out.println("Message SID: " + message.getSid());
    }

      */
    // Initialisation de Twilio avec vos identifiants
    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";

    // Méthode pour envoyer un SMS
    public static void envoyerSMS(String numeroDestinataire, String message) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            // Envoi du SMS avec Twilio
            Message.creator(new PhoneNumber(numeroDestinataire), new PhoneNumber("Phone Number"), message).create();
            System.out.println("SMS envoyé avec succès à " + numeroDestinataire);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi du SMS : " + e.getMessage());
        }
    }

}
