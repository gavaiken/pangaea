package aiken.gav.appengine.pangaea.client.channel;

/**
 * License http://www.apache.org/licenses/LICENSE-2.0.html
 * Also available in the COPYING file in this folder.
 * 
 * Original source http://code.google.com/p/dance-dance-robot/
 * 
 * All credit to the authors.
 *  
 * @author schwardo@google
 * @author tobyr@google
 */
public class ChannelFactory {
    public static final native Channel createChannel(String channelId) /*-{
      return new $wnd.goog.appengine.Channel(channelId);	
    }-*/;
}
