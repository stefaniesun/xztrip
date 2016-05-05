$(function(){
	var o = xyz.getUrlparam();
	urlprovider = o.provider;
	$("#hotel_title").html(o.nameCn);
	queryList(1);
});

function queryList(){
	var rows = 0; //记录条数
	var page = 0; //每页数
	$('#show_content_1').dropload({
		scrollArea : window,
		domDown : {
			domClass : 'dropload-down',
			domRefresh : '<div class="dropload-refresh">↑上拉加载更多</div>',
			domLoad : '<div class="dropload-load">加载中...</div>',
			domNoData : '<div class="dropload-noData">暂无数据</div>'
		},
		autoLoad : true, //自动加载
		loadDownFn : function(me){
			rows += 5; //记录以及加载的条数
			page++ ; //没调用一次  页数加1
			xyz.ajax({
				url:'BuyerHotelWS/queryHotelList.app',
				progress:false,
				data : {
					page : page,
					rows : 5,
					provider : urlprovider
				},
				success : function(data) {
					if(data.status==1){
						var total = data.content.total; 
						var result = data.content;
						var table = document.body.querySelector('#tableList');
    					if(page==1){
    						table.innerHTML = '';
    					}
    					createHtml(table , result.rows);
						if(rows >= total){ //每页数据
							me.lock();
							me.noData();
						}
						me.resetload();
					}else{
						alert(data.msg);
					}
				}
			});
		}
	});
}

function createHtml(table , list){
	for(var i=0; i<list.length; i++){
		var o = list[i];
		var div = document.createElement('div');
		div.className = 'room-list-item default-width';
		var url = xyz.setUrlparam('hotel_reservation.html',{provider:o.numberCode,nameCn:o.nameCn});
		var btnDisabled = 'disabled="disabled"';
		var btnStyle = 'background:#D4D4D4;';
		var html = '<div class="room-item-name">'+o.nameCn+'</div>';
        html +='<div class="room-item-peice">';
        if(o.price && !isNaN(o.price)){
        	html +='<i>¥'+o.price+'</i>起';
        	btnDisabled = '';
        	btnStyle = '';
        }else{
        	html +='<i><font color="#ccc" size="1">暂无报价</font></i>';
        	btnDisabled = 'disabled="disabled"';
        	btnStyle = 'background:#D4D4D4;';
        }
        html +='</div>';
        
        html +='<div class="ticket-item-btn"><button '+btnDisabled+' class="order-btn" style="'+btnStyle+'" onclick="window.location.href=\''+url+'\'">预订</button></div>';
		//html += '<span id="json_'+o.numberCode+'" style="display:none;">'+JSON.stringify(o)+'</span>';
		div.innerHTML = html;
		table.appendChild(div);
	}
}
