// 페이지 로드 시 실행되는 코드
window.onload = function() {
    const refundBtns = document.querySelectorAll('#refund--Btn'); // 모든 refund--Btn 요소를 선택

    refundBtns.forEach(refundBtn => {
        const status = refundBtn.getAttribute('data-status');

        // status 값이 1이면 텍스트를 '환불 신청중'으로 변경하고 클릭 비활성화
        if (status === '1') {
            const anchor = refundBtn.querySelector('a');
            anchor.textContent = '환불 처리중';
            anchor.style.pointerEvents = 'none'; // 클릭 이벤트 비활성화
            anchor.classList.add('disabled');
        } else if (status === '2'){
            const anchor = refundBtn.querySelector('a');
            anchor.textContent = '처리 완료';
            anchor.style.pointerEvents = 'none'; // 클릭 이벤트 비활성화
            anchor.classList.add('disabled');
        }
    });
};

// SockJS 라이브러리를 사용하여 WebSocket 연결을 생성
// http://localhost:8080/ws는 서버의 STOMP 엔드포인트 URL
const socket = new SockJS('http://localhost:8080/ws');

// SockJS로 생성한 WebSocket 객체를 STOMP 클라이언트로 래핑
const stompClient = Stomp.over(socket);

// STOMP 클라이언트를 사용하여 서버에 연결
stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);

    // '/topic/refundAlerts' 경로를 구독
    stompClient.subscribe('/topic/refundAlerts', function (message) {
        const alertData = JSON.parse(message.body); // 메시지 내용을 JSON 형식으로 변환
        console.log("환불 요청 알림을 받았습니다:", alertData);

        // 알림을 표시하거나 UI 업데이트를 실행하는 코드 추가
        // showRefundAlert(alertData); // 예: showRefundAlert() 함수로 알림 표시
    }, function (error) {
        // 연결 실패 시 호출되는 콜백 함수
        console.error('Error connecting: ', error); // 오류 메시지를 로그에 출력
    }); // 이 부분이 추가되었습니다.

});

function requestRefund(id, userId) {
    if (confirm("정말 환불신청을 하시겠습니까?")) {

        const requestData = {
            id: id,       // 환불 요청 ID
            userId: userId // 사용자 ID
        }
        // 환불 요청을 서버로 전송
        stompClient.send("/app/requestRefund", {}, JSON.stringify(requestData));

        fetch(`http://localhost:8080/user/fetchRefundStatus?id=` + id + `&userId=` + userId)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log("완료");
                location.reload();
            })
            .catch(error => {
                console.error('Error fetching:', error);
            });
    } else {
        console.log("사용자가 환불신청을 취소했습니다.");
    }
}