package io.github.paulyc.jthefax;

import spark.Route;

import static spark.Spark.post;
import static spark.Spark.port;

public class Main {

    private static Route faxSent = (request, response) -> {
        // Let's manually build some TwiML. We can choose to receive the
        // fax with <Receive>, or reject with <Reject>.
        String twiml = "<Response>" +
                "<Receive action=\"/fax/received\"/>" +
                "</Response>";

        // Send Fax twiml response
        response.type("text/xml");
        return twiml;
    };

    // Define a handler for when the fax is finished sending to us - if successful,
    // We will have a URL to the contents of the fax at this point
    private static Route faxReceived  = (request, response) -> {
        // log the URL of the PDF received in the fax
        System.out.println(request.params(":MediaUrl"));

        // Respond with empty 200/OK to Twilio
        response.status(200);
        return null;
    };

    public static void main(String[] args) {
        port(3000);

        post("/fax/sent", faxSent);

        post("/fax/received", faxReceived);
    }
}
