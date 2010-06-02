class qx.Class.define( "org.codefaces.ui.widgets.ajaxclient.AjaxClient", {
  extend: qx.core.Object,
  construct : function() {},
  
  members {
    
    __sendReponse: function(evt){
      var wid = org.eclipse.swt.WidgetManager.getInstance().findIdByWidget(this);
      var req = org.eclipse.swt.Request.getInstance();
      req.addParameter(wid + ".statusCode", evt.getStatusCode);
      req.addParameter(wid + ".content", evt.getContent);
      req.send();
    }, 
  
    sendHttpRequest : function(url, method, async, timeout){
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
