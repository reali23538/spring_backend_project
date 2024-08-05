package com.seed.sbp.common.response;

import lombok.Getter;

@Getter
public enum CommonResultCode {

	SUCCESS( "성공 하였습니다.", "처리 성공"),
	SUCCESS_CREATE( "등록 되었습니다.", "등록 성공"),
	FAIL("실패 하였습니다.", "처리 실패"),

	// Common
	COMMON_INTERNAL_SERVER_ERROR( "처리 실패 하였습니다.", "서버 내부 에러"),
	COMMON_INVALID_PARAMS( "등록 값을 확인해주세요.", "유효하지않은 파라미터"),
	COMMON_NO_CONTENT( "존재하지않는 컨텐츠입니다.", "존재하지않은 컨텐츠"),
	COMMON_FILE_NOT_EXIST("파일을 선택 후 업로드해주세요.", "파일이 존재하지 않습니다."),
	COMMON_FILE_PROCESSING_FAIL("파일 업로드에 실패하였습니다.","파일 업로드 실패"),

	// 인증
	NOT_FOUND_USER("해당 아이디(이메일)는 회원이 아닙니다.","등록되지않은 아이디(이메일)"),
	LOCKED_USER("잠긴 계정입니다. 관리자에게 문의해주세요.", "잠김 계정"),
	WRONG_ID_OR_PASSWORD("아이디(이메일)와 패스워드를 확인해주세요. (5회 실패시 계정 잠김)","잘못된 아이디(이메일) or 패스워드"),
	NOT_VALID("다시 로그인 해주세요.", "유효하지 않은 리프레쉬 토큰 or 리프레쉬 토큰 유효기간 만료"),
	WRONG_REFRESH_TOKEN("다시 로그인 해주세요.", "잘못된 리프레쉬 토큰"),
	;

	private String message; // 결과 메시지

	private String developerMessage; // 개발자 확인 메시지

	CommonResultCode(String message, String developerMessage) {
		this.message = message;
		this.developerMessage = developerMessage;
	}

}
