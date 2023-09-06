package org.apache.catalina;

import java.util.HashMap;
import java.util.Map;
import nextstep.jwp.model.User;

public class SessionManager implements Manager {

  private static final SessionManager INSTANCE = new SessionManager();
  private static final Map<String, Session> SESSIONS = new HashMap<>();

  public static SessionManager InstanceOf() {
    return INSTANCE;
  }

  @Override
  public void add(final Session session) {
    SESSIONS.put(session.getId(), session);
  }

  public void addLoginSession(final String jSessionId, final User user) {
    Session session = new Session(jSessionId);
    session.setAttribute("user", user);
    INSTANCE.add(session); //세션 매니저에 세션을 추가한다.
  }

  @Override
  public Session findSession(final String id) {
    return SESSIONS.get(id);
  }

  @Override
  public void remove(Session session) {
    SESSIONS.remove(session.getId(), session);
  }


  @Override
  public void remove(final String id) {
    SESSIONS.remove(id);
  }

  private SessionManager() {
  }

}
