package nextstep.jwp.controller;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.catalina.SessionManager;
import org.apache.coyote.http11.controller.AbstractController;
import org.apache.coyote.http11.exception.UnauthorizeException;
import org.apache.coyote.http11.request.HttpCookie;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpResponseHeader;
import org.apache.coyote.http11.response.HttpResponseStatus;

import java.util.Map;
import java.util.UUID;

public class LoginPostController extends AbstractController {
    public static final String JSESSIONID = "JSESSIONID";

    @Override
    public boolean isSupported(HttpRequest request) {
        return request.isPOST() && request.isSamePath("/login");
    }

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        if (request.isNotExistBody()) {
            throw new IllegalArgumentException("로그인 정보가 입력되지 않았습니다.");
        }
        final HttpCookie cookie = request.getCookie();
        Map<String, String> parsedRequestBody = request.parseBody();
        User user = InMemoryUserRepository.findByAccount(parsedRequestBody.get("account"))
                .orElseThrow(() -> new UnauthorizeException("입력한 회원 ID가 존재하지 않습니다."));
        if (isLoginFail(user, parsedRequestBody)) {
            throw new UnauthorizeException("입력한 회원 ID가 존재하지 않습니다.");
        }
        if (!cookie.isExist(JSESSIONID)) {
            String jSessionId = String.valueOf(UUID.randomUUID());
            String setCookie = JSESSIONID + "=" + jSessionId;
            SessionManager.getInstance().addLoginSession(jSessionId, user);
            HttpResponseHeader responseHeader = new HttpResponseHeader.Builder(
                    readContentType(request.getAccept(), request.getPath()), String.valueOf(0))
                    .addLocation("/index.html")
                    .addSetCookie(setCookie)
                    .build();
            response.updateResponse(HttpResponseStatus.FOUND, responseHeader, "");
            return;
        }
        HttpResponseHeader responseHeader = new HttpResponseHeader.Builder(
                readContentType(request.getAccept(), request.getPath()), String.valueOf(0))
                .addLocation("/index.html")
                .build();
        response.updateResponse(HttpResponseStatus.FOUND, responseHeader, "");

    }

    private boolean isLoginFail(User user, Map<String, String> parsedRequestBody) {
        return !user.checkPassword(parsedRequestBody.get("password"));
    }
}
