var my = {};
var isrigh=false;
my.login = function(){
    if(isrigh==true) {
        var req = {};
        req.username = $('.form .username').val().trim();
        req.password = $('.form .password').val().trim();

        Af.rest('[[@{/admin/login.do}]]', req, function (data) {
            layer.msg('成功', {
                time: 1000,
                end: function () {
                    location.href = '[[@{/admin/info}]]';
                }
            })
        })
    }
    else {
        layer.msg ('请输入验证码',{
                time:1000,
                end:function (){

                }
            }
        )
    }

}
my.change=function ()
{
    var des='[[@{/code}]]';
    document.getElementById('identity').src=des+"?ts="+new Date().getTime();

}
my.verify= function ()
{
    var req = {};
    req.code = $('.form .code').val().trim();
    Af.rest('[[@{/code.do}]]', req, function(data){
        layer.msg ('正确',{
            time:1000,
            end: function(){
                isrigh=true;

            }
        })
    },function (data1,data2){
        layer.msg ('验证错误',{
            time:1000,
            end: function(){
                isrigh=false;
                my.change();
            }
        })
    })
}
my.search=function ()
{
    var req={};

    req.keyWord=$('.search .search-info').val().trim();
    alert(req.keyWord);
    Af.rest('[[@{/book/search}]]', req, function(data){
            location.href='[[@{/book/list}]]';
        }
    )
}
var up = new AfUploader();
up.setButton('.file');  // 指定<file>元素
up.setUploadUrl('[[@{/photo/upload.do}]]'); // 可以使用thymeleaf表达式
// 事件处理 'start' 'progress' 'complete' 'error' 'abort'
up.processEvent = function(event)
{
    if(event == 'progress')
    {
        // this.progress 表示进度
        $('.note').html(this.progress + '%');
    }
    else if(event == 'complete')
    {

        var result = JSON.parse(this.response);
        var imageUrl = result.data.url;
        $('.preview img').attr('src', imageUrl);
        document.getElementById('preview').style.display="block";
    }
};

// 是否允许上传
up.beforeUpload = function(){
    if(this.file.size > 10*1000000)
    {
        alert("文件太大!");
        return false;
    }
    return true;
};

my.upload = function(){
    // 选择文件，并上传
    up.openFileDialog();
}
my.init=function (){
    document.getElementById('preview').style.display="none";
}
my.register=function (){
    if(isrigh==true) {
        var req={};
        req.id = $('.form .userID').val().trim();
        req.name = $('.form .name').val().trim();
        req.sex = $('.form .sex').val().trim();
        req.birth = $('.form .birth').val().trim();
        req.tel = $('.form .tel').val().trim();
        req.password = $('.form .password').val().trim();
        Af.rest('[[@{/register.do}]]', req, function (data) {
            layer.msg('成功', {
                time: 1000,
                end: function () {
                    location.href = '[[@{/borrower/info}]]';
                }
            })
        },function (data1,data2){
            layer.msg ('该用户id已注册',{
                time:1000,
                end: function(){
                    isrigh=false;
                    my.change();
                }
            })
        })
    }
    else {
        layer.msg ('请输入验证码',{
                time:1000,
                end:function (){

                }
            }
        )
    }

}