/**
 * 创建人：张浩
 * 创建时间：2019.7.22
 */

// 页面加载时绑定事件
$(document).ready(function(){
	gerDetails();
	// 查询信息
	$("#accurateSearch-button").click(search);
	$("#fuzzySearch-button").click(search);
});
var temp;

// 精准搜索
function search(){
	// 删除当前所有搜索结果
	$("#content").empty();

	var searchMethod = $(this).attr("id").split("-")[0];
	$.ajax({
		type:"post",
		url:searchMethod,
		data:JSON.parse(JSON.stringify(getSearchInfo())),
		cache:false,
		success:function(message){
			if(message.length!=0){
				console.log(message);
				temp = message;

				// 添加dom
				for(var i=0;i<message.length;i++){
					$("#content").append("" +
						"<div class=\"content-row\">\n" +
						"        <div class=\"detail-content\">\n" +
						"            <!-- 此处src属性由前台生成 -->\n" +
						"            <a"+" href='details?"+message[i].fileName+"'>文件名："+message[i].fileName+"</a>\n" +
						"        </div>\n" +
						"        <div class=\"abbr-content\">\n" +
						"            <div class=\"img-content\">\n" +
						"                <img src=\"img/"+message[i].fileName+".jpg\">\n" +
						"            </div>\n" +
						"            <div class=\"text-content\">\n" +  message[i].ocrText+
						"            </div>\n" +
						"        </div>\n" +
						"</div>");
				}

				// 修改搜索信息
				$(".searchInfo").removeClass("hide");
				$(".searchInfo label span").text(message.length);
			}else{
				// 修改搜索信息
				$(".searchInfo").removeClass("hide");
				$(".searchInfo label span").text("0")
			}
		},
		error:function(message){
			console.log(message);
		}
	})
}

// 获取查询的信息
function getSearchInfo(){
	return {
		"searchStr":$("#searchInput").val()
	};
}

// 页面加载后向后台进行查询

//
function gerDetails(){
    var jsonData = {
        "href":getUrlPara()
    };

    $.ajax({
        type:"POST",
        url:"accurateSearch2",
        async:false,
        cache:false,
        ifModified:true,
        contentType:"application/x-www-form-urlencoded",
        data:JSON.parse(JSON.stringify(jsonData)),
        dataType:"json",
        success:function(message){
            console.log(message);
			$("#content-img-wrapper img").attr("src","img/"+getUrlPara()+".jpg");
			$("#content-text").append("<p>"+message.ocrText+"</p>")
        },
        error:function(message){
            console.log(message);
        }
    })
}

/**
 * 获取URL的参数
 * @returns {string}
 * @constructor
 */
function getUrlPara()
{
	var url = document.location.toString();
	var arrUrl = url.split("?");

	var para = arrUrl[1];
	return para;
}

// function getHref(){
//
// }