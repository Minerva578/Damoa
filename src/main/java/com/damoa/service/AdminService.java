package com.damoa.service;

import com.damoa.dto.TossHistoryDTO;
import com.damoa.dto.admin.AdDTO;
import com.damoa.dto.admin.NoticeDTO;
import com.damoa.dto.user.MonthlyRegisterDTO;
import com.damoa.repository.interfaces.UserRepository;
import com.damoa.repository.model.Ad;
import com.damoa.repository.model.Notice;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.damoa.dto.admin.AdminSignInDTO;
import com.damoa.handler.exception.DataDeliveryException;
import com.damoa.repository.interfaces.AdminRepository;
import com.damoa.repository.model.Admin;
import com.damoa.repository.model.User;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.NoSuchElementException;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdminService {

    @Value("${file.upload-dir-ad}")
    private String uploadAddir;

    private final AdminRepository adminRepository;

    private final UserRepository userRepository;


    /**
     * 어드민 로그인 확인
     *
     * @param adminSignInDTO
     * @return
     */
    public Admin findAdmin(AdminSignInDTO adminSignInDTO) {
        Admin admin = adminRepository.findByUsername(adminSignInDTO.getUsername());

        if (admin == null) {
            throw new DataDeliveryException("존재하지 않는 아이디 입니다.", HttpStatus.BAD_REQUEST);
        }

        if (admin.getPassword() == null) {
            throw new DataDeliveryException("비밀번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        return admin;

    }

    public List<MonthlyRegisterDTO> getMonthlyRegisterData() {
        return userRepository.getMonthlyRegisterData();
    }

    /**
     * 어드민 회원관리 페이징 처리 서비스
     *
     * @param pageable
     * @return
     */
    public Page<User> getAllUser(Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        List<User> userList = adminRepository.getUserList(pageable.getPageSize(), offset);
        int totalCount = adminRepository.countUser();
        return new PageImpl<>(userList, pageable, totalCount);
    }

    public List<User> getUserList(int limit, int offset) {
        return adminRepository.getUserList(limit, offset);
    }

    public User getUserDetail(int id) {
        return adminRepository.getUserDetail(id);
    }


    public int createAd(AdDTO dto) {
        int result = adminRepository.insertAd(dto);
        return result;
    }


    public String[] uploadFile(MultipartFile mfile) {

        final long MAX_FILE_SIZE = 20 * 1024 * 1024;


        if (mfile.isEmpty()) {
            throw new DataDeliveryException("파일이 선택되지 않았습니다.", HttpStatus.BAD_REQUEST);
        }

        if (mfile.getSize() > MAX_FILE_SIZE) {
            throw new DataDeliveryException("파일 크기는 20MB 이상 클 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        // 서버 컴퓨터에 파일을 넣을 디렉토리가 있는지 검사
        String saveDirectory = uploadAddir;
        File directory = new File(saveDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 파일 이름 생성(중복 이름 예방)
        String uploadFileName = UUID.randomUUID() + "_" + mfile.getOriginalFilename();

        // 파일 전체경로 + 새로생성한 파일명
        String uploadPath = saveDirectory + File.separator + uploadFileName;

        File destination = new File(uploadPath);
        System.out.println("경로설정: " + destination.getAbsolutePath());


        try {
            mfile.transferTo(destination);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.err.println("에러메시지 :" + e.getMessage());
            throw new DataDeliveryException("파일 업로드중에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return new String[]{mfile.getOriginalFilename(), uploadFileName};
    }

    public Page<Ad> getAdList(Pageable pageable) {
        // number 가 1페이지 2 페이지 , size 는 한페이지에 리스트가몇개인지
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        List<Ad> adList = adminRepository.getAdList(pageable.getPageSize(), offset);
        int count = countAd();

        return new PageImpl<>(adList, pageable, count);
    }

    public Ad getAdDetail(int id) {
        return adminRepository.findById(id);
    }

    public int deleteAd(int id) {
        return adminRepository.deleteAdById(id);
    }

    public int updateAdById(int id, String title) {
        return adminRepository.updateAdById(id, title);

    }

    public int countAd() {
        return adminRepository.countAd();
    }

    public List<AdDTO> activeAd() {
        return adminRepository.activeAd();
    }


    // 공지 삭제
    public void deleteNotice(int id) {
        // 1. 유효한 ID인지 확인
        if (id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다. ID는 1 이상이어야 합니다.");
        }

        // 2. 공지를 삭제하고 결과를 확인
        int result = adminRepository.deleteNoticeById(id);

        // 3. 삭제가 실패한 경우 예외를 발생시킴
        if (result <= 0) {
            throw new NoSuchElementException("해당 ID를 가진 공지를 찾을 수 없습니다.");
        }
    }

    /**
     * 어드민 공지글 작성
     *
     * @param title
     * @param content
     */
    @Transactional
    public void createNotice(String title, String content) {
        int result = adminRepository.insertNotice(title, content);
        if (result != 1) {
            throw new RuntimeException("데이터 삽입 실패" + result);
        }
    }

    public NoticeDTO findNotice(int id) {
        NoticeDTO dto = adminRepository.findNoticeById(id);
        return dto;
    }

    /**
     * 어드민 공지사항 업데이트
     *
     * @param dto
     */
    @Transactional
    public void updateNotice(int id, NoticeDTO dto) {
        adminRepository.updateNotice(id, dto.getTitle(), dto.getContent());
    }

    public Page<NoticeDTO> getAllNotice(Pageable pageable) {

        int offset = pageable.getPageNumber() * pageable.getPageSize();
        List<NoticeDTO> noticeList = adminRepository.viewAllNotice(offset, pageable.getPageSize());
        int totalCount = adminRepository.countNotice();
        return new PageImpl<>(noticeList, pageable, totalCount);
    }
}





