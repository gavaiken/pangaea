package aiken.gav.appengine.pangaea.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.google.api.translate.Language;
import com.google.api.translate.Translate;

import aiken.gav.appengine.pangaea.client.ChatService;
import aiken.gav.appengine.pangaea.client.IdInUseException;

public class ChatServiceImpl extends RemoteServiceServlet implements ChatService {

  private static final Logger logger = Logger.getLogger(ChatServiceImpl.class.getName());
  private ArrayList<String> clientIds = new ArrayList<String>(); 
  private HashMap<String, String> clientIdToToken = new HashMap<String, String>(); 
  
  public ChatServiceImpl(){
    super();
    Translate.setKey(Constants.GoogleAPIKey);
    Translate.setHttpReferrer("http://pan-gae.appspot.com");
  }
  
  @Override
  public String Login(String clientId) throws IdInUseException {
    synchronized (clientIds)
    {
      if(clientIds.contains(clientId))
      {
        throw new IdInUseException(clientId);
      }
      
      clientIds.add(clientId);
    }
    
    ChannelService channelService = ChannelServiceFactory.getChannelService();

    String token = channelService.createChannel(clientId);

    logger.info("sending back channel api token : " + token);
    clientIdToToken.put(clientId, token);
    
    String message = clientId + " logged on";
    for(String id : clientIds){
      channelService.sendMessage(new ChannelMessage(id, message));
    }
        
    return token;
  }

  @Override
  public void SendMessage(String userId, String message) {

    try 
    {
      String translatedText = Translate.execute(message,
              Language.ENGLISH, Language.FRENCH);

      message = translatedText;
    } catch (Exception e)
    {
      
    }
    
    for(String id : clientIds){
      ChannelServiceFactory.getChannelService()
        .sendMessage(new ChannelMessage(id, userId + ": " + message));
    }
  }

}
