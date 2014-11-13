$(document).ready(function() {
	$('#smsForm').submit(function(event) {
		var stockMessage = $('#stockMessage').val();
		var check = stockMessage.search(/CHART/i);
		var phoneNumber="7259966676";
		console.log(check);
		var messageData = "stockMessage=" + encodeURIComponent(stockMessage)+"&phoneNumber=" + phoneNumber;
		console.log("messageData"+messageData);
		
		$('#receiveSMS').show();
		if(check>0){
			$('#responseText').html('<img src="/smsmarketing/received.htm?'+messageData+'"/>');
		}
		else{
			$.ajax({
				url : $('#smsForm').attr("action")+".htm",
				data : messageData,
				type : "GET",
				contentType: "text/plain",
				success : function(response) {
					$('#responseText').text(response);
				},
				error : function(xhr, success, error) {
					alert(xhr.responseText);
				}
			});
		}
		return false;
	});
});