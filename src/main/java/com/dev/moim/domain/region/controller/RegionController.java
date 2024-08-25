package com.dev.moim.domain.region.controller;

import com.dev.moim.domain.region.dto.RegionListDTO;
import com.dev.moim.domain.region.dto.RegionSearchListDTO;
import com.dev.moim.domain.region.service.RegionQueryService;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.validation.annotation.CheckCursorValidation;
import com.dev.moim.global.validation.annotation.CheckTakeValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/regions")
@Tag(name = "주소 관련 컨트롤러")
public class RegionController {

    private final RegionQueryService regionQueryService;

    @GetMapping("/sido")
    @Operation(summary="행정구역 (시/도) 조회 API", description="행정구역 (시/도) 조회 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    public BaseResponse<RegionListDTO> getSido(
    ) {
        return BaseResponse.onSuccess(regionQueryService.getSido());
    }

    @GetMapping("/sigungu")
    @Operation(summary="행정구역 (시/군/구) 조회 API", description="행정구역 (시/군/구) 조회 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    public BaseResponse<RegionListDTO> getSigungu(
            @Parameter(description = "상위 시/도 ID") @RequestParam Long parentId
            ) {
        return BaseResponse.onSuccess(regionQueryService.getSigungu(parentId));
    }

    @GetMapping("/dong")
    @Operation(summary="행정구역 (동) 조회 API", description="행정구역 (동) 조회 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    public BaseResponse<RegionListDTO> getDong(
            @Parameter(description = "상위 시/군/구 ID") @RequestParam Long parentId
            ) {
        return BaseResponse.onSuccess(regionQueryService.getDong(parentId));
    }

    @GetMapping("/search")
    @Operation(summary="행정구역 검색 API", description="행정구역 검색 API 입니다.")
    @Parameter(name = "searchTerm", description = "해당 검색어가 포함된 행정구역을 조회합니다.", example = "서울특별시")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    public BaseResponse<RegionSearchListDTO> getRegions(
            @RequestParam(name = "searchTerm") String searchTerm,
            @RequestParam(name = "cursor") Long cursor,
            @CheckTakeValidation @RequestParam(name = "take") Integer take
    ) {
        return BaseResponse.onSuccess(regionQueryService.getRegions(searchTerm, cursor, take));
    }
}
