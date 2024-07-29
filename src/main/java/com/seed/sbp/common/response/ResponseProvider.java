package com.seed.sbp.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseProvider {

	public static <T> ResponseEntity<CommonResult<T>> ok() {
		return getResponseEntity(null, CommonResultCode.SUCCESS, null);
	}

	public static <T> ResponseEntity<CommonResult<T>> ok(T t) {
		return getResponseEntity(t, CommonResultCode.SUCCESS, null);
	}

	public static <T> ResponseEntity<CommonResult<T>> fail(CommonResultCode CommonResultCode) {
		return getResponseEntity(null, CommonResultCode);
	}

	public static <T> ResponseEntity<CommonResult<T>> getResponseEntity(
			T t, CommonResultCode resultCode) {
		return getResponseEntity(t, resultCode, null);
	}

	public static <T> ResponseEntity<CommonResult<T>> getResponseEntity(
			CommonResultCode resultCode, List<ObjectError> objectErrors) {
		Map<String, String> fieldErrors = new HashMap<>();

		objectErrors.forEach(objectError -> {
			FieldError fieldError = (FieldError) objectError;

			String key = String.format("%s_error", fieldError.getField());
			fieldErrors.put(key, fieldError.getDefaultMessage());
		});
		return getResponseEntity(null, resultCode, fieldErrors);
	}

	public static <T> ResponseEntity<CommonResult<T>> getResponseEntity(
			T t, CommonResultCode resultCode, Map<String, String> fieldErrors) {
		CommonResult<T> result = new CommonResult<>(t, resultCode);
		
		// httpStatus 셋팅
		HttpStatus httpStatus;
		if ( CommonResultCode.SUCCESS == resultCode ) {
			httpStatus = HttpStatus.OK;
		} else if ( CommonResultCode.SUCCESS_CREATE  == resultCode ) {
			httpStatus = HttpStatus.CREATED;
		} else if (
				CommonResultCode.COMMON_INVALID_PARAMS == resultCode
			 || CommonResultCode.NOT_FOUND_USER == resultCode
			 || CommonResultCode.WRONG_ID_OR_PASSWORD == resultCode
		) {
			httpStatus = HttpStatus.BAD_REQUEST;
			result.setFieldErrors(fieldErrors);
		} else if ( CommonResultCode.COMMON_NO_CONTENT == resultCode ) {
			httpStatus = HttpStatus.NO_CONTENT;
		} else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(result, httpStatus);
	}

}
