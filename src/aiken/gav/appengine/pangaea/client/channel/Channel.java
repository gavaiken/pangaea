package aiken.gav.appengine.pangaea.client.channel;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * License http://www.apache.org/licenses/LICENSE-2.0.html
 * Also available in the COPYING file in this folder.
 * 
 * Original source http://code.google.com/p/dance-dance-robot/
 * 
 * All credit to the first two authors, I just made a couple of changes.
 *  
 * @author schwardo@google
 * @author tobyr@google
 * @author gav.aiken@gmail
 */
public class Channel extends JavaScriptObject {
    protected Channel() {
    }

    public final native Socket open(SocketListener listener) /*-{
        var socket = this.open();
        socket.onopen = function(event) {
          listener.@aiken.gav.appengine.pangaea.client.channel.SocketListener::onOpen()();
        };
        socket.onmessage = function(event) {
          listener.@aiken.gav.appengine.pangaea.client.channel.SocketListener::onMessage(Ljava/lang/String;)(event.data);
        };
        socket.onerror = function(event) {
          listener.@aiken.gav.appengine.pangaea.client.channel.SocketListener::onError(Ljava/lang/String;)(event.description);
        };
        socket.onclose = function(event) {
          listener.@aiken.gav.appengine.pangaea.client.channel.SocketListener::onClose()();
        };
        return socket;
    }-*/;
}
