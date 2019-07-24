/**
 * 创建人：张浩
 * 创建时间：2019.7.22
 */

// 页面加载时绑定事件
$(document).ready(function(){
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
//
// function gerDetails(){
//     var jsonData = {
//         "href":$(this).attr("href")
//     }
//
//     $.ajax({
//         type:"POST",
//         url:"details",
//         async:false,
//         cache:false,
//         ifModified:true,
//         contentType:"application/x-www-form-urlencoded",
//         data:JSON.parse(JSON.stringify()),
//         dataType:"json",
//         success:function(message){
//             console.log(message);
//             if(message.status == "failed"){
//                 // 清空密码栏并提示错误信息
//                 $("#password input").val("");
//                 $("#password label").removeClass("hidden");
//             }else{
//                 $("#password label").addClass("hidden");
//                 // 检查是否有选中记住用户名和密码。如果有的话则设置cookie。
//                 if($("#checkboxG5").prop('checked')){
//                     // 设置用户名、密码及checkbox的cookie
//                     // 设置用户名cookie
//                     $.cookie('username', $(".user-info-input").val(), { expires: 7 });
//                     $.cookie('password', $("#password .user-info-input").val(), { expires: 7 });
//                     $.cookie('checked',"true",{expires:7});
//                 }else{
//                     // 清除cookie
//                     $.cookie('username', '', { expires: -1 });
//                     $.cookie('password', '', { expires: -1 });
//                     $.cookie('checked', '', { expires: -1 });
//                 }
//
//                 // 跳转页面
//                 var date = new Date();
//                 var time = date.getTime()
//                 window.location.href = "index?" + time;
//             }
//         },
//         error:function(message){
//             console.log(message);
//         }
//     });
// }
//
// function getHref(){
//
// }