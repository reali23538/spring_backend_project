package com.seed.sbp.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CommonResult<T> {

	@Schema(description = "결과 코드", example = "SUCCESS")
	private String code;

	@Schema(description = "결과값")
	private T contents;

	@Schema(description = "결과 메시지", example = "성공 하였습니다.")
	private String message;

	@Schema(description = "개발자 확인 메시지", example = "처리 성공")
	private String developerMessage;

	@Schema(description = "필드 에러 (등록시)")
	private Map<String, String> fieldErrors = new HashMap<>();

	public CommonResult(T t, CommonResultCode commonResultCode) {
		this.contents = t;
		this.setCommonResult(commonResultCode);
	}
	
	public void setCommonResult(CommonResultCode commonResultCode) {
		this.code = commonResultCode.name();
		this.message = commonResultCode.getMessage();
		this.developerMessage = commonResultCode.getDeveloperMessage();
	}

}