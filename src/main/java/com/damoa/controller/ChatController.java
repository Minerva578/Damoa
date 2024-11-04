package com.damoa.controller;

import com.damoa.dto.chat.ChatListDTO;
import com.damoa.repository.model.ChatMessage;
import com.damoa.repository.model.User;
import com.damoa.service.ChatListService;
import com.damoa.service.ChatMessageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/match")
@RequiredArgsConstructor
@Log4j2
public class ChatController {

    private final ChatListService chatListService;
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate; // STOMP 메시지 전송을 위한 템플릿 클래스

    @GetMapping("/my-project")
    public String myProjectPage(){

        return null;
    }

    /**
     * 클라이언트가 "/app/chat" 경로로 보낸 메시지를 처리
     * @param chatMessage 클라이언트가 보낸 채팅 메시지
     * @return 클라이언트에게 다시 전송할 채팅 메시지
     */
    @MessageMapping("/chat/{roomId}/{senderId}/{receiverId}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage,
                                   @DestinationVariable String roomId) {

        // message, senderId, receiverId를 MongoDB에 저장
        chatMessageService.saveMessage(chatMessage);

        messagingTemplate.convertAndSend("/topic/messages/" + roomId, chatMessage);

        log.info("전송할 메시지: {}", chatMessage);
        log.info("브로드캐스트할 roomId: {}", roomId);

        return chatMessage;
    }

    // 채팅 페이지를 반환하는 메서드
    @GetMapping("/chat")
    public String chatPage() {

        return "layout/chat";
    }

    /*
    * sender, receiver ID 테이블 저장 기능
    * 대화 요청 버튼에 해당 기능 삽입예정
    * 주소설계: http://localhost:8080/match/chat-request
    * */
    @PostMapping("/chat-request")
    @ResponseBody   // 서버 응답 요청
    public String saveChatRequest(@RequestParam("senderId") int senderId,
                                  @RequestParam("receiverId") int receiverId) {

        log.info("senderId: {}, receiverId: {}", senderId, receiverId);
        if (senderId <= 0 && receiverId <= 0) {
            return "채팅 요청에 실패했습니다.";
        }

        chatListService.saveByChatList(senderId, receiverId);

        return "채팅 요청이 완료되었습니다.";
    }

    //채팅 목록을 반환 하는 메서드
    @GetMapping("/chat-list")
    @ResponseBody
    public List<ChatListDTO> chatList(HttpSession session, Model model) {
        // 로그인 된 유저 세션 정보
        User user = (User) session.getAttribute("principal");

        // 서비스의 메서드를 이용해 userName과 list 데이터를 가져옴
        List<ChatListDTO> chatList = chatListService.findByChatList(user.getId());

        return chatList;
    }

    /*
    * 채팅 목록 삭제 기능
    * @param roomId
    * */
    @DeleteMapping("/delete-chat-room")
    @ResponseBody
    public void deleteChatList(@RequestParam("roomId") int roomId) {
        chatListService.deleteByChatList(roomId);
    }

    /*
    * 채팅 메시지 내역 조회 기능
    * @param roomId
    * */
    @GetMapping("/chat/messages")
    @ResponseBody
    public List<ChatMessage> messageList(@RequestParam("roomId") String roomId) {
        List<ChatMessage> messageList = chatMessageService.findByMessageList(roomId);

        log.info("roomId: {}", roomId);
        log.info("messageList: {}", messageList);
        return messageList;
    }
}
