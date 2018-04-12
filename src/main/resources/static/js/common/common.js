/**
 * 全局变量事件 app
 */

window.app = {};
/**
 * 提示
 */
window.app.tips = new (function(){
	//<div style=" background: white;"><img src="../images/tip/404.png" style=" margin: 0px auto; "></div>
	this.loading = function(config){
		//$('body').append('<div id="loadingDiv"><img src="../images/j0_loading_100.gif"></div>');
		var $loadingDom = $('<div id="loadingDiv"><img src="/static/images/loading_124.gif"></div>');
		var targetSelector = 'body' ;
		if(config){
			config.css && $loadingDom.css(config.css);
			targetSelector = config.target || targetSelector;
			config.time &&　setTimeout(function(){ app.tips.close(); },config.time);
		}
		$(targetSelector).append($loadingDom);
	}
	//带文字加载
	this.swalLoading = function(title,closeTime){
		var loadSwal=swal({ title : '',
							text : title,
							imageUrl : '../images/loading_124.gif',
							showConfirmButton:false,
							allowEscapeKey: false}); /** 加载loding */
		closeTime && this.close(closeTime);
	}
	this.closeLoading = function(){
		$('body #loadingDiv').remove();
	}
	//关闭
	this.close = function(time){
		if(time){
			setTimeout(function(){
				$('body #loadingDiv').remove();
				swal.close();
			},time);
		}else{
			$('body #loadingDiv').remove();
			swal.close();
		}
	}
	this.loadEmptyTip = function($tpl,i){
		$tpl.html('<div class="m tipDiv" style="margin: 100px 0px;"><div class="help-tip"><img style="margin: 0px auto;" src="../images/tip/tip'+i+'.png" /></div></div>');
	}
	//显示客户端提示框
	this.showAppDialog = function(config,callback){
		var currWindow = window.parent ? window.parent.window : window;
		currWindow.messageBoxCallback = function(result){		//点击回调
        	if(result == 'ok'){
            	callback();
        	}
            delete currWindow.messageBoxCallback; //注销事件
        }
		callAppInterface('showMessage',JSON.stringify(config),1);
	}
	//error
	this.error = function(text){
		if($('#resourceList').length && !$('.pre-cont-fr').hasClass('expand')) return;
		swal("error...",text,'error')
	}
	//success
	this.success = function(text){
		swal("success...",text,'success')
	}
	//对话框
	this.dialogBox = function(config){
		swal({
		    title: config.title  || '',
		    text: config.text || '',
		    type: config.type  || 'warning',
		    showCancelButton: true,
		    confirmButtonColor: "#DD6B55",
		    confirmButtonText: i18n['ok'],
		    cancelButtonText: i18n['cencel'],
		    closeOnConfirm: false,
		    closeOnCancel: false,
		    allowEscapeKey: config.escFlag || false
		}, function(isConfirm) {
		    if (isConfirm) {	//确定
		    	config.okCallback && config.okCallback();
			} else {	//取消
				swal.close();
				config.cencelCallback && config.cencelCallback();
			}
		});
	}
})