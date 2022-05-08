package mypackage;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
	public void submitEmail(String total,ArrayList<String> transactions, String averageDebit, String averageCredit) {
	String to = "victorandy78@gmail.com";
    String from = "victorandy78@gmail.com";
    String host = "smtp.gmail.com";
    String port = "465";
    Properties properties = System.getProperties();
    properties.setProperty("mail.smtp.host", host);
    properties.setProperty("mail.smtp.port", port);
    properties.setProperty("mail.smtp.ssl.enable", "true");
    properties.setProperty("mail.smtp.auth", "true");
    Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

        protected PasswordAuthentication getPasswordAuthentication() {

            return new PasswordAuthentication(from, "'youremailpassword");

        }

    });
    try {
       MimeMessage message = new MimeMessage(session);
       message.setFrom(new InternetAddress(from));
       message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
       message.setSubject("Transaction summary");
       String body = "";
       body += "<img src='https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQCULtT3AtjpuADGYkPZK4Ez_ph0R5XqgQboA&usqp=CAU'></img><br>";
       body += "<br><div>"+total+"<div><br>";
       for (String t : transactions) {
    	   body += "<div>"+t+"<div><br>";
       }
       body += "<div>"+averageDebit+"<div><br>";
       body += "<div>"+averageCredit+"<div>";
       message.setContent(body,"text/html");
       Transport.send(message);
       System.out.println("Sent message successfully....");
    } catch (MessagingException mex) {
       mex.printStackTrace();
    }
	}
}