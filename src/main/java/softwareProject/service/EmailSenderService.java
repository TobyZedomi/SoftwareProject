package softwareProject.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import softwareProject.business.*;
import softwareProject.persistence.*;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("screensafari321@gmail.com");

        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);

        System.out.println("Mail sent successfully...");
    }


    public void sendEmailMovie(String toEmail, String subject, Model model, HttpSession session) throws MessagingException, IOException {

        MimeMessage messageMime = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(messageMime, true, "UTF-8");
        helper.setFrom("screensafari321@gmail.com");

        helper.setTo(toEmail);
        helper.setSubject(subject);

        sendingEmailBodyAndMovieImageToUser(session, helper);

        mailSender.send(messageMime);

        System.out.println("Mail sent successfully...");
    }

    private static void sendingEmailBodyAndMovieImageToUser(HttpSession session, MimeMessageHelper helper) throws MessagingException {
        User u = (User) session.getAttribute("loggedInUser");

        CartDao cartDao = new CartDaoImpl("database.properties");

        // gettingCartId from username
        Cart cart = cartDao.getCartByUsername(u.getUsername());

        CartItemDao cartItemDao = new CartItemDaoImpl("database.properties");


        // getting all cart Items from cartId
        ArrayList<CartItem> cartItems = cartItemDao.getAllCartItemsByCartId(cart.getCart_id());

        ArrayList<MovieProduct> movies = new ArrayList<>();

        OrderItem orderItem;


        ArrayList<OrderItem> orderItems = new ArrayList<>();

        for (int i = 0; i < cartItems.size();i++) {


            // getting movies by movies in cartItem Id
            MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");

            movies.add(movieProductDao.getMovieById(cartItems.get(i).getMovie_id()));

            ShopOrderDao shopOrderDao = new ShopOrderDaoImpl("database.properties");

            ShopOrder shopOrder = shopOrderDao.getOrderWithTheHighestOrderIdByUsername(u.getUsername());

            orderItem = new OrderItem(0, movies.get(i).getListPrice(), shopOrder.getOrder_id(), cartItems.get(i).getMovie_id());

            // create an arraylist of order items based on order items id above

            orderItems.add(orderItem);

        }

        System.out.println(orderItems);

        MovieProductDao movieProductDao = new MovieProductDaoImpl("database.properties");
        ;

        double total = 0;

        ArrayList<String> movieNames = new ArrayList<>();
        ArrayList<String> movieImages = new ArrayList<>();
        MovieProduct movieProduct;


        for (int i = 0; i < orderItems.size();i++){

            movieProduct = movieProductDao.getMovieById(orderItems.get(i).getMovie_id());

            movieNames.add(movieProduct.getMovie_name());
            movieImages.add(movieProduct.getMovie_image());

            total = total + movieProduct.getListPrice();

        }

/*
        for (int i = 0; i < movieNames.size();i++){

            helper.setText("You have successfully purchased movies: " + movieNames.get(i) + " with a Total Price of £"+total);
        }

 */


        helper.setText("You have successfully purchased movies: " + movieNames + " with a Total Price of £"+total);

        for (int i = 0; i < movieImages.size();i++){

            helper.addAttachment(movieImages.get(i), new File("C:\\Users\\tobyz\\IdeaProjects\\SoftwareProject\\src\\main\\resources\\static\\css\\images\\"+movieImages.get(i)));

        }
    }
}
