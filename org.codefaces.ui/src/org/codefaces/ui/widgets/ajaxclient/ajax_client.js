class qx.Class.define("org.codefaces.ui.widgets.ajaxclient.AjaxClient", {
  extend: qx.core.Object,
  construct : function() {},
  
  members: {
    
    /**
     * Callback function when a HTTP response is received. Return back 
     * the status code and the content of the response.
     */
    __sendReponse: function(evt){
      var wid = org.eclipse.swt.WidgetManager.getInstance().findIdByWidget(this);
      var req = org.eclipse.swt.Request.getInstance();
      req.addParameter(wid + ".statusCode", evt.getStatusCode);
      req.addParameter(wid + ".content", evt.getContent);
      req.send();
    }, 
  
    /**
     * send an HTTP request and send back the HTTP response
     *
     * @param[String] url - the HTTP URL.
     * @param[String] method - request method. Can be "GET", "POST", 
     *     "PUT", "HEAD", "DELETE", "OPTIONS".
     * @param[Boolean] async - if set to false, it will stop the script 
     *     execution until the response was received.
     * @param[Number] timeout - timeout in milliseconds of each request. 
     *     Use default value if set to null.
     */
    sendHttpRequest: function(url, method, async, timeout){
      ajaxCall = new qx.io.remote.Request(url, method, "text/plain");
      ajaxCall.setAsynchronous(async);
      ajaxCall.setCrossDomain(true);
      ajaxCall.setTimeout(timeout);
      ajaxCall.addListener("completed", __sendReponse);
      ajaxCall.addListener("timeout", __sendReponse);
      ajaxCall.addListener("failed", __sendReponse);
    }
  }
});
