var page = 0;
$(function(){
	$('.back-left').click(function(){
		xyz.back();
	});
	$('.icon-search').click(function(){
		$('.search-show').slideToggle(100);
	});
	$("#searchBtn").click(function(){
		queryList(page=1);
	});
	
	$(".dropload-down").remove();
	$('#pullrefresh_order').dropload({
		scrollArea : window,
		loadUpFn : function(me){
			queryList(page=1,me);
		},
		loadDownFn : function(me){
			queryList(++page,me);
		}
	});
});

function queryList(page,dropload){
	xyz.ajax({
		url:'BuyerProviderWS/queryProviderList.app',
		progress:false,
		data : {
			nameCn : xyz.id('spotNameForm').value,
			page : page,
			rows : 5,
			providerType : 'HO',
			orderBy : 'alterDate DESC'
		},
		success : function(data) {
			if(data.status==1){
				var result = data.content;//当前数据条数
				var table = document.body.querySelector('#tableList');
				if(page==1){
					table.innerHTML = '';
				}
				createHtml(table , result.rows);
				//重置
				if(dropload){
					dropload.resetload();
					if(page==1){
						dropload.unlock();
						dropload.noData(false);
					}
					if(result.rows.length<5){
						dropload.lock('down');
						dropload.noData();
					}
				}
			}else{
				if(dropload){
					dropload.lock();
					dropload.noData();
					dropload.resetload();
				}
				weui.toast(data.msg);
			}
		},error: function(xhr, type){
            weui.toast('无法访问服务器啦');
            if(dropload){
            	dropload.lock();
				dropload.noData();
				dropload.resetload();
			}
        }
	});
}

/*function queryList(nameCn){
	var rows = 0; //记录条数
	var page = 0; //每页数
	$(".dropload-down").remove();
	$('#pullrefresh_order').dropload({
		scrollArea : window,
		loadDownFn : function(me){
			rows += 5; //记录以及加载的条数
			page++ ; //没调用一次  页数加1
			xyz.ajax({
				url:'BuyerProviderWS/queryProviderList.app',
				progress:false,
				data : {
					nameCn : nameCn,
					page : page,
					rows : 5,
					providerType : 'HO'
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
					}else{
						alert(data.msg);
					}
					me.resetload();
				},error: function(xhr, type){
	                alert('Ajax error!');
	                me.resetload();
	            }
			});
		}
	});
}*/

function createHtml(table , list){
	for(var i=0; i<list.length; i++){
		var o = list[i];
		var li = document.createElement('li');
		li.className = 'index-hotel-list';
		var url = xyz.setUrlparam(xyz.getFullurl('page/productHotelList.html'),{numberCode:o.numberCode});
		var html = '<div class="index-hotel-img">';
        html +='<a href="javascript:void(0);" title="" onclick="window.location.href=\''+url+'\'"><img src="'+xyz.getMiddleImageUrl(o.imageUrl)+'" alt=""/></a>';
        html +='</div>';
        html +='<div class="index-hotel-info">';
        html +='<p class="index-hotel-name">';
        html +='<a href="javascript:void(0);">'+o.nameCn+'</a>';
        html +='</p>';
        html +='<p class="index-hotel-price">';
        if(o.price && !isNaN(o.price)){
        	html +='<i>¥'+o.price+'</i>起';
        }else{
        	html +='<i>暂无报价</i>';
        }
        html +='</p>';
        html +='</div>';
		
		//html += '<span id="json_'+o.numberCode+'" style="display:none;">'+JSON.stringify(o)+'</span>';
		li.innerHTML = html;
		table.appendChild(li);
	}
}
