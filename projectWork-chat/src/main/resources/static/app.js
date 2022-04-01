let usernameForm = document.querySelector('#usernameForm');
let messageForm = document.querySelector('#messageForm');
let messageInput = document.querySelector('#message');
let messageArea = document.querySelector('#messageArea');
let messagingArea = document.querySelector('#messagingArea');
let connectingElement = document.querySelector('.connecting');

let stompClient = null;
let sub1 = null;
let subNewUserMessage = null;
let user = null;
let username = null;
let currentUserForm = null;

let userColor = '#378805'
let usernameColor = '#703770'

function connect(that) {

    if(currentUserForm){
        currentUserForm.userButton.style['background-color']='#82AC85';
    }

    user = that.user.value.trim();
    username = that.username.value.trim();
    document.title = user + ':: Чат с ' + username;

    that.userButton.style['background-color']='#F46D75';

    $('#userButton_'+username).text(username);

    currentUserForm=that;

    if(username) {
        let socket = new SockJS('/websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}

function onConnected() {

    if(sub1){
        sub1.unsubscribe();
    }

    sub1 = stompClient.subscribe('/topic/response.'+username+'.'+user, onMessageReceived);

    if(!subNewUserMessage){
        subNewUserMessage = stompClient.subscribe('/topic/messageFor.'+user, updateUsersNewMessages);
    }

    console.log(new Date().toString().substring(4, 24))
    $('#messageArea').empty();
    stompClient.send("/app/chat."+user+"."+username, {}, {});
    messagingArea.classList.remove('hidden');
    connectingElement.classList.add('hidden');
}

function onError(error) {
    connectingElement.textContent = 'Не удается подключиться к серверу WebSocket. Попробуйте еще раз!';
    connectingElement.style.color = 'darkred';
}

function updateUsersNewMessages(payload) {
    message = JSON.parse(payload.body);
    if(message.usernameFrom!=username&&message.usernameTo!=username&&message.usernameFrom!=user&&message.usernameTo==user){
            $('#userButton_'+message.usernameFrom).text(message.usernameFrom+" (*)");
    }
}

function sendMessage(event) {
    let messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        let message = {
            userFrom: user,
            userTo: username,
            text: messageInput.value,
            sendingTime: new Date().toString().substring(4, 24),
        };

        stompClient.send("/app/sendMessage."+user+'.'+username, {}, JSON.stringify(message));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    let messageElement = document.createElement('li');

    messageElement.classList.add('chat-message');

    let usernameElement = document.createElement('span');
    let usernameText = document.createTextNode(message.usernameFrom);
    if(message.usernameFrom==user){
        usernameElement.style.color=userColor;
    }else{
        usernameElement.style.color=usernameColor;
    }

    usernameElement.appendChild(usernameText);

    messageElement.appendChild(usernameElement);

    let textElement = document.createElement('p');
    let messageText = document.createTextNode(message.messageSendTime.replace('T', ' ') + ': ' + message.messageText);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

messageForm.addEventListener('submit', sendMessage, true)