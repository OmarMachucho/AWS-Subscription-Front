/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.newsletter;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Omar Machucho
 */
@ManagedBean(name = "controller")
@RequestScoped
public class controller implements Serializable {

    private String email;
    private String messageStatus;
    private WebTarget webTarget;
    private Client client;

    @PostConstruct
    public void init() {
        email = "";
        messageStatus = "";
    }

    public controller() {
    }

    public void checkEmail() {

        if (findEmail()) {
            System.out.println("The email doesn't exists");
            saveEmail();
        } else {
            messageStatus = "This email has already been registered";
            PrimeFaces.current().ajax().update("status");
            System.out.println("The email exists already");
        }
    }

    public void saveEmail() {
        client = ClientBuilder.newClient();
        webTarget = client.target("http://localhost:8080/api").path("/saveEmail");
        try {

            client = ClientBuilder.newClient();
            String get = "";
            WebTarget resource = webTarget;
            get = resource
                    .queryParam("email", this.email)
                    .request(MediaType.APPLICATION_JSON)
                    .get(String.class);

//            get = resource.request().accept(MediaType.APPLICATION_JSON).post(Entity.json(this.email), String.class);
            JSONObject data = new JSONObject(get);
            if (data.optString("status").equals("1")) {
                System.out.println("Email saved successfully");
                messageStatus = "Email registered";
                PrimeFaces.current().ajax().update("status");
                //Email saved
                System.out.println("");
            } else if (data.optString("status").equals("0")) {
                System.out.println("Couldn't save email");
                //Email not saved
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public boolean findEmail() {
        System.out.println("Email ingresado " + this.email);
        client = ClientBuilder.newClient();
        webTarget = client.target("http://localhost:8080/api").path("/findEmail");

        try {
            client = ClientBuilder.newClient();
            String get = "";
            WebTarget resource = webTarget;
            get = resource
                    .queryParam("email", this.email)
                    .request(MediaType.APPLICATION_JSON)
                    .get(String.class);
            JSONObject data = new JSONObject(get);
            if (data.optString("status").equals("1")) {
                //Save Email
                return false;
            } else if (data.optString("status").equals("0")) {
                //Don't save email
                return true;
            }

        } catch (Exception e) {
            System.out.println("Falla aca");
            System.out.println(e);
        }

        return true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

}
