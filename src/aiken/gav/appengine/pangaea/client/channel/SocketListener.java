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
public interface SocketListener {
    void onOpen();
    void onMessage(String message);
    void onError(String description);
    void onClose();
}
