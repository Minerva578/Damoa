// 알림 카운트를 로컬 스토리지에서 불러오기
let alertCount = 0;

window.onload = function() {
    const alertCountElement = document.querySelector('.alert-count');
    const savedAlertCount = localStorage.getItem('alertCount');

    // 저장된 카운트가 있으면 표시
    if (savedAlertCount) {
        alertCount = parseInt(savedAlertCount);
        alertCountElement.textContent = alertCount;

        // 카운트가 0이면 숨김
        alertCountElement.style.display = alertCount > 0 ? 'inline' : 'none';
    } else {
        alertCountElement.style.display = 'none';
    }
};

// WebSocket 및 STOMP 설정
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

// 알림 데이터를 수신하고 처리하는 초기화 함수
function initializeNotification(callback) {
    stompClient.connect({}, function(frame) {
        console.log('Connected for notifications: ' + frame);

        stompClient.subscribe('/topic/refundAlerts', function(message) {
            const alertData = JSON.parse(message.body);
            console.log("환불 요청 알림을 받았습니다:", alertData);

            // 수신된 데이터를 콜백을 통해 UI에 전달
            if (callback && typeof callback === 'function') {
                callback(alertData);
            }
        });
    });
}

// 팝업 상태를 저장하는 변수
let isPopupOpen = false;

// 알림 아이콘 클릭 시 팝업 표시/숨기기
document.querySelector('.alert-icon').addEventListener('click', function() {
    const alertPopup = document.getElementById('alertPopup');
    const alertCountElement = document.querySelector('.alert-count');

    if (isPopupOpen) {
        alertPopup.style.display = 'none'; // 팝업 숨기기
    } else {
        alertPopup.style.display = 'block'; // 팝업 표시

        // 팝업이 열릴 때 알림 카운트 초기화
        alertCount = 0;
        alertCountElement.textContent = alertCount;
        alertCountElement.style.display = 'none'; // 카운트가 0일 때 숨김
        localStorage.setItem('alertCount', alertCount);
    }
    isPopupOpen = !isPopupOpen;
});

// 수신된 알림 데이터를 처리하는 콜백 함수 설정
initializeNotification(function(alertData) {
    const alertCountElement = document.querySelector('.alert-count');
    alertCount++;

    // 알림 카운트를 업데이트하고 로컬 스토리지에 저장
    alertCountElement.textContent = alertCount;
    alertCountElement.style.display = alertCount > 0 ? 'inline' : 'none'; // 카운트가 0이면 숨김
    localStorage.setItem('alertCount', alertCount);
});
