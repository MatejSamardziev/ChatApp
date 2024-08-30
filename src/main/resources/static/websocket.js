// Declare a variable to hold the STOMP client
var stompClient = null;
var unreadMessagesCount = parseInt(document.getElementById('unreadMessagesCount').innerText, 10) || 0;

// Function to connect to the WebSocket server
function connect() {
    // Create a new SockJS connection to the /ws endpoint
    var socket = new SockJS('/ws');
    // Create a STOMP client over the SockJS connection
    stompClient = Stomp.over(socket);

    // Connect the STOMP client
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        // Subscribe to the /user/queue/reply destination to receive messages
        stompClient.subscribe('/user/queue/reply', function (messageOutput) {
            showMessage(JSON.parse(messageOutput.body));
        });
    });
}


// Function to display a received message
function showMessage(message) {
    console.log("TESTING SHIT");
    //var messages = document.getElementById('messages');
    //var messageElement = document.createElement('div');
    //messageElement.appendChild(document.createTextNode(message.sender + ": " + message.content));
    //messages.appendChild(messageElement);
    updateUnreadMessagesCount();
}

// Function to update the unread messages count display
function updateUnreadMessagesCount() {
    unreadMessagesCount++;
    var unreadMessagesDisplay = document.getElementById('unreadMessagesCount');
    unreadMessagesDisplay.innerText = unreadMessagesCount;
}

// Connect to the WebSocket server when the page loads
connect();