    // submit 했을 때 처리
    document.getElementById('chatForm').addEventListener('submit', function (event) {
        event.preventDefault(); // 기본 제출 동작 방지

        var formData = new FormData(this);
        fetchMessage();

    });

function fetchMessage() {
    // 입력된 메시지를 가져오는 방법 (예: input 필드에서)
    const message = document.getElementById('bot--message').value;

    fetch('http://localhost:8080/chat/fetchMessage', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            "message": message,
        })
    })
    .then(response => {
        if (!response.ok) {
        console.error(`HTTP 오류: ${response.status} ${response.statusText}`);
            throw new Error('네트워크 오류');
        }
        return response.json();
    })
    .then(data => {
        console.log("챗봇 응답:", data);
        // 응답 데이터를 resultDiv에 표시
        const resultDiv = document.getElementById('resultDiv');

        // 사용자가 보낸 메시지 (오른쪽 정렬)
        resultDiv.innerHTML += `<p class="right-message">${message}</p><br>`;
        // 챗봇 응답 (왼쪽 정렬)
        resultDiv.innerHTML += `<p class="left-message">${data.description}</p><br>`;

        // 입력 필드 초기화
        document.getElementById('bot--message').value = '';

        // 스크롤을 맨 아래로 이동
        resultDiv.scrollTop = resultDiv.scrollHeight;
    })

    .catch(error => {
        console.error("에러 발생:", error);
    });
}

// 입력 필드의 값을 가져오는 함수
function getMessage() {
    const message = document.getElementById('bot--message').value; // 입력 필드의 값 가져오기
    console.log('입력한 메시지:', message);
    return message; // 값을 반환
}