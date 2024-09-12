package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.UserProfile;
import org.springframework.data.domain.Slice;

import java.util.List;

public record ProfilePageDTO(
        List<?> list,
        Long nextCursor,
        Boolean hasNext
) {
    public static ProfilePageDTO toProfileListDTO(List<ProfileDTO> profileDTOList, Slice <UserProfile> userProfileSlice) {

        Long nextCursor = userProfileSlice.hasNext() && !userProfileSlice.getContent().isEmpty()
                ? userProfileSlice.getContent().get(userProfileSlice.getNumberOfElements() - 1).getId()
                : null;

        return new ProfilePageDTO(
                profileDTOList,
                nextCursor,
                userProfileSlice.hasNext()
        );
    }
}
