package streamnet;

import streamnet.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyPageViewHandler {


    @Autowired
    private MyPageRepository myPageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenRegistered_then_CREATE_1 (@Payload Registered registered) {
        try {
            if (registered.isMe()) {
                // view 객체 생성
                MyPage myPage = new MyPage();
                // view 객체에 이벤트의 Value 를 set 함
                myPage.setName(registered.getName());
                myPage.setEmail(registered.getEmail());
                myPage.setPhone(registered.getPhone());
                myPage.setStatus(registered.getStatus());
                myPage.setMemberId(registered.getId());
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenWithdrawn_then_UPDATE_1(@Payload Withdrawn withdrawn) {
        try {
            if (withdrawn.isMe()) {
                // view 객체 조회
                List<MyPage> myPageList = myPageRepository.findByMemberId(withdrawn.getId());
                for(MyPage myPage : myPageList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    myPage.setStatus(withdrawn.getStatus());
                    // view 레파지 토리에 save
                    myPageRepository.save(myPage);
                }
                /*
                Optional<MyPage> myPageOptional = myPageRepository.findById(withdrawn.getId());
                if( myPageOptional.isPresent()) {
                    MyPage myPage = myPageOptional.get();
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    myPage.setStatus(withdrawn.getStatus());
                    // view 레파지 토리에 save
                    myPageRepository.save(myPage);
                }

                 */
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}