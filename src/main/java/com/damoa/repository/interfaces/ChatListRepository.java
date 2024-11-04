package com.damoa.repository.interfaces;

import com.damoa.repository.model.ChatList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/*
 * MySQL 매퍼
 * chat_list_tb
 */
@Mapper
public interface ChatListRepository {

    // 전체 채팅 목록 데이터 가져오기
    List<ChatList> findByChatList(int userId);

    // senderId와 receiverId의 조합이 list에 있는지 중복 확인
    boolean existsBySenderIdAndReceiverId(@Param("senderId") int senderId, @Param("receiverId") int receiverId);

    /*
     * function: chat_list_tb insert
     * @param senderId, receiverId
     */
    void saveByChatList(ChatList chatList);

    // function: chat_list_tb row delete
    void deleteByChatList(int roomId);

    /*
     * function: 대화방 개설 시 최초 메시지 이후 수신자의 목록에 나타나도록 업데이트
     * @param senderId, reciverId
     */
    void updateVisibleToReceiver(@Param("senderId") int senderId, @Param("receiverId") int receiverId);
}
