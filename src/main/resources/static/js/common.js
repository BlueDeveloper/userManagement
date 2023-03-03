

/**********************************************
 * 공통으로 사용하는 AJAX 함수
 * @param type 			 - ajax 호출 type  ex) GET, POST, PUT, DELETE
 * @param url 			 - ajax 호출 url
 * @param data 			 - ajax param
 * @param beforeSendFunc - ajax beforeSend 수행 함수
 * @param successFunc 	 - ajax success 수행 함수
 * @param errorFunc 	 - ajax error 수행 함수
 *********************************************/

function ajaxCall(type, url, data, beforeSend, success, error){
	/*if(tradition == true) {
		$.ajaxSettings.traditional = true;
		tradition=false;
	}*/
	$.ajaxSettings.traditional = true;
	$.ajax({
        type: type,
        url: url,
        data: data,
        dataType : "json",
        beforeSend: beforeSend,
        success: success,
        error: error
	});
}

/**********************************************
 * data가 배열일 경우 사용하는 AJAX 함수
 * Controller에서 @RequestBody String으로 받고
 * 파싱해서 사용 가능
 * @param url 			 - ajax 호출 url
 * @param data 			 - ajax param
 * @param beforeSendFunc - ajax beforeSend 수행 함수
 * @param successFunc 	 - ajax success 수행 함수
 * @param errorFunc 	 - ajax error 수행 함수
 *********************************************/

function ajaxCallList(url, data, beforeSend, success, error){
	$.ajax({
		type: "POST",
		url: url,
        data : JSON.stringify(data),
        dataType:"json",
        contentType: "application/json; charset=utf-8",
        beforeSend: beforeSend,
        success: success,
        error: error
	});
}

function ajaxFormData(url, data, beforeSend, success, error){
	$.ajaxSettings.traditional = true;
	$.ajax({
        type       : "POST",
        url        : url,
        data       : data,
        cache      : false,
		contentType: false,
		processData: false,
		beforeSend : beforeSend,
        success    : success,
        error      : error
	});
}