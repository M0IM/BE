package com.dev.moim.domain.account.controller;

import com.dev.moim.domain.account.dto.CreateReviewDTO;
import com.dev.moim.domain.account.dto.ReviewListDTO;
import com.dev.moim.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "유저 관련 컨트롤러")
public class UserController {

    @Operation(summary = "멤버 후기 작성 API", description = "멤버 후기 작성을 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/users/reviews")
    public BaseResponse<String> postMemberReview(@RequestBody CreateReviewDTO createReviewDTO) {
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "멤버 후기 상세보기 API", description = "멤버 후기를 조회 합니다. _by 제이미_")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/users/{userId}/reviews")
    public BaseResponse<ReviewListDTO> getMemberReview(@PathVariable Long userId) {
        return BaseResponse.onSuccess(null);
    }
}
