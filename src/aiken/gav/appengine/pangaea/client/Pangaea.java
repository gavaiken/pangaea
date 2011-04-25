package aiken.gav.appengine.pangaea.client;

import java.util.logging.Logger;

import aiken.gav.appengine.pangaea.client.channel.Channel;
import aiken.gav.appengine.pangaea.client.channel.ChannelFactory;
import aiken.gav.appengine.pangaea.client.channel.SocketListener;
import aiken.gav.appengine.pangaea.server.ChatServiceImpl;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.FlexTable;

public class Pangaea implements EntryPoint {

  private static final Logger logger = Logger.getLogger(Pangaea.class.getName());

  private String userId;

  private VerticalPanel mainPanel = new VerticalPanel();
  private ScrollPanel scrollPanel = new ScrollPanel();
  private FlexTable chatTable = new FlexTable();
  
  private HorizontalPanel horizontalPanel = new HorizontalPanel();
  private TextBox chatTextBox = new TextBox();
  private Button submitButton = new Button("Send"); 
  
  private Label errorMsgLabel = new Label();
  
  private HorizontalPanel loginPanel = new HorizontalPanel();
  
  private TextBox usernameTextBox = new TextBox();
  private Button usernameSubmitButton = new Button("Select");
  
  private ChatServiceAsync chatService = GWT.create(ChatService.class);
  
  @Override
  public void onModuleLoad() {

    // Add the main chat area
    loginPanel.add(usernameTextBox); 
    loginPanel.add(usernameSubmitButton);
    
    usernameSubmitButton.addClickHandler(new ClickHandler(){
      @Override
      public void onClick(ClickEvent event) {
        String text = usernameTextBox.getText();
        if (!"".equals(text)){
          userId = text;
          Login(text);
        }
      }});
    
    mainPanel.add(loginPanel);
    
    // Add the main chat area
    mainPanel.add(scrollPanel);    
    scrollPanel.setWidget(chatTable);
    
    // Add the submit area
    mainPanel.add(horizontalPanel);
    horizontalPanel.add(chatTextBox);
    horizontalPanel.add(submitButton);
    
    submitButton.addClickHandler(new ClickHandler(){
      @Override
      public void onClick(ClickEvent event) {
        final String text = chatTextBox.getText();
        if (!"".equals(text)){
          final String details = "User: " + userId + " Message: " + text;
          chatService.SendMessage(userId, text, new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
              DisplayError("Failed to send message - " + details);
            }

            @Override
            public void onSuccess(Void result) {
              logger.info("Message Sent - " + details);
            }
          });
        }
      }});
    
    // Add the area to display error messages
    errorMsgLabel.setVisible(false);
    mainPanel.add(errorMsgLabel);
    
    // Associate the Main panel with the HTML host page.
    RootPanel.get("chatConsole").add(mainPanel);
  }
  
  private void Login(String userName){
    // Open a connection to the server
    chatService.Login(userName, new AsyncCallback<String>() {

      public void onFailure(Throwable caught) {
        String details = caught.getMessage();
        if (caught instanceof IdInUseException) {
          details = "ID '" + ((IdInUseException)caught).getId() + "' is in use";
        }

        DisplayError("Login request failed: " + details);
      }

      public void onSuccess(String result) {
        loginPanel.setVisible(false);
        
        Channel channel = ChannelFactory.createChannel(result);
        channel.open(new SocketListener() {
            @Override
            public void onMessage(String message) {
              logger.info("Recieved Message: " + message);
              chatTable.setText(chatTable.getRowCount(), 0, message);
            }
            
            @Override
            public void onOpen() {
              logger.info("Connection Opened: ");
            }

            @Override
            public void onError(String details) {
              DisplayError("Failed to Open Channel: " + details);
            }

            @Override
            public void onClose() {
              logger.info("Connection Closed");
            }
        });

        // Clear any errors.
        errorMsgLabel.setVisible(false);
      }});
  }
  

  private void DisplayError(String errorDetails) {
    errorMsgLabel.setText(errorDetails);
    errorMsgLabel.setVisible(true);
  }
}
