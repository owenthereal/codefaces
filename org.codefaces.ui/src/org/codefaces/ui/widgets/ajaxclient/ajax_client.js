qx.Class.define("org.codefaces.ui.widgets.ajaxclient.AjaxClient", {
  extend: qx.ui.core.Widget,
    
  construct: function() {
    this.base(arguments);
  },
  
  members: {
    
  
    /**
     * send an HTTP request and send back the HTTP response
     *
     * @param[String] url - the HTTP URL.
     * @param[String] method - request method. Can be "GET", "POST", 
     *     "PUT", "HEAD", "DELETE", "OPTIONS".
     * @param[Boolean] asyn - if set to false, it will stop the script 
     *     execution until the response was received.
     * @param[Number] timeout - timeout in milliseconds of each request. 
     *     Use default value if set to null.
     */
    sendHttpRequest: function(url, method, asyn, timeout){
      var wid = org.eclipse.swt.WidgetManager.getInstance().findIdByWidget(this);
      
	  $.ajax({
	    url: url,
	    dataType: 'jsonp',
	    type: method,
	    async: false,
	    success: function (data, textStatus) {
	      var req = org.eclipse.swt.Request.getInstance();
	      req.addParameter(wid + ".statusCode", "500");
          req.addParameter(wid + ".content", JSON.stringify(data));
          req.send();
	    } 
	  });
	}
  }
});
