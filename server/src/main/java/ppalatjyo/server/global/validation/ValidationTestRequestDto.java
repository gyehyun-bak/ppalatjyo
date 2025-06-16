package ppalatjyo.server.global.validation;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class ValidationTestRequestDto {
    @NotBlank(message = "이름은 필수 입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotNull(message = "나이는 필수입니다.")
    @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
    private Integer age;

    @NotNull(message = "주문 수량은 필수입니다.")
    @Max(value = 999, message = "주문 수량은 한 번에 최대 999개 입니다.")
    private Integer quantity;

    @NotNull(message = "전화번호는 필수입니다.")
    @Size(min = 10, max = 15, message = "전화번호는 10자 이상 15자 이하여야 합니다.")
    private String phone;

    @NotEmpty(message = "태그를 최소 1개 이상 입력해야 합니다.")
    private List<@NotBlank(message = "태그는 빈 값일 수 없습니다.") String> tags;
}
