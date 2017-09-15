<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
<meta name="viewport"
	content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0 minimal-ui"></meta>
<meta name="apple-mobile-web-app-capable" content="yes"></meta>
<meta name="apple-mobile-web-app-status-bar-style" content="white"></meta>
<title>结果</title>

<script type="text/JavaScript" src="static/js/wx.js"></script>
<script type="text/JavaScript" src="static/js/jquery-1.9.1.min.js"></script>

<style type="text/css">
.pay_msg {
	font-size: 18px;
	line-height: 43px;
	margin-left: 10px;
}

.preview_item {
	overflow: hidden;
	line-height: 1.5em;
	padding: 10px 25px;
}

.preview_label {
	float: left;
	margin-right: 1em;
	min-width: 4em;
	color: #999999;
	text-align: justify;
	text-align-last: justify;
}

.statu {
	width: 45px;
	vertical-align: middle;
}
</style>
<script type="text/javascript">
	function pageCloseSubmit() {
		if (confirm("你确认要关闭该页面吗？")) {
			 WeixinJSBridge.call('closeWindow');   // 关闭微信浏览器
			 window.opener=null;
			 window.open('','_self');
			 window.close();   // 关闭电脑浏览器
		}
	}
	
	var _ctx = "http://127.0.0.1:8081/jiddProjects";
	
	function userLogin() {
		var type = "json";
		//type = "text";
		$.ajax({
			url : _ctx + '/auxiliary/index.htm',
			method : 'POST',
			data : {
				"mobile" : "13612341234",
				"verifyCode" : "123123",
				"action" : 'login',
				"userName" : "u",//用户名
				"password" : "pwd",
				"imageCode" : "123abc",
				"channel" : "c"
			},
			dataType : type,
			//dataType : 'json',
			//dataType : 'text',
			success : function(data) {
				
				if('json'==type){
					buildHtml(data.data);
				}
				
				if('text'==type){
					var dataObj=eval("("+data+")");
					buildHtml(dataObj);
				}
				
			}
		});
	}
	
	//构建内容  
	function buildHtml(jsonData){
		if(jsonData){
	        var tr = [
	            '<tr>',
	              '<td>',jsonData.channel,'</td>',
	              '<td>',jsonData.mobile,'</td>',
	              '</tr>'  
	        ].join('');
	        $("#gripTablebody").append(tr);
	        
	        var tr = [
	            '<tr>',
	              '<td>',jsonData.verifyCode,'</td>',
	              '<td>',jsonData.imageCode,'</td>',
	              '</tr>'  
	        ].join('');
	        $("#gripTablebody").append(tr);
	        
	        var tr = [
	            '<tr>',
	              '<td>',jsonData.desc,'</td>',
	              '</tr>'  
	        ].join('');
	        $("#gripTablebody").append(tr);
		}
	}
	//构建内容  
	function buildHtml_list(list){
	    $.each(list,function(i,val){
	        var tr = [
	            '<tr>',
	              '<td>',val.noticeId,'</td>',
	              '<td>',val.title,'</td>',
	              '</tr>'
	        ].join('');
	        $("#gripTablebody").append(tr);
	    });
	}
	
</script>
</head>
<body>
	<div class="weui_msg">
		<div class="weui_icon_area">
			<img src="static/images/succ_green.png" /> <span
				class="pay_msg"><h2>jiddProjects is deployed successfully</h2></span>
		</div>
	</div>

	<div>
		<div class="preview_item">
			<form action="user/login.htm">
				<input class="weui_input" type="tel" pattern="[0-9]*" placeholder="请输入手机号" id="mobile" name="mobile" maxlength="15"></input>
				<input class="weui_input" type="text" placeholder="请输入用户名" id="userName" name="userName" maxlength="15" />
				<input class="weui_input" type="hidden" id="channel" name="channel" value="channel_test" />
				<br/>
				<input type="button" value="button" onclick="pageCloseSubmit();">
				<br/>
				<input type="submit" value="submit试试">
			</form>
			<br/>
			<br/>
			<div>
				<a id="reg_btn" href="#" onclick="userLogin()">auxiliary/index</a>
				<table style="width: 100%">
					<!-- <thead>
						<tr>
							<th>channel</th>
							<th>mobile</th>
							<th>verifyCode</th>
							<th>imageCode</th>
							<th>desc</th>
						</tr>
					</thead> -->
					<tbody id="gripTablebody">
					</tbody>
				</table>
			</div>
			<ul>
				<li><a href="auxiliary/index.htm?channel=c&userName=u">auxiliary/index</a></li>
				<li><a href="user/userInfo.htm">userInfo</a></li>
				<li><a href="user/index.htm">index</a></li>
				<li><a href="user/toLogin.htm">toLogin</a></li>
				<li><a href="user/info.htm">info</a></li>
				<li><a href="user/publicEntrance_1.htm">publicEntrance_1</a></li>
				<li><a href="user/publicEntrance_2.htm">publicEntrance_2</a></li>
				<li><a href="user/toSuccess.htm">toSuccess</a></li>
				<li><a href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb17ce3d03ed8073e&redirect_uri=http://10.148.21.80:8081/jiddProjects/user/info.htm?pubId=gh_51790c1ef5c3&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect">wechat</a></li>
			</ul>
		</div>
	</div>
</body>
</html>
