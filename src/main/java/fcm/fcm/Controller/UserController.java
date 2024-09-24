package fcm.fcm.Controller;

import fcm.fcm.Entity.LoginRequest;
import fcm.fcm.Entity.UserEntity;
import fcm.fcm.Entity.grade.UserGrade;
import fcm.fcm.Exception.UserAlreadyExistsException;
import fcm.fcm.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        UserEntity user = userService.findUser(request.getEmail());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저 정보가 잘못되었습니다.");
        }

        if (user.getPassword().equals(request.getPassword()) && user.getEmail().equals(request.getEmail())) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 잘못 되었습니다.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        // 세션 제거 코드 아마 사용할일 없을듯 -> 세션 관련은 프론트에서 하는게 편함
        session.removeAttribute("user");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserEntity user) {
        // 사용자 기본 등급과 생성 및 업데이트 시간을 설정
        user.setUserGrade(UserGrade.USER);
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());

        // Optional을 사용하여 기존 사용자가 있는지 확인
        Optional<UserEntity> existingUser = Optional.ofNullable(userService.findUser(user.getEmail()));

        // 사용자가 이미 존재하면 예외 발생
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("이미 존재하는 이메일입니다.");
        } else {
            // 새로운 사용자 가입 처리
            userService.join(user);
        }

        // 성공적으로 가입되면 해당 사용자 정보 반환
        return ResponseEntity.ok(user);
    }
}
