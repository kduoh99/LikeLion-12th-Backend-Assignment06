package org.likelion.likelionassignmentcrud.developer.api.dto.request;

public record DeveloperSaveReqDto(
        // 이름, 국가, 설립일
        String developerName,
        String country,
        String establishedDate
) {
}
