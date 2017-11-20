$(document).ready(function() {
	var logsEndpoint = "https://webapps.amihaiemil.com/comdor/api/logs/";
	var queryString = getQueryStringParams();
    var logFile = queryString['log'];
	var url='';
	if(logFile && logFile.length > 0) {
		if(logFile.indexOf('/') == 0) {
			logFile = logFile.substring(1, logFile.length);
		}
		getLogs(logsEndpoint + logFile)
		setInterval(
		    function(){
				    getLogs(logsEndpoint + logFile)
				},
				10000
	  );
	} else {
	  getLogs("");
    }
});
function getLogs(logsurl) {
	if(logsurl == '') {
		$("#logspre").html('').append('No logs to display. Make sure you specify the log querystring param containing a valid log filename');
	} else {
    $.support.cors = true;
	  $.ajax({
	  	type : "GET",
		  url : logsurl,
		  contentType : "text/plain; charset=utf-8",
		  headers : {
			  Accept : "text/plain; charset=utf-8"
		  },
		  success : function(logs, status) {
			  if(status == "success") {
					 $("#logspre").html('').append(logs)
			  } else {
					 $("#logspre").html('').append('No logs to display. Make sure you specify the log querystring param containing a valid log filename')
			  }
		  },
		  statusCode: {
			  404: function() {
			     $("#logspre").html('').append('No logs to display. Make sure you specify the log querystring param containing a valid log filename');
			  },
			  500: function() {
				  $("#logspre").html('').append('No logs to display. Make sure you specify the log querystring param containing a valid log filename');
			  }
		  },
		  error : function(e) {
			  $("#logspre").html('').append('No logs to display. Make sure you specify the log querystring param containing a valid log filename');
		  }
	  });
  }
}
function getQueryStringParams() {
    var quertStringParams = location.search.substring(1,
        location.search.length).split('&');
    var result = {}, s2, i;
    for (i = 0; i < quertStringParams.length; i += 1) {
        s2 = quertStringParams[i].split('=');
        result[decodeURIComponent(s2[0]).toLowerCase()] = decodeURIComponent(s2[1]);
    }
    return result;
}
