package aiken.gav.appengine.pangaea.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ChatServiceAsync {

  void Login(String userId, AsyncCallback<String> callback);

  void SendMessage(String userId, String message, AsyncCallback<Void> callback);

}
