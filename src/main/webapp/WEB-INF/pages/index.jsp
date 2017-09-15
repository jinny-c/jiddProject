<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Basic Tabs - jQuery EasyUI Demo</title>
<link rel="stylesheet" type="text/css" href="static/jquery-easyui-1.5.1/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="static/jquery-easyui-1.5.1/themes/icon.css">
<link rel="stylesheet" type="text/css" href="static/jquery-easyui-1.5.1/demo/demo.css">
<script type="text/javascript" src="static/jquery-easyui-1.5.1/jquery.min.js"></script>
<script type="text/javascript" src="static/jquery-easyui-1.5.1/jquery.easyui.min.js"></script>
<script type="text/javascript">
	function submitForm() {
		//$('#ff').form('submit');
		$('#ff').submit();
	}
	function clearForm() {
		$('#ff').form('clear');
	}
	function doSearch(value,name){
		//alert(value);
		//alert(name);
		//$('#sff').form('submit');
		$('#sff').submit();
	}
	$(function() {
		$("#name").textbox("setValue", "输入汉字");
		
		var tabs = $('#ttdiv').tabs().tabs('tabs');
		for(var i=0; i<tabs.length; i++){
			tabs[i].panel('options').tab.unbind().bind('mouseenter',{index:i},function(e){
				$('#ttdiv').tabs('select', e.data.index);
			});
		}
		
		});
</script>
</head>
<body>
	<h2>jiddProjects is deployed successfully</h2>
	<h2>Hello World</h2>
	<p>Click tab strip to swap tab panel content.</p>
	<div style="margin: 20px 0 10px 0;"></div>
	<div id="ttdiv" class="easyui-tabs" style="width: 700px; height: 400px">
		<div title="About" style="padding: 10px">
			<p style="font-size: 14px">jQuery EasyUI framework helps you
				build your web pages easily.</p>
			<ul>
				<li><a href="user/index.htm">index</a></li>
				<li><a href="user/toLogin.htm">toLogin</a></li>
				<li><a href="hello?name=zhangsan">点击跳转</a></li>
				<li><a href="hello.do?name=李四lisi">点击跳转</a></li>
				<li><a href="queryInterface">点击跳转json</a></li>
				<li>easyui save your time and scales while developing your
					products.</li>
				<li>easyui is very easy but powerful.</li>
			</ul>
		</div>
		<div title="My Documents" style="padding: 10px">
			<form id="sff" action="hello.action" method="post">
				<table>
					<tr><td><input id="type" name="type" class="easyui-searchbox" data-options="prompt:'type',searcher:doSearch" style="width:300px"></input></td></tr>
					<tr><td><input id="name" name="name" class="easyui-textbox" data-options="onClickButton:doSearch,buttonText:'Search',buttonIcon:'icon-search',prompt:'输入汉字'" style="width:250px;height:24px;" /></td></tr>
				</table>
			</form>

		</div>
		<div title="My Documents" style="padding: 10px">
			<form id="ff" action="queryBean" method="post">
				<table cellpadding="4" cellspacing="4">
					<tr><td>userName=</td><td>	<input class="easyui-textbox" type="text" name="userName" data-options="required:true,prompt:'这是写汉字的'" />	</td></tr>
					<tr><td>password=</td><td>	<input type="password" id="password" name="password" /></td></tr>
					<tr><td>realName=</td><td>	<input class="easyui-textbox" type="text" name="realName" data-options="required:false" />	</td></tr>
					<tr><td>userType=</td><td>	<input class="easyui-textbox" type="text" name="userType" data-options="required:false" />	</td></tr>
					<tr><td>operateFlag=</td><td>	<input class="easyui-textbox" type="text" name="operateFlag" data-options="required:false" />	</td></tr>
					<tr><td colspan="2">	<input type="submit"	class="easyui-linkbutton" value="确认" />	</td></tr>
				</table>
			</form>
			<div style="text-align: center; padding: 5px">
						<a href="javascript:void(0)" class="easyui-linkbutton"
							onclick="submitForm()">Submit</a> <a href="javascript:void(0)"
							class="easyui-linkbutton" onclick="clearForm()">Clear</a>
			</div>
		</div>
		<div title="Help" data-options="iconCls:'icon-help',closable:true"
			style="padding: 10px">This is the help content.
				<div class="easyui-panel" title="Register" style="width:400px;padding:30px 60px">
				<div style="margin-bottom:20px">
					<div>Email:</div>
					<input class="easyui-textbox" data-options="prompt:'Enter a email address...',validType:'email'" style="width:100%;height:32px">
				</div>
				<div style="margin-bottom:20px">
					<div>First Name:</div>
					<input class="easyui-textbox" style="width:100%;height:32px">
				</div>
				<div style="margin-bottom:20px">
					<div>Last Name:</div>
					<input class="easyui-textbox" style="width:100%;height:32px">
				</div>
				<div style="margin-bottom:20px">
					<div>Company:</div>
					<input class="easyui-textbox" style="width:100%;height:32px">
				</div>
				<div>
					<a href="#" class="easyui-linkbutton" iconCls="icon-ok" style="width:100%;height:32px">Register</a>
				</div>
			</div>
			
			</div>
	</div>
</body>
</html>