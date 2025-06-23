package ppalatjyo.server.global.websocket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ppalatjyo.server.global.websocket.dto.PublicationDto;
import ppalatjyo.server.global.websocket.dto.TestPublicationDataDto;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageBrokerServiceTest {

    @Mock
    SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    MessageBrokerService messageBrokerService;

    @Test
    @DisplayName("메시지 발행")
    void publish() {
        // given
        String destination = "destination";
        TestPublicationDataDto dataDto = new TestPublicationDataDto();
        dataDto.setNickname("nickname");
        dataDto.setContent("content");
        PublicationDto<TestPublicationDataDto> requestDto = new PublicationDto<>();
        requestDto.setDestination(destination);
        requestDto.setData(dataDto);

        // when
        messageBrokerService.publish(requestDto);

        // then
        verify(simpMessagingTemplate).convertAndSend("/topic/" + destination, dataDto);
    }
}