/**
 * Created by Administrator on 2016/2/18.
 */
$(document).ready(function(){
    $(".category-choice-hotel").click(function(){
        $(this).addClass("category-choice-active");
        $(".category-choice-ticket").removeClass("category-choice-active");
        $(".index-hotel-card").css("display","block");
        $(".index-ticket-card").css("display","none");
    });
    $(".category-choice-ticket").click(function(){
        $(this).addClass("category-choice-active"),
        $(".category-choice-hotel").removeClass("category-choice-active");
        $(".index-hotel-card").css("display","none");
        $(".index-ticket-card").css("display","block");
    });
    /* 返回*/
    $(".icon-left").click(function(){
    	 window.history.back();
    });
    
    $(".icon-house").click(function(){
    	window.location.href="./index.html";
    });

    /* 目录 */
    $(".icon-menu").click(function(){
        $(".menu-show").slideToggle("100");
    });
    $(".icon-search").click(function(){
        $(".search-show").slideToggle("100");
    });
    $(".category-item-price").click(function(){
        $(".category-price-list").slideToggle("100");
    });
    $(".category-item-sale").click(function(){
        $(".category-sale-list").slideToggle("100");
    });
    $(".screen-button").click(function(){
        $(".category-choice-box").slideToggle("100");
    });
    
    /*购买数量*/
    $(".icon-add").click(function(){
		var num=$("#numForm").val();
		$("#numForm").val(parseInt(num)+1);
	});
	
	$(".icon-reduce").click(function(){
		var num=$("#numForm").val();
		if(num>1){
			$("#numForm").val(parseInt(num)-1);
		}
	});
	
	

});

function showContent(index){
	$("a[id^='show_title").each(function(){
		$(this).removeAttr("class");
	});
	$(event.target).attr("class","order-active");
	$("div[id^='show_content_']").each(function(){
		$(this).hide();
	});
	$("#show_content_"+index).show();	
}