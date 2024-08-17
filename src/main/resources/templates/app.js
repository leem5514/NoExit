// // WebSocket 서버에 연결합니다.
// const socket = new WebSocket('ws://localhost:8080/ws/chat');
//
// // WebSocket 연결이 열렸을 때
// socket.onopen = function(event) {
//     console.log('WebSocket connection opened');
//     // 입장 메시지 보내기
//     const enterMessage = {
//         type: 'ENTER',
//         roomId: '5ab6b180-6928-4ddc-a977-a3c2a49e0053', // 실제 방 ID로 교체
//         sender: 'User4',
//         message: 'hi'
//     };
//     socket.send(JSON.stringify(enterMessage));
// };
//
// // 서버로부터 메시지를 받았을 때
// socket.onmessage = function(event) {
//     const messageData = JSON.parse(event.data);
//     const chat = document.getElementById('chat');
//     const messageElement = document.createElement('div');
//     messageElement.textContent = `${messageData.sender}: ${messageData.message}`;
//     chat.appendChild(messageElement);
// };
//
// // 서버와의 연결이 닫혔을 때
// socket.onclose = function(event) {
//     console.log('WebSocket connection closed');
// };
//
// // 서버와의 통신 중 오류가 발생했을 때
// socket.onerror = function(event) {
//     console.error('WebSocket error:', event);
// };
//
// // 메시지 보내기
// function sendMessage() {
//     const messageInput = document.getElementById('message');
//     const message = messageInput.value;
//
//     if (message.trim() === '') return; // 빈 메시지는 무시
//
//     const chatMessage = {
//         type: 'TALK',
//         roomId: '5ab6b180-6928-4ddc-a977-a3c2a49e0053', // 실제 방 ID로 교체
//         sender: 'User4',
//         message: message
//     };
//
//     socket.send(JSON.stringify(chatMessage));
//     messageInput.value = ''; // 입력 필드 초기화
// }