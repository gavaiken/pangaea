package aiken.gav.appengine.pangaea.client;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("chatService")
public interface ChatService extends RemoteService {

  public abstract String Login (String userId) throws IdInUseException ;
  public abstract void SendMessage (String userId, String message) ;
}
