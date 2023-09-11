package nextstep.jwp.controller;

import org.apache.coyote.http11.controller.AbstractController;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpResponseHeader;
import org.apache.coyote.http11.response.HttpResponseStatus;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class RegisterGetController extends AbstractController {


    @Override
    public boolean isSupported(HttpRequest request) {
        return request.isGET() && request.isSamePath("/register");
    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) throws URISyntaxException, IOException {
        URL filePathUrl = getClass().getResource("/static/register.html");
        String responseBody = readHtmlFile(filePathUrl);

        HttpResponseHeader responseHeader = new HttpResponseHeader.Builder(
                readContentType(request.getAccept(), request.getPath()),
                String.valueOf(responseBody.getBytes().length))
                .build();
        response.updateResponse(HttpResponseStatus.OK, responseHeader, responseBody);
    }
}
