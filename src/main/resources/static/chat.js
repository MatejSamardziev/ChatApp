
    // Declare a variable to hold the STOMP client
    var stompClient = null;

    // Function to connect to the WebSocket server
    function connect() {
    // Create a new SockJS connection to the /ws endpoint
    var socket = new SockJS('/ws');
    // Create a STOMP client over the SockJS connection
    stompClient = Stomp.over(socket);

    // Connect the STOMP client
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        // Notify the server that the client is online
        stompClient.send('/app/user-online', {});

        stompClient.subscribe("/user/queue/db-messages",function(messageOutput)
        {
            showMessageFromDB(JSON.parse(messageOutput.body))
        })

        stompClient.subscribe("/user/queue/unread-messages-rabbit",function(messageOutput)
        {
            showMessage(JSON.parse(messageOutput.body),true);

        });
        fetchUnreadMessagesFromRabbit();
        // Subscribe to the /user/queue/reply destination to receive messages
         stompClient.subscribe('/user/queue/reply', function (messageOutput) {
           showMessage(JSON.parse(messageOutput.body),true);
         });

         stompClient.subscribe("/user/queue/unread-from-db",function(messageOutput){
            unreadMessagesNotification(JSON.parse(messageOutput.body));
         });
         fetchUnreadMessagesFromDB();

        window.addEventListener('beforeunload', function () {
            // Notify server that the user is offline
            stompClient.send("/app/user-offline", {});
        });
});



}

    // Function to send a message
    function sendMessage(event) {
        // Prevent the default form submission

        // Find the form that was submitted
        var form = event.target.closest('form');

        // Get values from the form elements within the submitted form
        var recipient = form.querySelector('#recipient').value;
        var messageContent = form.querySelector('input[type="text"]').value;
        var sender = form.querySelector('#currentUser').value;
        console.log("RECIPIENT IS "+recipient);
        console.log("SENDER IS "+sender);

        // Send a message to the /app/message destination
        stompClient.send("/app/message", {}, JSON.stringify({
            'content': messageContent,
            'recipient': recipient,
            'sender': sender
        }));

        // Clear the message input after sending
        form.querySelector('input[type="text"]').value = '';

        // Optionally, you can update UI immediately with the sent message
        showOwnMessage({ recipient: recipient, content: messageContent,sender:sender});
    }

    // Function to display a received message
    function showMessage(message,flag) {
    var sender=message.sender;
    var messages = document.getElementById('messages-'+sender);
    var messageElement = document.createElement('div');
    messageElement.className = 'theirMessage';
    var usernameElement = document.createElement('div');
    usernameElement.className = 'theirUsername';
    usernameElement.textContent = message.sender;
    var messageBoxElement = document.createElement('div');
    messageBoxElement.className = 'theirMessageBox';
    messageBoxElement.textContent = message.content;
    messageElement.appendChild(usernameElement);
    messageElement.appendChild(messageBoxElement);
    messages.appendChild(messageElement);
    if(flag){
        var chatWindow=document.getElementById('chat-'+sender);
        if(chatWindow.classList.contains("hidden"))
            showUnreadNotification(message.sender);
    }
}

  function showOwnMessage(message){
      var recipient=message.recipient;
      var messages = document.getElementById('messages-'+recipient);
      var messageElement = document.createElement('div');
      messageElement.className='yourMessage';
      messageElement.appendChild(document.createTextNode(message.content));
      messages.appendChild(messageElement)
  }

    function showMessageFromDB(messages) {
        if (messages.length > 0) {
            // Get the other user's identifier from the first message
            var otherUser = messages[0].otherUser;
            // Get the messages container for the other user
            var messagesContainer = document.getElementById('messages-' + otherUser);

            // Clear previous messages to prevent duplication
            messagesContainer.innerHTML = '';
        }
        messages.forEach(function(message) {
            var sender=message.sender;
            var otherUser=message.otherUser;
            var messages = document.getElementById('messages-'+message.otherUser);
            var messageElement = document.createElement('div');
            if(sender===otherUser){
               showMessage(message,false);
            }
            else{
                messageElement.className='yourMessage';
                messageElement.appendChild(document.createTextNode(message.content));
            }
            messages.appendChild(messageElement);
        });
    }

  function fetchUnreadMessagesFromRabbit(){
        stompClient.send("/app/fetch-unread-rabbit",{});
  }

  function fetchUnreadMessagesFromDB(){
   stompClient.send("/app/fetch-unread-db",{});
  }



    // Function to open chat window
    function openChat(event, element) {
        event.preventDefault();
        var username = element.textContent;
        var chatWindows = document.querySelectorAll('.chat-window');
        chatWindows.forEach(function (chatWindow) {
            chatWindow.classList.add('hidden');
        });
        var chatWindow = document.getElementById('chat-' + username);
        var userBanner=document.getElementById("chat-user-banner");
        userBanner.innerHTML=username;
        if (chatWindow) {
            chatWindow.classList.remove('hidden');
        }
        var userListItem=document.getElementById("user-list-item-"+username);
        var unreadValue=
        stompClient.send("/app/fetch-db",{},JSON.stringify({
            'otherUser':username
        }));

        // Mark messages as read
        stompClient.send("/app/mark-as-read", {}, JSON.stringify({
            'otherUser': username
        }));
        var notificationElement =document.getElementById("message-notification-"+username);
        notificationElement.classList.add("hidden");
    }

    function unreadMessagesNotification(senders){
        senders.forEach(function (sender){
           showUnreadNotification(sender);
        });
    }

    function showUnreadNotification(username){
        var notificationElement =document.getElementById("message-notification-"+username);
        notificationElement.classList.remove("hidden");
    }
    // Connect to the WebSocket server when the page loads
    connect();
