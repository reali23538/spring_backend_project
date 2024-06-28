package com.seed.sbp.common.response;

import lombok.Getter;

@Getter
public enum CommonResultCode {

	SUCCESS( "성공 하였습니다.", "처리 성공"),
	SUCCESS_CREATE( "등록 되었습니다.", "등록 성공"),
	FAIL("실패 하였습니다.", "처리 실패"),

	// 공통
	COMMON_INTERNAL_SERVER_ERROR( "처리 실패 하였습니다.", "서버 내부 에러"),
	COMMON_INVALID_PARAMS( "등록 값을 확인해주세요.", "유효하지않은 파라미터"),
	COMMON_NO_CONTENT( "존재하지않는 컨텐츠입니다.", "존재하지않은 컨텐츠"),
	COMMON_FILE_NOT_EXIST("파일을 선택 후 업로드해주세요.", "파일이 존재하지 않습니다."),
	COMMON_FILE_PROCESSING_FAIL("파일 업로드에 실패하였습니다.","파일 업로드 실패"),
	;

	private String message; // 결과 메시지

	private String developerMessage; // 개발자 확인 메시지

	CommonResultCode(String message, String developerMessage) {
		this.message = message;
		this.developerMessage = developerMessage;
	}

}
