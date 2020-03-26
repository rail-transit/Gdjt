$(function() {
    selectModel();
});

/**
 * 模拟网页中所有下拉列表select
 * @return {[type]} [description]
 */
function selectModel() {
    var $box = $('div.model-box');
    var $option = $('ul.model-select-option', $box);
    var $txt = $('div.model-select-text', $box);
    var speed = 10;
    /**
     * 单击某个下拉列表时，显示当前下拉列表的下拉列表框
     * 并隐藏页面中其他下拉列表
     */
    $box.on('click', function() {
        // $option.not($self).siblings('ul.model-select-option').slideUp(speed, function() {//有问题
        //     console.log("test01");
        //     init($self);
        // });
        if($(this).hasClass("on")){ //已展开状态 此处$(this)为$box 即 $('div.model-box')
            $(this).find(".model-select-option").slideUp(10,function () { //此处$(this)为$box 即 $('div.model-box') 当前点击的div
                $(this).parent(".model-box").removeClass("on");// 此处$(this)为$(this).find(".model-select-option") ul
            });
        }else{//收缩状态
            $(this).parents("table").find(".model-box").each(function(){ //此处$(this)为$box 即 $('div.model-box') 当前点击的div
                if($(this).hasClass("on")){ //此处$(this)为循环到的.model-box div
                    $(this).find(".model-select-option").slideUp(10,function () { //此处$(this)为循环到的.model-box div
                        $(this).parent(".model-box").removeClass("on"); //此处$(this)为$(this).find(".model-select-option") ul
                    });
                }
            });


            //当高度不够展开时设置高度，出现滚动条
            if ($("#cont_b").height() - $(this).position().top - $(this).outerHeight() - $(this).find(".model-select-option").position().top  < $(this).find(".model-select-option").outerHeight()){
                $(this).find(".model-select-option").css({
                    "height": ($("#cont_b").height() - $(this).position().top - $(this).outerHeight() - parseInt($(this).find(".model-select-option").css("top"))  - ($(this).find(".model-select-option").outerHeight() - $(this).find(".model-select-option").height())) +"px",
                    "overflow": "auto"
                });
            }
            $(this).find(".model-select-option").slideDown(10,function(){ //此处$(this)为$box 即 $('div.model-box') 当前点击的div

                // //当高度不够展开时设置高度，出现滚动条
                // if ($("#cont_b").height() - ($(this).parent(".model-box").position().top + $(this).parent(".model-box").height() + $(this).position().top) < $(this).height()){
                //     $(this).css({
                //         "height": ($("#cont_b").height() - ($(this).parent(".model-box").position().top + $(this).parent(".model-box").height() + $(this).position().top)) +"px",
                //         "overflow": "auto"
                //     });
                // }
                $(this).parent(".model-box").addClass("on");  //此处$(this)为$(this).find(".model-select-option") ul
            });
        }

        // $(this).parents("table").find(".model-select-option").slideUp(10,function(){
        //     // if(){}
        //     console.log("收缩");
        // });
        // $(this).find(".model-select-option").slideDown(10,function(){
        //     if ($("#cont_b").height() - ($("#bbbbbb").position().top + window.document.getElementById("bbbbbb").offsetHeight + $("#aaaaaa").position().top) < window.document.getElementById("aaaaaa").offsetHeight){
        //         $("#aaaaaa").css({
        //             "height": ($("#cont_b").height() - ($("#bbbbbb").position().top + window.document.getElementById("bbbbbb").offsetHeight + $("#aaaaaa").position().top)) +"px",
        //             "overflow": "auto"
        //         });
        //     }
        //     console.log("展开");
        // });

        // $self.siblings('ul.model-select-option').slideToggle(speed, function() {
        //     if ($("#cont_b").height() - ($("#bbbbbb").position().top + window.document.getElementById("bbbbbb").offsetHeight + $("#aaaaaa").position().top) < window.document.getElementById("aaaaaa").offsetHeight){
        //         $("#aaaaaa").css({
        //             "height": ($("#cont_b").height() - ($("#bbbbbb").position().top + window.document.getElementById("bbbbbb").offsetHeight + $("#aaaaaa").position().top)) +"px",
        //             "overflow": "auto"
        //         });
        //     }
        //     init($self);
        //
        // });
        // return false;
    });

    // 点击选择，关闭其他下拉
    /**
     * 为每个下拉列表框中的选项设置默认选中标识 data-selected
     * 点击下拉列表框中的选项时，将选项的 data-option 属性的属性值赋给下拉列表的 data-value 属性，并改变默认选中标识 data-selected
     * 为选项添加 mouseover 事件
     */
    $option.find('li').each(function(index, element) {
        var $self = $(this);
        // if ($self.hasClass('selected')) {
        //     console.log("test03---each");
        //     $self.addClass('data-selected');
        // }
    }).mousedown(function() {
        $(this).parent().siblings('div.model-select-text').text($(this).text()).attr('data-value', $(this).attr('data-option'));
        $option.slideUp(speed, function() {
            // init($(this));
            $(this).parent(".model-box").removeClass("on");
        });
        $(this).addClass('selected data-selected').siblings('li').removeClass('selected data-selected');
    }).mouseover(function() {
        $(this).addClass('selected').siblings('li').removeClass('selected');
    });
    // 点击当前窗口隐藏所有下拉列表框
    $("#cont_b").on('click', function() {
        // $option.slideUp(speed, function() {
        //     console.log("点击当前窗口隐藏下拉");
        //     init($self);
        // })
        $(this).find(".model-box").each(function(){ //此处$(this)为$box 即 $('div.model-box') 当前点击的div
            if($(this).hasClass("on")){ //此处$(this)为循环到的.model-box div
                $(this).find(".model-select-option").slideUp(10,function () { //此处$(this)为循环到的.model-box div
                    $(this).parent(".model-box").removeClass("on"); //此处$(this)为$(this).find(".model-select-option") ul
                });
            }
        });
    });

    // /**
    //  * 初始化默认选择
    //  */
    // function init(obj) {
    //     obj.find('li.data-selected').addClass('selected').siblings('li').removeClass('selected');
    // }
}