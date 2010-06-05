qx.Class.define("org.codefaces.ui.widgets.ajaxclient.AjaxClient", {
  extend: qx.ui.core.Widget,
    
  construct: function() {
    this.base(arguments);
  },
  
  members: {
    
    /**
     * send an JSONP request and send back the JSONP response
     *
     * @param[String] url - the HTTP URL.
     * @param[Number] timeout - timeout in milliseconds of each request. 
     *     Use default value if set to 0.
     */
    sendJsonpRequest: function(url, timeout){
      var wid = org.eclipse.swt.WidgetManager.getInstance().findIdByWidget(this);
      
      //function for sending a reply to server
      var sendResponse = function(wid, status , content){
      var req = org.eclipse.swt.Request.getInstance();
        req.addParameter(wid + '.status', status);
        req.addParameter(wid + '.content', content);
        req.send();
      };
      
      // is the response received?
      var responseReceived = false;

      // perform the call      
	  $.jsonp({
	    url: url,
	    dataType: 'jsonp',
	    timeout: timeout,
	    callbackParameter: 'callback',
	    
	    //textStatus is always 'success'
	    success: function (data, textStatus) {
	      responseReceived = true;
	      sendResponse(wid, textStatus, JSON.stringify(data));
	    },
	    
	    //textStatus can be 'error' or 'timeout'
	    error: function(xOptions, textStatus){
         responseReceived = true;
	      sendResponse(wid, textStatus, '');
	    }
	  });
	}
  }
});
